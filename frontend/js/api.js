// =============================================
// INVENTORY MANAGEMENT SYSTEM - API UTILITY
// =============================================

// Auto-detect: localhost → direct to :8080, production (Vercel) → relative /api (proxied)
const API_BASE_URL = window.API_BASE_URL ||
    (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
        ? 'http://localhost:8080/api'
        : '/api');

// =============================================
// LOCAL STORAGE HELPERS
// =============================================

const Storage = {
    setToken: (token) => localStorage.setItem('token', token),
    getToken: () => localStorage.getItem('token'),
    removeToken: () => localStorage.removeItem('token'),
    setUser: (user) => localStorage.setItem('user', JSON.stringify(user)),
    getUser: () => JSON.parse(localStorage.getItem('user') || 'null'),
    removeUser: () => localStorage.removeItem('user'),
    clear: () => localStorage.clear(),
    clearAuth: () => { localStorage.removeItem('token'); localStorage.removeItem('user'); }
};

// =============================================
// API CLIENT
// =============================================

class ApiClient {
    static async request(endpoint, options = {}) {
        const token = Storage.getToken();
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                ...options,
                headers
            });

            // Handle 401 Unauthorized - redirect to login
            if (response.status === 401) {
                Storage.clearAuth();
                window.location.href = 'login.html';
                return { success: false, message: 'Session expired. Please log in again.', data: null };
            }

            // Handle 403 Forbidden
            if (response.status === 403) {
                redirectToError('403', 'Access Denied', 'You do not have permission to perform this action.');
                return { success: false, message: 'Access denied', data: null };
            }

            // Handle 5xx server errors
            if (response.status >= 500) {
                redirectToError(String(response.status), 'Server Error', 'The server encountered an error. Please try again later.');
                return { success: false, message: 'Server error', data: null };
            }

            // Try to parse JSON body
            let data;
            const contentType = response.headers.get('content-type') || '';
            if (contentType.includes('application/json')) {
                data = await response.json();
            } else {
                const text = await response.text();
                data = text ? { success: false, message: text, data: null } : { success: false, message: 'Empty response', data: null };
            }

            // If response is not OK and data doesn't already have success field, wrap it
            if (!response.ok && data.success === undefined) {
                return { success: false, message: data.message || 'Request failed', data: null };
            }

            return data;
        } catch (error) {
            console.error('API Error:', error);
            return { success: false, message: error.message || 'Network error', data: null };
        }
    }

    static get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    }

    static post(endpoint, data) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    static put(endpoint, data) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }

    static patch(endpoint, data) {
        return this.request(endpoint, {
            method: 'PATCH',
            body: JSON.stringify(data)
        });
    }

    static delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }
}

// =============================================
// AUTHENTICATION API
// =============================================

const AuthAPI = {
    login: (credentials) => ApiClient.post('/auth/login', credentials),
    register: (userData) => ApiClient.post('/auth/register', userData),
    getCurrentUser: () => ApiClient.get('/auth/me'),
    logout: () => {
        Storage.clear();
        window.location.href = 'index.html';
    }
};

// =============================================
// DASHBOARD API
// =============================================

const DashboardAPI = {
    getStats: () => ApiClient.get('/dashboard/stats'),
    getLowStock: () => ApiClient.get('/dashboard/low-stock'),
    getCategoryDistribution: () => ApiClient.get('/dashboard/category-distribution')
};

// =============================================
// PRODUCTS API
// =============================================

const ProductsAPI = {
    getAll: () => ApiClient.get('/products'),
    getById: (id) => ApiClient.get(`/products/${id}`),
    search: (name) => ApiClient.get(`/products/search?name=${name}`),
    getByCategory: (categoryId) => ApiClient.get(`/products/category/${categoryId}`),
    getBySupplier: (supplierId) => ApiClient.get(`/products/supplier/${supplierId}`),
    getLowStock: () => ApiClient.get('/products/low-stock'),
    create: (product) => ApiClient.post('/products', product),
    update: (id, product) => ApiClient.put(`/products/${id}`, product),
    updateStock: (id, quantity, notes) => 
        ApiClient.patch(`/products/${id}/stock?quantity=${quantity}&notes=${notes || ''}`),
    delete: (id) => ApiClient.delete(`/products/${id}`),
    getTotalValue: () => ApiClient.get('/products/inventory-value')
};

// =============================================
// CATEGORIES API
// =============================================

const CategoriesAPI = {
    getAll: () => ApiClient.get('/categories'),
    getById: (id) => ApiClient.get(`/categories/${id}`),
    search: (name) => ApiClient.get(`/categories/search?name=${name}`),
    create: (category) => ApiClient.post('/categories', category),
    update: (id, category) => ApiClient.put(`/categories/${id}`, category),
    delete: (id) => ApiClient.delete(`/categories/${id}`)
};

// =============================================
// SUPPLIERS API
// =============================================

