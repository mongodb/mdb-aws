/**
 * Customer Management System - Frontend JavaScript
 * This file handles all the client-side logic for interacting with the backend REST API
 */

// Global variables
let editMode = false;
const API_URL = '/customers';

// DOM Elements
const customerForm = document.getElementById('customerForm');
const customerTableBody = document.getElementById('customerTableBody');
const formTitle = document.getElementById('formTitle');
const submitBtn = document.getElementById('submitBtn');
const resetBtn = document.getElementById('resetBtn');
const loadingSpinner = document.getElementById('loadingSpinner');
const errorAlert = document.getElementById('errorAlert');
const successAlert = document.getElementById('successAlert');
const errorMessage = document.getElementById('errorMessage');
const successMessage = document.getElementById('successMessage');
const noCustomers = document.getElementById('noCustomers');

// Form fields
const customerIdField = document.getElementById('customerId');
const nameField = document.getElementById('name');
const emailField = document.getElementById('email');
const phoneField = document.getElementById('phone');
const addressField = document.getElementById('address');

/**
 * Initialize the application when the DOM is fully loaded
 */
document.addEventListener('DOMContentLoaded', () => {
    // Load all customers when the page loads
    loadCustomers();

    // Set up event listeners
    customerForm.addEventListener('submit', handleFormSubmit);
    resetBtn.addEventListener('click', resetForm);

    // Set up event listeners for alert close buttons
    document.querySelectorAll('.alert .close').forEach(button => {
        button.addEventListener('click', function() {
            this.parentElement.style.display = 'none';
        });
    });
});

/**
 * Load all customers from the backend and display them in the table
 */
function loadCustomers() {
    showLoading(true);

    fetch(API_URL)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch customers');
            }
            return response.json();
        })
        .then(customers => {
            renderCustomerTable(customers);
            showLoading(false);
        })
        .catch(error => {
            showError('Error loading customers: ' + error.message);
            showLoading(false);
        });
}

/**
 * Render the customer table with the provided data
 * @param {Array} customers - Array of customer objects
 */
function renderCustomerTable(customers) {
    customerTableBody.innerHTML = '';

    if (customers.length === 0) {
        noCustomers.style.display = 'block';
        return;
    }

    noCustomers.style.display = 'none';

    customers.forEach(customer => {
        const row = document.createElement('tr');

        // Get the ID as a string (our backend now provides it directly)
        const customerId = customer.id || 'N/A';

        row.innerHTML = `
            <td>${customerId}</td>
            <td>${escapeHtml(customer.name)}</td>
            <td>${escapeHtml(customer.email)}</td>
            <td>${escapeHtml(customer.phone)}</td>
            <td>${escapeHtml(customer.address)}</td>
            <td>
                <button class="btn btn-primary btn-sm edit-btn" data-id="${customerId}">Edit</button>
                <button class="btn btn-danger btn-sm delete-btn" data-id="${customerId}">Delete</button>
            </td>
        `;

        customerTableBody.appendChild(row);
    });

    // Add event listeners to the edit and delete buttons
    document.querySelectorAll('.edit-btn').forEach(button => {
        button.addEventListener('click', () => editCustomer(button.getAttribute('data-id')));
    });

    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', () => deleteCustomer(button.getAttribute('data-id')));
    });
}

/**
 * Handle form submission (create or update customer)
 * @param {Event} event - Form submit event
 */
function handleFormSubmit(event) {
    event.preventDefault();

    // Validate form
    if (!customerForm.checkValidity()) {
        customerForm.reportValidity();
        return;
    }

    const customerData = {
        name: nameField.value,
        email: emailField.value,
        phone: phoneField.value,
        address: addressField.value
    };

    showLoading(true);

    if (editMode) {
        // Update existing customer
        const customerId = customerIdField.value;

        fetch(`${API_URL}/${customerId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customerData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to update customer');
            }
            return response.json();
        })
        .then(() => {
            showSuccess('Customer updated successfully');
            resetForm();
            loadCustomers();
        })
        .catch(error => {
            showError('Error updating customer: ' + error.message);
            showLoading(false);
        });
    } else {
        // Create new customer
        fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customerData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to create customer');
            }
            return response.json();
        })
        .then(() => {
            showSuccess('Customer added successfully');
            resetForm();
            loadCustomers();
        })
        .catch(error => {
            showError('Error adding customer: ' + error.message);
            showLoading(false);
        });
    }
}

/**
 * Load customer data into the form for editing
 * @param {string} customerId - ID of the customer to edit
 */
function editCustomer(customerId) {
    showLoading(true);

    fetch(`${API_URL}/${customerId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch customer details');
            }
            return response.json();
        })
        .then(customer => {
            // Populate the form with customer data
            customerIdField.value = customerId;
            nameField.value = customer.name;
            emailField.value = customer.email;
            phoneField.value = customer.phone;
            addressField.value = customer.address;

            // Switch to edit mode
            editMode = true;
            formTitle.textContent = 'Edit Customer';
            submitBtn.textContent = 'Update Customer';

            // Scroll to the form
            customerForm.scrollIntoView({ behavior: 'smooth' });

            showLoading(false);
        })
        .catch(error => {
            showError('Error loading customer details: ' + error.message);
            showLoading(false);
        });
}

/**
 * Delete a customer
 * @param {string} customerId - ID of the customer to delete
 */
function deleteCustomer(customerId) {
    if (!confirm('Are you sure you want to delete this customer?')) {
        return;
    }

    showLoading(true);

    fetch(`${API_URL}/${customerId}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to delete customer');
        }

        showSuccess('Customer deleted successfully');
        loadCustomers();
    })
    .catch(error => {
        showError('Error deleting customer: ' + error.message);
        showLoading(false);
    });
}

/**
 * Reset the form to its initial state
 */
function resetForm() {
    customerForm.reset();
    customerIdField.value = '';
    editMode = false;
    formTitle.textContent = 'Add New Customer';
    submitBtn.textContent = 'Save Customer';
}

/**
 * Show or hide the loading spinner
 * @param {boolean} show - Whether to show or hide the spinner
 */
function showLoading(show) {
    loadingSpinner.style.display = show ? 'block' : 'none';
}

/**
 * Show an error message
 * @param {string} message - Error message to display
 */
function showError(message) {
    errorMessage.textContent = message;
    errorAlert.style.display = 'block';

    // Auto-hide after 5 seconds
    setTimeout(() => {
        errorAlert.style.display = 'none';
    }, 5000);
}

/**
 * Show a success message
 * @param {string} message - Success message to display
 */
function showSuccess(message) {
    successMessage.textContent = message;
    successAlert.style.display = 'block';

    // Auto-hide after 5 seconds
    setTimeout(() => {
        successAlert.style.display = 'none';
    }, 5000);
}

/**
 * Escape HTML to prevent XSS attacks
 * @param {string} unsafe - Unsafe string that might contain HTML
 * @return {string} Escaped string
 */
function escapeHtml(unsafe) {
    if (unsafe === null || unsafe === undefined) {
        return '';
    }

    return unsafe
        .toString()
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}
