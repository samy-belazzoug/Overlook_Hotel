// Overlook Hotel - Admin Dashboard JavaScript

// Initialize page
document.addEventListener('DOMContentLoaded', function () {
    checkAuthentication();
    loadDashboardData();
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

// Load dashboard data
async function loadDashboardData() {
    try {
        await Promise.all([
            loadStats(),
            loadRecentReservations(),
            loadRoomStatus()
        ]);
    } catch (error) {
        showAlert('Erreur lors du chargement des données: ' + error.message, 'error');
    }
}

// Load statistics
async function loadStats() {
    try {
        const [rooms, reservations, users] = await Promise.all([
            hotelAPI.getAdminRooms(),
            hotelAPI.getAdminReservations(),
            hotelAPI.getAdminUsers()
        ]);

        // Update stats
        document.getElementById('total-rooms').textContent = rooms ? rooms.length : 0;
        document.getElementById('total-users').textContent = users ? users.length : 0;

        if (reservations) {
            const activeReservations = reservations.filter(r => r.statut === 'active').length;
            document.getElementById('active-reservations').textContent = activeReservations;

            // Calculate occupancy rate
            const totalRooms = rooms ? rooms.length : 1;
            const occupancyRate = Math.round((activeReservations / totalRooms) * 100);
            document.getElementById('occupancy-rate').textContent = occupancyRate + '%';
        }
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

// Load recent reservations
async function loadRecentReservations() {
    const container = document.getElementById('recent-reservations');

    try {
        const response = await fetch('http://localhost:8080/api/dashboard-test/recent-reservations');
        const data = await response.json();

        if (data.status === 'success' && data.reservations && data.reservations.length > 0) {
            container.innerHTML = data.reservations.map(reservation => `
                <div class="flex items-center justify-between py-3 border-b border-gray-200 last:border-b-0">
                    <div>
                        <div class="font-medium text-gray-800">
                            Chambre ${reservation.chambre ? reservation.chambre.numero : 'N/A'}
                        </div>
                        <div class="text-sm text-gray-600">
                            ${formatDate(reservation.dateDebut)} - ${formatDate(reservation.dateFin)}
                        </div>
                    </div>
                    <div class="text-right">
                        <div class="text-sm font-medium ${getStatusColorClass(reservation.statut, 'reservation')}">
                            ${formatReservationStatus(reservation.statut)}
                        </div>
                        <div class="text-sm text-gray-500">
                            ${reservation.chambre ? formatCurrency(reservation.chambre.prix) : 'N/A'}
                        </div>
                    </div>
                </div>
            `).join('');
        } else {
            container.innerHTML = `
                <div class="text-center py-4 text-gray-500">
                    Aucune réservation trouvée
                </div>
            `;
        }
    } catch (error) {
        container.innerHTML = `
            <div class="text-center py-4 text-red-600">
                Erreur lors du chargement
            </div>
        `;
    }
}

// Load room status
async function loadRoomStatus() {
    const container = document.getElementById('room-status');

    try {
        const response = await fetch('http://localhost:8080/api/dashboard-test/room-status');
        const data = await response.json();

        if (data.status === 'success' && data.statusCounts) {
            const statusMap = {
                'disponible': { label: 'Disponibles', color: 'text-secondary' },
                'occupee': { label: 'Occupées', color: 'text-red-600' },
                'nettoyage': { label: 'En nettoyage', color: 'text-yellow-600' },
                'maintenance': { label: 'Maintenance', color: 'text-gray-500' }
            };

            container.innerHTML = Object.entries(data.statusCounts).map(([status, count]) => {
                const statusInfo = statusMap[status] || { label: status, color: 'text-gray-600' };
                return `
                    <div class="flex items-center justify-between py-2">
                        <span class="text-gray-600">${statusInfo.label}</span>
                        <span class="font-medium ${statusInfo.color}">${count}</span>
                    </div>
                `;
            }).join('');
        } else {
            container.innerHTML = `
                <div class="text-center py-4 text-gray-500">
                    Aucune chambre trouvée
                </div>
            `;
        }
    } catch (error) {
        container.innerHTML = `
            <div class="text-center py-4 text-red-600">
                Erreur lors du chargement
            </div>
        `;
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

// Refresh dashboard data
async function refreshDashboard() {
    showAlert('Actualisation en cours...', 'info', 2000);
    await loadDashboardData();
}

// Add refresh functionality
document.addEventListener('DOMContentLoaded', function () {
    // Add refresh button to page header
    const pageHeader = document.querySelector('.mb-8');
    if (pageHeader) {
        const refreshButton = document.createElement('button');
        refreshButton.className = 'btn btn-ghost btn-sm mt-2';
        refreshButton.innerHTML = '🔄 Actualiser';
        refreshButton.onclick = refreshDashboard;
        pageHeader.appendChild(refreshButton);
    }
});