const SuppliersAPI = {
    getAll: () => ApiClient.get('/suppliers'),
    getById: (id) => ApiClient.get(`/suppliers/${id}`),
    search: (name) => ApiClient.get(`/suppliers/search?name=${name}`),
    create: (supplier) => ApiClient.post('/suppliers', supplier),
    update: (id, supplier) => ApiClient.put(`/suppliers/${id}`, supplier),
    delete: (id) => ApiClient.delete(`/suppliers/${id}`)
};

// =============================================
// TRANSACTIONS API
// =============================================

const TransactionsAPI = {
    getAll: () => ApiClient.get('/transactions'),
    getById: (id) => ApiClient.get(`/transactions/${id}`),
    getByProduct: (productId) => ApiClient.get(`/transactions/product/${productId}`),
    getByUser: (userId) => ApiClient.get(`/transactions/user/${userId}`),
    getByType: (type) => ApiClient.get(`/transactions/type/${type}`),
    getRecent: () => ApiClient.get('/transactions/recent'),
    getByDateRange: (startDate, endDate) => 
        ApiClient.get(`/transactions/date-range?startDate=${startDate}&endDate=${endDate}`),
    // Stock In — increases product quantity
    stockIn: (data) => ApiClient.post('/transactions/stock-in', data),
    // Issue / Order — decreases product quantity (prevents negative stock)
    order: (data) => ApiClient.post('/transactions/order', data)
};

// =============================================
// UI UTILITIES
// =============================================

const UI = {
    showLoading: () => {
        document.getElementById('loadingOverlay')?.classList.add('active');
    },

    hideLoading: () => {
        document.getElementById('loadingOverlay')?.classList.remove('active');
    },

    showAlert: (message, type = 'info') => {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type}`;
        alertDiv.textContent = message;
        alertDiv.style.position = 'fixed';
        alertDiv.style.top = '20px';
        alertDiv.style.right = '20px';
        alertDiv.style.zIndex = '9999';
        alertDiv.style.minWidth = '300px';
        
        document.body.appendChild(alertDiv);

        setTimeout(() => {
            alertDiv.remove();
        }, 5000);
    },

    showSuccess: (message) => UI.showAlert(message, 'success'),
    showError: (message) => UI.showAlert(message, 'error'),
    showWarning: (message) => UI.showAlert(message, 'warning'),
    showInfo: (message) => UI.showAlert(message, 'info'),

    confirmDelete: (message = 'Are you sure you want to delete this item?') => {
        return confirm(message);
    },

    formatCurrency: (amount) => {
        return new Intl.NumberFormat('en-IN', {
            style: 'currency',
            currency: 'INR',
            maximumFractionDigits: 0
        }).format(amount);
    },

    formatDate: (date) => {
        return new Date(date).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    },

    formatDateTime: (date) => {
        return new Date(date).toLocaleString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    }
};

// =============================================
// AUTH GUARD
// =============================================

const PUBLIC_PAGES = ['login.html', 'index.html', 'error.html'];

const AuthGuardHelper = {
    check: () => {
        const token = Storage.getToken();
        const isPublic = PUBLIC_PAGES.some(p => window.location.pathname.includes(p));
        if (!token && !isPublic) {
            window.location.href = 'index.html';
            return false;
        }
        return true;
    },

    getUserInfo: () => {
        const user = Storage.getUser();
        if (user) {
            const displayElements = document.querySelectorAll('.user-name');
            displayElements.forEach(el => el.textContent = user.username);
            
            const avatarElements = document.querySelectorAll('.user-avatar');
            avatarElements.forEach(el => {
                el.textContent = user.username.charAt(0).toUpperCase();
            });
        }
    }
};

// =============================================
// TABLE UTILITIES
// =============================================

const TableUtils = {
    createRow: (data, columns) => {
        const tr = document.createElement('tr');
        columns.forEach(col => {
            const td = document.createElement('td');
            if (col.render) {
                td.innerHTML = col.render(data);
            } else {
                td.textContent = data[col.field] || '-';
            }
            tr.appendChild(td);
        });
        return tr;
    },

    populateTable: (tableId, data, columns) => {
        const tbody = document.querySelector(`#${tableId} tbody`);
        tbody.innerHTML = '';
        
        if (data.length === 0) {
            const tr = document.createElement('tr');
            tr.innerHTML = `<td colspan="${columns.length}" class="text-center">No data available</td>`;
            tbody.appendChild(tr);
            return;
        }

        data.forEach(item => {
            tbody.appendChild(TableUtils.createRow(item, columns));
        });
    }
};

// =============================================
// FORM UTILITIES
// =============================================

const FormUtils = {
    getFormData: (formId) => {
        const form = document.getElementById(formId);
        const formData = new FormData(form);
        const data = {};
        formData.forEach((value, key) => {
            data[key] = value;
        });
        return data;
    },

    resetForm: (formId) => {
        document.getElementById(formId)?.reset();
    },

    fillForm: (formId, data) => {
        const form = document.getElementById(formId);
        Object.keys(data).forEach(key => {
            const input = form.querySelector(`[name="${key}"]`);
            if (input) {
                input.value = data[key] || '';
            }
        });
    }
};

