// Overlook Hotel - Utility Functions

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('fr-FR', {
        style: 'currency',
        currency: 'EUR',
    }).format(amount);
}

// Format date
function formatDate(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('fr-FR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
    }).format(date);
}

// Format date for input fields
function formatDateForInput(dateString) {
    const date = new Date(dateString);
    return date.toISOString().split('T')[0];
}

// Get today's date in YYYY-MM-DD format
function getTodayDate() {
    return new Date().toISOString().split('T')[0];
}

// Get date N days from today
function getDateFromToday(days) {
    const date = new Date();
    date.setDate(date.getDate() + days);
    return date.toISOString().split('T')[0];
}

// Validate email
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Validate date range
function isValidDateRange(startDate, endDate) {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    return start >= today && end > start;
}

// Show loading state
function showLoading(element) {
    if (element) {
        element.innerHTML = '<div class="spinner"></div>';
        element.disabled = true;
    }
}

// Hide loading state
function hideLoading(element, originalText) {
    if (element) {
        element.innerHTML = originalText;
        element.disabled = false;
    }
}

// Show alert message
function showAlert(message, type = 'info', duration = 5000) {
    const alertContainer = document.getElementById('alert-container') || createAlertContainer();

    const alert = document.createElement('div');
    alert.className = `alert alert-${type} fade-in`;
    alert.innerHTML = `
        <div class="flex items-center justify-between">
            <span>${message}</span>
            <button onclick="this.parentElement.parentElement.remove()" class="ml-4 text-lg">&times;</button>
        </div>
    `;

    alertContainer.appendChild(alert);

    // Auto remove after duration
    if (duration > 0) {
        setTimeout(() => {
            if (alert.parentElement) {
                alert.remove();
            }
        }, duration);
    }
}

// Create alert container if it doesn't exist
function createAlertContainer() {
    const container = document.createElement('div');
    container.id = 'alert-container';
    container.className = 'fixed top-4 right-4 z-50 space-y-2';
    document.body.appendChild(container);
    return container;
}

// Show modal
function showModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('active');
        document.body.style.overflow = 'hidden';
    }
}

// Hide modal
function hideModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('active');
        document.body.style.overflow = '';
    }
}

// Close modal when clicking outside
function setupModalCloseOnOutsideClick(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                hideModal(modalId);
            }
        });
    }
}

// Debounce function
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Throttle function
function throttle(func, limit) {
    let inThrottle;
    return function () {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

// Get URL parameters
function getUrlParams() {
    const params = new URLSearchParams(window.location.search);
    const result = {};
    for (const [key, value] of params) {
        result[key] = value;
    }
    return result;
}

// Set URL parameters
function setUrlParams(params) {
    const url = new URL(window.location);
    Object.keys(params).forEach(key => {
        if (params[key]) {
            url.searchParams.set(key, params[key]);
        } else {
            url.searchParams.delete(key);
        }
    });
    window.history.pushState({}, '', url);
}

// Check if user is authenticated
function isAuthenticated() {
    return !!localStorage.getItem('authToken');
}

// Get user role from token (simple implementation)
function getUserRole() {
    const token = localStorage.getItem('authToken');
    if (!token) return null;

    try {
        // Simple JWT decode (in production, use a proper JWT library)
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.role;
    } catch (error) {
        console.error('Error decoding token:', error);
        return null;
    }
}

// Redirect to login if not authenticated
function requireAuth() {
    if (!isAuthenticated()) {
        window.location.href = '/login.html';
        return false;
    }
    return true;
}

// Redirect based on user role
function redirectByRole() {
    const role = getUserRole();
    switch (role) {
        case 'ADMIN':
            window.location.href = '/admin/dashboard.html';
            break;
        case 'EMPLOYE':
            window.location.href = '/employee/shifts.html';
            break;
        case 'CLIENT':
        default:
            window.location.href = '/index.html';
            break;
    }
}

// Format room type for display
function formatRoomType(type) {
    const typeMap = {
        'simple': 'Simple',
        'double': 'Double',
        'suite': 'Suite',
        'SIMPLE': 'Simple',
        'DOUBLE': 'Double',
        'SUITE': 'Suite'
    };
    return typeMap[type] || type;
}

// Format room status for display
function formatRoomStatus(status) {
    const statusMap = {
        'disponible': 'Disponible',
        'occupee': 'Occupée',
        'nettoyage': 'En nettoyage',
        'maintenance': 'Maintenance'
    };
    return statusMap[status] || status;
}

// Format reservation status for display
function formatReservationStatus(status) {
    const statusMap = {
        'active': 'Active',
        'annulee': 'Annulée',
        'terminee': 'Terminée',
        'completed': 'Terminée',
        'cancelled': 'Annulée'
    };
    return statusMap[status] || status;
}

// Get status color class
function getStatusColorClass(status, type = 'reservation') {
    if (type === 'reservation') {
        switch (status) {
            case 'active':
                return 'text-secondary';
            case 'annulee':
            case 'cancelled':
                return 'text-red-600';
            case 'terminee':
            case 'completed':
                return 'text-gray-500';
            default:
                return 'text-gray-600';
        }
    } else if (type === 'room') {
        switch (status) {
            case 'disponible':
                return 'text-secondary';
            case 'occupee':
                return 'text-red-600';
            case 'nettoyage':
                return 'text-yellow-600';
            case 'maintenance':
                return 'text-gray-500';
            default:
                return 'text-gray-600';
        }
    }
    return 'text-gray-600';
}

// Generate random ID (for temporary elements)
function generateId() {
    return Math.random().toString(36).substr(2, 9);
}

// Copy text to clipboard
async function copyToClipboard(text) {
    try {
        await navigator.clipboard.writeText(text);
        showAlert('Copié dans le presse-papiers', 'success', 2000);
    } catch (error) {
        console.error('Failed to copy text: ', error);
        showAlert('Erreur lors de la copie', 'error');
    }
}

// Smooth scroll to element
function smoothScrollTo(elementId) {
    const element = document.getElementById(elementId);
    if (element) {
        element.scrollIntoView({
            behavior: 'smooth',
            block: 'start'
        });
    }
}

// Check if element is in viewport
function isInViewport(element) {
    const rect = element.getBoundingClientRect();
    return (
        rect.top >= 0 &&
        rect.left >= 0 &&
        rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
        rect.right <= (window.innerWidth || document.documentElement.clientWidth)
    );
}

// Lazy load images
function setupLazyLoading() {
    const images = document.querySelectorAll('img[data-src]');
    const imageObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                img.classList.remove('opacity-0');
                img.classList.add('opacity-100');
                observer.unobserve(img);
            }
        });
    });

    images.forEach(img => imageObserver.observe(img));
}

// Initialize common functionality
function initializeApp() {
    // Setup lazy loading
    setupLazyLoading();

    // Setup modal close on outside click
    document.querySelectorAll('.modal').forEach(modal => {
        setupModalCloseOnOutsideClick(modal.id);
    });

    // Setup form validation
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', handleFormSubmit);
    });
}

// Handle form submission
function handleFormSubmit(event) {
    const form = event.target;
    const submitBtn = form.querySelector('button[type="submit"]');

    if (submitBtn) {
        const originalText = submitBtn.innerHTML;
        showLoading(submitBtn);

        // Re-enable button after 3 seconds as fallback
        setTimeout(() => {
            hideLoading(submitBtn, originalText);
        }, 3000);
    }
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', initializeApp);
