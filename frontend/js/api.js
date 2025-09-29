// Overlook Hotel - API Client
class HotelAPI {
    constructor() {
        // Détecter automatiquement l'URL de base selon l'environnement
        const isLocalhost = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
        this.baseURL = isLocalhost ? 'http://localhost:8080/api' : '/api';
        this.token = localStorage.getItem('authToken');
    }

    // Set authentication token
    setToken(token) {
        this.token = token;
        localStorage.setItem('authToken', token);
    }

    // Clear authentication token
    clearToken() {
        this.token = null;
        localStorage.removeItem('authToken');
    }

    // Get headers for API requests
    getHeaders() {
        const headers = {
            'Content-Type': 'application/json',
        };

        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }

        return headers;
    }

    // Generic request method
    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        const config = {
            headers: this.getHeaders(),
            ...options,
        };

        try {
            const response = await fetch(url, config);

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || `HTTP ${response.status}: ${response.statusText}`);
            }

            // Handle empty responses
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            }

            return null;
        } catch (error) {
            console.error('API Request failed:', error);
            throw error;
        }
    }

    // Authentication
    async login(email, password) {
        const response = await this.request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email, password }),
        });

        if (response && response.token) {
            this.setToken(response.token);
        }

        return response;
    }

    async logout() {
        this.clearToken();
    }

    // Rooms
    async getRooms() {
        return await this.request('/rooms');
    }

    async searchRooms(filters = {}) {
        const params = new URLSearchParams();

        if (filters.checkIn) params.append('checkIn', filters.checkIn);
        if (filters.checkOut) params.append('checkOut', filters.checkOut);
        if (filters.minPrice) params.append('minPrice', filters.minPrice);
        if (filters.maxPrice) params.append('maxPrice', filters.maxPrice);
        if (filters.type) params.append('type', filters.type);

        const queryString = params.toString();
        const endpoint = queryString ? `/rooms/search?${queryString}` : '/rooms';

        return await this.request(endpoint);
    }

    async getRoom(id) {
        return await this.request(`/rooms/${id}`);
    }

    // Reservations
    async createReservation(reservationData) {
        return await this.request('/reservations', {
            method: 'POST',
            body: JSON.stringify(reservationData),
        });
    }

    async getMyReservations(clientId) {
        return await this.request(`/reservations/my-reservations?clientId=${clientId}`);
    }

    async cancelReservation(id) {
        return await this.request(`/reservations/${id}/cancel`, {
            method: 'PUT',
        });
    }

    // Admin functions
    async getAdminRooms() {
        return await this.request('/admin/rooms');
    }

    async createRoom(roomData) {
        return await this.request('/admin/rooms', {
            method: 'POST',
            body: JSON.stringify(roomData),
        });
    }

    async updateRoom(id, roomData) {
        return await this.request(`/admin/rooms/${id}`, {
            method: 'PUT',
            body: JSON.stringify(roomData),
        });
    }

    async deleteRoom(id) {
        return await this.request(`/admin/rooms/${id}`, {
            method: 'DELETE',
        });
    }

    async getAdminUsers() {
        return await this.request('/admin/users');
    }

    async createUser(userData) {
        return await this.request('/admin/users', {
            method: 'POST',
            body: JSON.stringify(userData),
        });
    }

    async updateUser(id, userData) {
        return await this.request(`/admin/users/${id}`, {
            method: 'PUT',
            body: JSON.stringify(userData),
        });
    }

    async deleteUser(id) {
        return await this.request(`/admin/users/${id}`, {
            method: 'DELETE',
        });
    }

    async getAdminReservations() {
        return await this.request('/admin/reservations');
    }

    async updateReservationStatus(id, status) {
        return await this.request(`/admin/reservations/${id}/status?status=${status}`, {
            method: 'PUT',
        });
    }

    // Employee functions
    async getMyShifts(employeeId) {
        return await this.request(`/employee/shifts?employeeId=${employeeId}`);
    }

    async getShiftsByDate(date) {
        return await this.request(`/employee/shifts/date?date=${date}`);
    }

    // Admin shift management
    async getAdminShifts() {
        return await this.request('/admin/shifts');
    }

    async createShift(shiftData) {
        return await this.request('/admin/shifts', {
            method: 'POST',
            body: JSON.stringify(shiftData),
        });
    }

    async updateShift(id, shiftData) {
        return await this.request(`/admin/shifts/${id}`, {
            method: 'PUT',
            body: JSON.stringify(shiftData),
        });
    }

    async deleteShift(id) {
        return await this.request(`/admin/shifts/${id}`, {
            method: 'DELETE',
        });
    }
}

// Create global API instance
window.hotelAPI = new HotelAPI();