// =============================================
// MODAL UTILITIES
// =============================================

const ModalUtils = {
    show: (modalId) => {
        document.getElementById(modalId)?.classList.add('active');
    },

    hide: (modalId) => {
        document.getElementById(modalId)?.classList.remove('active');
    },

    toggle: (modalId) => {
        document.getElementById(modalId)?.classList.toggle('active');
    }
};

// Initialize auth guard on page load
document.addEventListener('DOMContentLoaded', () => {
    const isPublic = PUBLIC_PAGES.some(p => window.location.pathname.includes(p));
    if (!isPublic) {
        AuthGuardHelper.check();
        AuthGuardHelper.getUserInfo();
    }
});

// =============================================
// GLOBAL HELPERS (used directly in HTML pages)
// =============================================

// Returns true if the currently logged-in user is a DEMO guest
function isDemo() {
    const user = Storage.getUser();
    return user && user.role === 'DEMO';
}

// AuthGuard as a callable function (pages call AuthGuard() directly)
function AuthGuard() {
    const token = Storage.getToken();
    if (!token) {
        window.location.href = 'index.html';
        return false;
    }
    // Show username in sidebar if element exists
    const user = Storage.getUser();
    if (user) {
        document.querySelectorAll('.user-name').forEach(el => el.textContent = user.username || user.name || '');
        document.querySelectorAll('.user-avatar').forEach(el => {
            el.textContent = (user.username || user.name || 'A').charAt(0).toUpperCase();
        });
    }

    // Demo mode: show sticky banner and add body class
    if (isDemo()) {
        document.body.classList.add('demo-mode');
        if (!document.getElementById('demoBanner')) {
            const banner = document.createElement('div');
            banner.id = 'demoBanner';
            banner.style.cssText = 'position:fixed;bottom:0;left:0;right:0;z-index:9999;background:#f59e0b;color:#1e293b;text-align:center;padding:8px 16px;font-size:0.85rem;font-weight:600;display:flex;align-items:center;justify-content:center;gap:8px;box-shadow:0 -2px 8px rgba(0,0,0,0.15);';
            banner.innerHTML = '<i class="fas fa-eye"></i> Demo / Guest Mode &mdash; View only. Add, Edit &amp; Delete actions are disabled.';
            document.body.appendChild(banner);
        }
    }
    return true;
}

// Global showAlert used by all pages
function showAlert(message, type = 'info') {
    // Remove existing alerts
    document.querySelectorAll('.page-alert').forEach(el => el.remove());

    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} page-alert`;
    alertDiv.textContent = message;
    Object.assign(alertDiv.style, {
        position: 'fixed',
        top: '20px',
        right: '20px',
        zIndex: '9999',
        minWidth: '300px',
        maxWidth: '450px',
        boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
        animation: 'slideIn 0.3s ease'
    });

    document.body.appendChild(alertDiv);
    setTimeout(() => alertDiv.remove(), 4000);
}

// Add slideIn animation style once
const _alertStyle = document.createElement('style');
_alertStyle.textContent = '@keyframes slideIn { from { transform: translateX(120%); opacity: 0; } to { transform: translateX(0); opacity: 1; } }';
document.head.appendChild(_alertStyle);

// =============================================
// GLOBAL ERROR HANDLING
// =============================================

/**
 * Redirect to the error page with query parameters.
 * code    — HTTP status code or short code shown large on the page
 * title   — Short heading
 * message — User-friendly explanation
 * detail  — (optional) raw technical detail shown on toggle
 */
function redirectToError(code, title, message, detail) {
    // Don't redirect if already on the error or login page
    const page = window.location.pathname.split('/').pop();
    if (page === 'error.html' || page === 'login.html') return;

    const params = new URLSearchParams();
    if (code)    params.set('code',    code);
    if (title)   params.set('title',   title);
    if (message) params.set('message', message);
    if (detail)  params.set('detail',  encodeURIComponent(detail));
    window.location.href = 'error.html?' + params.toString();
}

// Catch unhandled JS runtime errors
window.addEventListener('error', function (e) {
    // Ignore resource-load errors (images, scripts 404 etc.)
    if (e.target && e.target !== window) return;
    const detail = `${e.message} (${e.filename}:${e.lineno}:${e.colno})`;
    redirectToError('ERR', 'Unexpected Error', 'A JavaScript error occurred on this page.', detail);
});

// Catch unhandled Promise rejections
window.addEventListener('unhandledrejection', function (e) {
    const msg = e.reason?.message || String(e.reason);
    // Ignore benign fetch aborts / network errors that pages already handle
    if (msg.toLowerCase().includes('failed to fetch') ||
        msg.toLowerCase().includes('networkerror')) return;
    redirectToError('ERR', 'Unexpected Error', 'An unhandled error occurred.', msg);
});
