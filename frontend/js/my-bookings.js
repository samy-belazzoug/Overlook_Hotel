// Overlook Hotel - My Bookings Page JavaScript

let currentReservations = [];
let reservationToCancel = null;

// Initialize page
document.addEventListener('DOMContentLoaded', function () {
    checkAuthentication();
    loadReservations();
});

// Check authentication
function checkAuthentication() {
    if (!isAuthenticated()) {
        showAlert('Veuillez vous connecter pour voir vos réservations', 'error');
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 2000);
        return;
    }

    // Update UI for authenticated user
    const authButtons = document.getElementById('auth-buttons');
    const userMenu = document.getElementById('user-menu');

    if (authButtons && userMenu) {
        authButtons.classList.add('hidden');
        userMenu.classList.remove('hidden');

        const userNameElement = document.getElementById('user-name');
        if (userNameElement) {
            const role = getUserRole();
            userNameElement.textContent = `Bienvenue, ${role}`;
        }
    }
}

// Load user reservations
async function loadReservations() {
    const loadingState = document.getElementById('loading-state');
    const noReservations = document.getElementById('no-reservations');
    const reservationsList = document.getElementById('reservations-list');

    try {
        // For now, we'll use a default client ID
        // In a real app, you'd get this from the authenticated user
        const clientId = 1; // This should come from the logged-in user

        const reservations = await hotelAPI.getMyReservations(clientId);

        loadingState.classList.add('hidden');

        if (reservations && reservations.length > 0) {
            currentReservations = reservations;
            displayReservations(reservations);
        } else {
            noReservations.classList.remove('hidden');
        }
    } catch (error) {
        loadingState.classList.add('hidden');
        showAlert('Erreur lors du chargement des réservations: ' + error.message, 'error');
    }
}

// Display reservations
function displayReservations(reservations) {
    const reservationsList = document.getElementById('reservations-list');

    reservationsList.innerHTML = reservations.map(reservation => createReservationCard(reservation)).join('');

    // Add animation to cards
    const cards = reservationsList.querySelectorAll('.reservation-card');
    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`;
        card.classList.add('slide-up');
    });
}

// Create reservation card HTML
function createReservationCard(reservation) {
    const statusClass = getStatusColorClass(reservation.statut, 'reservation');
    const canCancel = reservation.statut === 'active';

    return `
        <div class="reservation-card card">
            <div class="card-body">
                <div class="flex items-start justify-between mb-4">
                    <div>
                        <h3 class="text-xl font-semibold text-gray-800">
                            Chambre ${reservation.chambre.numero}
                        </h3>
                        <p class="text-gray-600">${formatRoomType(reservation.chambre.type)}</p>
                    </div>
                    <div class="text-right">
                        <div class="text-2xl font-bold text-primary">
                            ${formatCurrency(reservation.chambre.prix)}
                        </div>
                        <div class="text-sm text-gray-500">par nuit</div>
                    </div>
                </div>
                
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                    <div>
                        <div class="text-sm text-gray-500">Arrivée</div>
                        <div class="font-medium">${formatDate(reservation.dateDebut)}</div>
                    </div>
                    <div>
                        <div class="text-sm text-gray-500">Départ</div>
                        <div class="font-medium">${formatDate(reservation.dateFin)}</div>
                    </div>
                </div>
                
                <div class="flex items-center justify-between">
                    <div class="flex items-center">
                        <span class="text-sm text-gray-500 mr-2">Statut:</span>
                        <span class="${statusClass} font-medium">
                            ${formatReservationStatus(reservation.statut)}
                        </span>
                    </div>
                    
                    ${canCancel ? `
                        <button onclick="showCancelModal(${reservation.id})" 
                                class="btn btn-outline btn-sm text-red-600 border-red-600 hover:bg-red-600 hover:text-white">
                            Annuler
                        </button>
                    ` : `
                        <span class="text-sm text-gray-500">Non annulable</span>
                    `}
                </div>
                
                <div class="mt-4 pt-4 border-t border-gray-200">
                    <div class="text-sm text-gray-500">
                        Réservé le ${formatDate(reservation.dateCreation)}
                    </div>
                </div>
            </div>
        </div>
    `;
}

// Show cancel confirmation modal
function showCancelModal(reservationId) {
    const reservation = currentReservations.find(r => r.id === reservationId);
    if (!reservation) return;

    reservationToCancel = reservationId;

    const detailsElement = document.getElementById('reservation-details');
    detailsElement.innerHTML = `
        <div class="flex justify-between items-center">
            <div>
                <div class="font-medium">Chambre ${reservation.chambre.numero}</div>
                <div class="text-sm text-gray-600">${formatDate(reservation.dateDebut)} - ${formatDate(reservation.dateFin)}</div>
            </div>
            <div class="text-right">
                <div class="font-bold text-primary">${formatCurrency(reservation.chambre.prix)}</div>
                <div class="text-sm text-gray-600">par nuit</div>
            </div>
        </div>
    `;

    showModal('cancelModal');
}

// Confirm cancellation
async function confirmCancel() {
    if (!reservationToCancel) return;

    try {
        await hotelAPI.cancelReservation(reservationToCancel);

        showAlert('Réservation annulée avec succès', 'success');
        hideModal('cancelModal');

        // Reload reservations
        await loadReservations();

    } catch (error) {
        showAlert('Erreur lors de l\'annulation: ' + error.message, 'error');
    }

    reservationToCancel = null;
}

// Logout
function logout() {
    hotelAPI.logout();
    showAlert('Déconnexion réussie', 'success');

    setTimeout(() => {
        window.location.href = 'index.html';
    }, 1000);
}

// Refresh reservations
async function refreshReservations() {
    const loadingState = document.getElementById('loading-state');
    const noReservations = document.getElementById('no-reservations');
    const reservationsList = document.getElementById('reservations-list');

    // Show loading
    loadingState.classList.remove('hidden');
    noReservations.classList.add('hidden');
    reservationsList.innerHTML = '';

    await loadReservations();
}

// Add refresh button functionality
document.addEventListener('DOMContentLoaded', function () {
    // Add refresh button to page header if needed
    const pageHeader = document.querySelector('.text-center.mb-8');
    if (pageHeader) {
        const refreshButton = document.createElement('button');
        refreshButton.className = 'btn btn-ghost btn-sm mt-2';
        refreshButton.innerHTML = '🔄 Actualiser';
        refreshButton.onclick = refreshReservations;
        pageHeader.appendChild(refreshButton);
    }
});
