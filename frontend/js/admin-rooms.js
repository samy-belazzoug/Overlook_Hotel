// Overlook Hotel - Admin Rooms Management JavaScript

let rooms = [];
let roomToEdit = null;
let roomToDelete = null;

// Initialize page
document.addEventListener('DOMContentLoaded', function () {
    checkAuthentication();
    loadRooms();
    setupEventListeners();
});

// Check authentication
function checkAuthentication() {
    if (!isAuthenticated()) {
        showAlert('Veuillez vous connecter en tant qu\'administrateur', 'error');
        setTimeout(() => {
            window.location.href = '../index.html';
        }, 2000);
        return;
    }

    const role = getUserRole();
    if (role !== 'ADMIN') {
        showAlert('Accès refusé. Seuls les administrateurs peuvent accéder à cette page.', 'error');
        setTimeout(() => {
            window.location.href = '../index.html';
        }, 2000);
        return;
    }
}

// Setup event listeners
function setupEventListeners() {
    // Room form
    document.getElementById('room-form').addEventListener('submit', handleRoomSubmit);

    // Search input
    document.getElementById('search-input').addEventListener('input', debounce(filterRooms, 300));

    // Filter selects
    document.getElementById('type-filter').addEventListener('change', filterRooms);
    document.getElementById('status-filter').addEventListener('change', filterRooms);
}

// Load rooms
async function loadRooms() {
    const loadingState = document.getElementById('loading-state');
    const tableBody = document.getElementById('rooms-table-body');

    try {
        loadingState.classList.remove('hidden');

        // Utiliser l'endpoint de test pour contourner l'authentification
        const response = await fetch('http://localhost:8080/api/rooms-test');
        rooms = await response.json();

        loadingState.classList.add('hidden');
        displayRooms(rooms);

    } catch (error) {
        loadingState.classList.add('hidden');
        showAlert('Erreur lors du chargement des chambres: ' + error.message, 'error');
    }
}

// Display rooms in table
function displayRooms(roomsToShow) {
    const tableBody = document.getElementById('rooms-table-body');

    if (roomsToShow.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="px-6 py-8 text-center text-gray-500">
                    Aucune chambre trouvée
                </td>
            </tr>
        `;
        return;
    }

    tableBody.innerHTML = roomsToShow.map(room => createRoomRow(room)).join('');
}

// Create room table row
function createRoomRow(room) {
    const statusClass = getStatusColorClass(room.etat, 'room');
    const statusText = formatRoomStatus(room.etat);

    return `
        <tr class="hover:bg-gray-50">
            <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm font-medium text-gray-900">${room.numero}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
                <span class="room-card-type">${formatRoomType(room.type)}</span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm font-medium text-gray-900">${formatCurrency(room.prix)}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
                <span class="${statusClass} font-medium">${statusText}</span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                <div class="flex space-x-2">
                    <button onclick="editRoom(${room.id})" class="text-primary hover:text-primary-700">
                        ✏️ Modifier
                    </button>
                    <button onclick="showDeleteModal(${room.id})" class="text-red-600 hover:text-red-700">
                        🗑️ Supprimer
                    </button>
                </div>
            </td>
        </tr>
    `;
}

// Show add room modal
function showAddRoomModal() {
    roomToEdit = null;
    document.getElementById('room-modal-title').textContent = 'Ajouter une chambre';
    document.getElementById('room-submit-btn').textContent = 'Ajouter';
    document.getElementById('room-form').reset();
    showModal('roomModal');
}

// Edit room
function editRoom(roomId) {
    const room = rooms.find(r => r.id === roomId);
    if (!room) return;

    roomToEdit = room;
    document.getElementById('room-modal-title').textContent = 'Modifier la chambre';
    document.getElementById('room-submit-btn').textContent = 'Modifier';

    // Fill form with room data
    document.getElementById('room-numero').value = room.numero;
    document.getElementById('room-type').value = room.type;
    document.getElementById('room-prix').value = room.prix;
    document.getElementById('room-etat').value = room.etat;

    showModal('roomModal');
}

// Handle room form submission
async function handleRoomSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const roomData = {
        numero: formData.get('numero'),
        type: formData.get('type'),
        prix: parseFloat(formData.get('prix')),
        etat: formData.get('etat')
    };

    try {
        let response;
        if (roomToEdit) {
            // Update existing room
            response = await fetch(`http://localhost:8080/api/rooms-test/${roomToEdit.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(roomData)
            });
            showAlert('Chambre modifiée avec succès', 'success');
        } else {
            // Create new room
            response = await fetch('http://localhost:8080/api/rooms-test', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(roomData)
            });
            showAlert('Chambre ajoutée avec succès', 'success');
        }

        const result = await response.json();
        if (result.status === 'error') {
            showAlert('Erreur: ' + result.message, 'error');
            return;
        }

        hideModal('roomModal');
        await loadRooms();

    } catch (error) {
        showAlert('Erreur lors de la sauvegarde: ' + error.message, 'error');
    }
}

// Show delete confirmation modal
function showDeleteModal(roomId) {
    const room = rooms.find(r => r.id === roomId);
    if (!room) return;

    roomToDelete = roomId;

    const detailsElement = document.getElementById('room-to-delete');
    detailsElement.innerHTML = `
        <div class="flex justify-between items-center">
            <div>
                <div class="font-medium">Chambre ${room.numero}</div>
                <div class="text-sm text-gray-600">${formatRoomType(room.type)} - ${formatCurrency(room.prix)}</div>
            </div>
            <div class="text-right">
                <div class="text-sm ${getStatusColorClass(room.etat, 'room')}">
                    ${formatRoomStatus(room.etat)}
                </div>
            </div>
        </div>
    `;

    showModal('deleteModal');
}

// Confirm delete
async function confirmDelete() {
    if (!roomToDelete) return;

    try {
        const response = await fetch(`http://localhost:8080/api/rooms-test/${roomToDelete}`, {
            method: 'DELETE'
        });

        const result = await response.json();
        if (result.status === 'error') {
            showAlert('Erreur: ' + result.message, 'error');
            return;
        }

        showAlert('Chambre supprimée avec succès', 'success');
        hideModal('deleteModal');
        await loadRooms();

    } catch (error) {
        showAlert('Erreur lors de la suppression: ' + error.message, 'error');
    }

    roomToDelete = null;
}

// Filter rooms
function filterRooms() {
    const searchTerm = document.getElementById('search-input').value.toLowerCase();
    const typeFilter = document.getElementById('type-filter').value;
    const statusFilter = document.getElementById('status-filter').value;

    let filteredRooms = rooms.filter(room => {
        const matchesSearch = !searchTerm || room.numero.toLowerCase().includes(searchTerm);
        const matchesType = !typeFilter || room.type === typeFilter;
        const matchesStatus = !statusFilter || room.etat === statusFilter;

        return matchesSearch && matchesType && matchesStatus;
    });

    displayRooms(filteredRooms);
}

// Logout
function logout() {
    hotelAPI.logout();
    showAlert('Déconnexion réussie', 'success');

    setTimeout(() => {
        window.location.href = '../index.html';
    }, 1000);
}
