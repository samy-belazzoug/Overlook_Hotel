// Overlook Hotel - Index Page JavaScript

// Initialize page
document.addEventListener('DOMContentLoaded', function () {
    initializePage();
    setupEventListeners();
    checkAuthentication();
    setDefaultDates();
});

// Initialize page
function initializePage() {
    // Set minimum dates for date inputs
    const today = getTodayDate();
    const tomorrow = getDateFromToday(1);

    document.getElementById('check-in').min = today;
    document.getElementById('check-out').min = today;

    // Set default dates
    document.getElementById('check-in').value = today;
    document.getElementById('check-out').value = tomorrow;
}

// Setup event listeners
function setupEventListeners() {
    // Search form
    document.getElementById('search-form').addEventListener('submit', handleSearch);

    // Login form
    document.getElementById('login-form').addEventListener('submit', handleLogin);

    // Register form
    document.getElementById('register-form').addEventListener('submit', handleRegister);

    // Date validation
    document.getElementById('check-in').addEventListener('change', validateDateRange);
    document.getElementById('check-out').addEventListener('change', validateDateRange);
}

// Check authentication status
function checkAuthentication() {
    if (isAuthenticated()) {
        const role = getUserRole();
        const authButtons = document.getElementById('auth-buttons');
        const userMenu = document.getElementById('user-menu');

        if (authButtons && userMenu) {
            authButtons.classList.add('hidden');
            userMenu.classList.remove('hidden');

            // Set user name (you might want to get this from the token or API)
            const userNameElement = document.getElementById('user-name');
            if (userNameElement) {
                userNameElement.textContent = `Bienvenue, ${role}`;
            }
        }
    }
}

// Set default dates
function setDefaultDates() {
    const checkInInput = document.getElementById('check-in');
    const checkOutInput = document.getElementById('check-out');

    if (!checkInInput.value) {
        checkInInput.value = getTodayDate();
    }

    if (!checkOutInput.value) {
        checkOutInput.value = getDateFromToday(1);
    }
}

// Handle search form submission
async function handleSearch(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const filters = {
        checkIn: formData.get('checkIn'),
        checkOut: formData.get('checkOut'),
        type: formData.get('type'),
        maxPrice: formData.get('maxPrice')
    };

    // Validate dates
    if (!isValidDateRange(filters.checkIn, filters.checkOut)) {
        showAlert('Veuillez sélectionner des dates valides', 'error');
        return;
    }

    await searchRooms(filters);
}

// Search rooms
async function searchRooms(filters) {
    const loadingState = document.getElementById('loading-state');
    const noResults = document.getElementById('no-results');
    const roomsGrid = document.getElementById('rooms-grid');

    // Show loading
    loadingState.classList.remove('hidden');
    noResults.classList.add('hidden');
    roomsGrid.innerHTML = '';

    try {
        const rooms = await hotelAPI.searchRooms(filters);

        // Hide loading
        loadingState.classList.add('hidden');

        if (rooms && rooms.length > 0) {
            displayRooms(rooms);
        } else {
            noResults.classList.remove('hidden');
        }
    } catch (error) {
        loadingState.classList.add('hidden');
        showAlert('Erreur lors de la recherche: ' + error.message, 'error');
    }
}

