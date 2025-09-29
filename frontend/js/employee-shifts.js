// Overlook Hotel - Employee Shifts JavaScript

let shifts = [];
let employeeId = null;

// Initialize page
document.addEventListener('DOMContentLoaded', function () {
    checkAuthentication();
    loadShifts();
    setupEventListeners();
});

// Check authentication
function checkAuthentication() {
    if (!isAuthenticated()) {
        showAlert('Veuillez vous connecter pour voir vos plannings', 'error');
        setTimeout(() => {
            window.location.href = '../index.html';
        }, 2000);
        return;
    }

    const role = getUserRole();
    if (role !== 'EMPLOYE') {
        showAlert('Accès refusé. Seuls les employés peuvent accéder à cette page.', 'error');
        setTimeout(() => {
            window.location.href = '../index.html';
        }, 2000);
        return;
    }

    // Update UI for authenticated user
    const userMenu = document.getElementById('user-menu');
    if (userMenu) {
        userMenu.classList.remove('hidden');

        const userNameElement = document.getElementById('user-name');
        if (userNameElement) {
            userNameElement.textContent = `Bienvenue, Employé`;
        }
    }

    // Set employee ID (in a real app, this would come from the user profile)
    employeeId = 3; // This should come from the logged-in user
}

// Setup event listeners
function setupEventListeners() {
    // Filter inputs
    document.getElementById('date-filter').addEventListener('change', filterShifts);
    document.getElementById('shift-filter').addEventListener('change', filterShifts);
}

// Load employee shifts
async function loadShifts() {
    const loadingState = document.getElementById('loading-state');
    const noShifts = document.getElementById('no-shifts');
    const shiftsList = document.getElementById('shifts-list');

    try {
        loadingState.classList.remove('hidden');

        if (!employeeId) {
            throw new Error('ID employé non trouvé');
        }

        shifts = await hotelAPI.getMyShifts(employeeId);

        loadingState.classList.add('hidden');

        if (shifts && shifts.length > 0) {
            displayShifts(shifts);
        } else {
            noShifts.classList.remove('hidden');
        }
    } catch (error) {
        loadingState.classList.add('hidden');
        showAlert('Erreur lors du chargement des plannings: ' + error.message, 'error');
    }
}

// Display shifts
function displayShifts(shiftsToShow) {
    const shiftsList = document.getElementById('shifts-list');

    if (shiftsToShow.length === 0) {
        shiftsList.innerHTML = `
            <div class="text-center py-8 text-gray-500">
                Aucun planning trouvé pour les critères sélectionnés
            </div>
        `;
        return;
    }

    // Sort shifts by date
    const sortedShifts = shiftsToShow.sort((a, b) => new Date(a.date) - new Date(b.date));

    shiftsList.innerHTML = sortedShifts.map(shift => createShiftCard(shift)).join('');

    // Add animation to cards
    const cards = shiftsList.querySelectorAll('.shift-card');
    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`;
        card.classList.add('slide-up');
    });
}

// Create shift card HTML
function createShiftCard(shift) {
    const shiftEmoji = getShiftEmoji(shift.shift);
    const shiftColor = getShiftColor(shift.shift);
    const isToday = isTodayDate(shift.date);
    const isPast = new Date(shift.date) < new Date();

    return `
        <div class="shift-card card ${isToday ? 'ring-2 ring-primary' : ''} ${isPast ? 'opacity-75' : ''}">
            <div class="card-body">
                <div class="flex items-center justify-between">
                    <div class="flex items-center space-x-4">
                        <div class="text-3xl">${shiftEmoji}</div>
                        <div>
                            <h3 class="text-lg font-semibold text-gray-800">
                                ${formatDate(shift.date)}
                            </h3>
                            <p class="text-gray-600">${formatShift(shift.shift)}</p>
                        </div>
                    </div>
                    <div class="text-right">
                        <div class="text-sm text-gray-500">
                            ${isToday ? 'Aujourd\'hui' : isPast ? 'Passé' : 'À venir'}
                        </div>
                        <div class="text-sm font-medium ${shiftColor}">
                            ${formatShift(shift.shift)}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
}

// Get shift emoji
function getShiftEmoji(shift) {
    const emojiMap = {
        'matin': '🌅',
        'soir': '🌆',
        'nuit': '🌙',
        'morning': '🌅',
        'evening': '🌆',
        'night': '🌙'
    };
    return emojiMap[shift] || '⏰';
}

// Get shift color
function getShiftColor(shift) {
    const colorMap = {
        'matin': 'text-yellow-600',
        'soir': 'text-orange-600',
        'nuit': 'text-blue-600',
        'morning': 'text-yellow-600',
        'evening': 'text-orange-600',
        'night': 'text-blue-600'
    };
    return colorMap[shift] || 'text-gray-600';
}

// Format shift
function formatShift(shift) {
    const shiftMap = {
        'matin': 'Matin',
        'soir': 'Soir',
        'nuit': 'Nuit',
        'morning': 'Matin',
        'evening': 'Soir',
        'night': 'Nuit'
    };
    return shiftMap[shift] || shift;
}

// Check if date is today
function isTodayDate(dateString) {
    const today = new Date();
    const date = new Date(dateString);

    return today.toDateString() === date.toDateString();
}

// Filter shifts
function filterShifts() {
    const dateFilter = document.getElementById('date-filter').value;
    const shiftFilter = document.getElementById('shift-filter').value;

    let filteredShifts = shifts.filter(shift => {
        const matchesDate = !dateFilter || shift.date === dateFilter;
        const matchesShift = !shiftFilter || shift.shift === shiftFilter;

        return matchesDate && matchesShift;
    });

    displayShifts(filteredShifts);
}

// Load shifts by date
async function loadShiftsByDate(date) {
    const loadingState = document.getElementById('loading-state');
    const shiftsList = document.getElementById('shifts-list');

    try {
        loadingState.classList.remove('hidden');

        const shifts = await hotelAPI.getShiftsByDate(date);

        loadingState.classList.add('hidden');
        displayShifts(shifts);

    } catch (error) {
        loadingState.classList.add('hidden');
        showAlert('Erreur lors du chargement des plannings: ' + error.message, 'error');
    }
}

// Logout
function logout() {
    hotelAPI.logout();
    showAlert('Déconnexion réussie', 'success');

    setTimeout(() => {
        window.location.href = '../index.html';
    }, 1000);
}

// Refresh shifts
async function refreshShifts() {
    showAlert('Actualisation en cours...', 'info', 2000);
    await loadShifts();
}

// Add refresh button functionality
document.addEventListener('DOMContentLoaded', function () {
    // Add refresh button to page header
    const pageHeader = document.querySelector('.text-center.mb-8');
    if (pageHeader) {
        const refreshButton = document.createElement('button');
        refreshButton.className = 'btn btn-ghost btn-sm mt-2';
        refreshButton.innerHTML = '🔄 Actualiser';
        refreshButton.onclick = refreshShifts;
        pageHeader.appendChild(refreshButton);
    }
});