// Display rooms in grid
function displayRooms(rooms) {
    const roomsGrid = document.getElementById('rooms-grid');

    roomsGrid.innerHTML = rooms.map(room => createRoomCard(room)).join('');

    // Add animation to cards
    const cards = roomsGrid.querySelectorAll('.room-card');
    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`;
        card.classList.add('slide-up');
    });
}

// Create room card HTML
function createRoomCard(room) {
    const isAvailable = room.etat === 'disponible';
    const statusClass = isAvailable ? 'available' : 'occupied';
    const statusText = isAvailable ? 'Disponible' : 'Occupée';

    return `
        <div class="room-card card">
            <div class="room-card-image">
                🏨
            </div>
            <div class="room-card-status ${statusClass}">
                ${statusText}
            </div>
            <div class="room-card-content">
                <h3 class="room-card-title">Chambre ${room.numero}</h3>
                <div class="room-card-price">${formatCurrency(room.prix)}</div>
                <div class="room-card-type">${formatRoomType(room.type)}</div>
                ${isAvailable ? `
                    <button onclick="bookRoom(${room.id})" class="btn btn-primary w-full mt-4">
                        Réserver
                    </button>
                ` : `
                    <button disabled class="btn btn-ghost w-full mt-4">
                        Indisponible
                    </button>
                `}
            </div>
        </div>
    `;
}

// Book room
async function bookRoom(roomId) {
    if (!isAuthenticated()) {
        showModal('loginModal');
        return;
    }

    // Get current search dates
    const checkIn = document.getElementById('check-in').value;
    const checkOut = document.getElementById('check-out').value;

    if (!checkIn || !checkOut) {
        showAlert('Veuillez sélectionner des dates de séjour', 'error');
        return;
    }

    try {
        // For now, we'll use a default client ID
        // In a real app, you'd get this from the authenticated user
        const clientId = 1; // This should come from the logged-in user

        const reservationData = {
            clientId: clientId,
            roomId: roomId,
            checkIn: checkIn,
            checkOut: checkOut
        };

        const reservation = await hotelAPI.createReservation(reservationData);

        showAlert('Réservation créée avec succès!', 'success');

        // Optionally redirect to bookings page
        setTimeout(() => {
            window.location.href = 'my-bookings.html';
        }, 2000);

    } catch (error) {
        showAlert('Erreur lors de la réservation: ' + error.message, 'error');
    }
}

// Handle login
async function handleLogin(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const email = formData.get('email');
    const password = formData.get('password');

    if (!isValidEmail(email)) {
        showAlert('Veuillez entrer un email valide', 'error');
        return;
    }

    try {
        const response = await hotelAPI.login(email, password);

        if (response && response.token) {
            showAlert('Connexion réussie!', 'success');
            hideModal('loginModal');

            // Refresh the page to update UI
            setTimeout(() => {
                window.location.reload();
            }, 1000);
        }
    } catch (error) {
        showAlert('Erreur de connexion: ' + error.message, 'error');
    }
}

// Handle register
async function handleRegister(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const email = formData.get('email');
    const password = formData.get('password');
    const username = formData.get('username');

    if (!isValidEmail(email)) {
        showAlert('Veuillez entrer un email valide', 'error');
        return;
    }

    if (password.length < 6) {
        showAlert('Le mot de passe doit contenir au moins 6 caractères', 'error');
        return;
    }

    try {
        // Note: You'll need to implement a register endpoint in your backend
        showAlert('Fonctionnalité d\'inscription en cours de développement', 'info');
        hideModal('registerModal');
    } catch (error) {
        showAlert('Erreur lors de l\'inscription: ' + error.message, 'error');
    }
}

// Logout
function logout() {
    hotelAPI.logout();
    showAlert('Déconnexion réussie', 'success');

    // Refresh the page to update UI
    setTimeout(() => {
        window.location.reload();
    }, 1000);
}

// Validate date range
function validateDateRange() {
    const checkIn = document.getElementById('check-in').value;
    const checkOut = document.getElementById('check-out').value;

    if (checkIn && checkOut) {
        if (!isValidDateRange(checkIn, checkOut)) {
            showAlert('La date de départ doit être après la date d\'arrivée', 'error');
            document.getElementById('check-out').value = '';
        }
    }
}

// Reset search
function resetSearch() {
    document.getElementById('search-form').reset();
    setDefaultDates();

    const noResults = document.getElementById('no-results');
    const roomsGrid = document.getElementById('rooms-grid');

    noResults.classList.add('hidden');
    roomsGrid.innerHTML = '';
}

// Smooth scroll to search section
function smoothScrollToSearch() {
    smoothScrollTo('search');
}

// Initialize search on page load if URL has search parameters
function initializeSearchFromURL() {
    const params = getUrlParams();

    if (params.checkIn || params.checkOut || params.type || params.maxPrice) {
        // Set form values from URL parameters
        if (params.checkIn) document.getElementById('check-in').value = params.checkIn;
        if (params.checkOut) document.getElementById('check-out').value = params.checkOut;
        if (params.type) document.getElementById('room-type').value = params.type;
        if (params.maxPrice) document.getElementById('max-price').value = params.maxPrice;

        // Trigger search
        const filters = {
            checkIn: params.checkIn,
            checkOut: params.checkOut,
            type: params.type,
            maxPrice: params.maxPrice
        };

        searchRooms(filters);
    }
}

// Call initialization
initializeSearchFromURL();
