// Authentication page JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Account type selection functionality
    const accountTypeButtons = document.querySelectorAll('.account-type-btn');
    const accountTypeInfos = document.querySelectorAll('.account-type-info');
    const accountTypeText = document.getElementById('account-type-text');
    const accountTypeInput = document.getElementById('accountType');

    // Account type mapping
    const accountTypeMap = {
        'individual': {
            text: 'Individual Owner',
            value: 'INDIVIDUAL_OWNER'
        },
        'business': {
            text: 'Business Owner',
            value: 'BUSINESS_OWNER'
        },
        'delivery': {
            text: 'Delivery Partner',
            value: 'DELIVERY_PARTNER'
        },
        'customer': {
            text: 'Customer',
            value: 'CUSTOMER'
        }
    };

    accountTypeButtons.forEach(button => {
        button.addEventListener('click', function() {
            const type = this.getAttribute('data-type');
            
            // Remove active class from all buttons and infos
            accountTypeButtons.forEach(btn => btn.classList.remove('active'));
            accountTypeInfos.forEach(info => info.classList.remove('active'));
            
            // Add active class to clicked button
            this.classList.add('active');
            
            // Show corresponding info
            const targetInfo = document.getElementById(type + '-info');
            if (targetInfo) {
                targetInfo.classList.add('active');
            }
            
            // Update text and hidden input
            if (accountTypeText) {
                accountTypeText.textContent = accountTypeMap[type].text;
            }
            if (accountTypeInput) {
                accountTypeInput.value = accountTypeMap[type].value;
            }
        });
    });

    // Form validation
    const forms = document.querySelectorAll('.auth-form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!validateForm(this)) {
                e.preventDefault();
            }
        });
    });

    // Real-time validation
    const inputs = document.querySelectorAll('input[required], textarea[required]');
    inputs.forEach(input => {
        input.addEventListener('blur', function() {
            validateField(this);
        });
        
        input.addEventListener('input', function() {
            clearFieldError(this);
        });
    });

    // Password strength indicator
    const passwordInput = document.getElementById('password');
    if (passwordInput) {
        passwordInput.addEventListener('input', function() {
            showPasswordStrength(this.value);
        });
    }
});

// Toggle password visibility
function togglePassword() {
    const passwordInput = document.getElementById('password');
    const toggleButton = document.querySelector('.password-toggle i');
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleButton.classList.remove('fa-eye');
        toggleButton.classList.add('fa-eye-slash');
    } else {
        passwordInput.type = 'password';
        toggleButton.classList.remove('fa-eye-slash');
        toggleButton.classList.add('fa-eye');
    }
}

// Form validation
function validateForm(form) {
    let isValid = true;
    const inputs = form.querySelectorAll('input[required], textarea[required]');
    
    inputs.forEach(input => {
        if (!validateField(input)) {
            isValid = false;
        }
    });
    
    return isValid;
}

// Field validation
function validateField(field) {
    const value = field.value.trim();
    const fieldName = field.getAttribute('name') || field.id;
    let isValid = true;
    let errorMessage = '';
    
    // Required field check
    if (!value) {
        errorMessage = `${getFieldLabel(fieldName)} is required`;
        isValid = false;
    } else {
        // Specific validations
        switch (fieldName) {
            case 'email':
                if (!isValidEmail(value)) {
                    errorMessage = 'Please enter a valid email address';
                    isValid = false;
                }
                break;
            case 'phone':
                if (!isValidPhone(value)) {
                    errorMessage = 'Please enter a valid phone number';
                    isValid = false;
                }
                break;
            case 'age':
                const age = parseInt(value);
                if (age < 18 || age > 100) {
                    errorMessage = 'Age must be between 18 and 100';
                    isValid = false;
                }
                break;
            case 'password':
                if (value.length < 6) {
                    errorMessage = 'Password must be at least 6 characters long';
                    isValid = false;
                }
                break;
            case 'username':
                if (value.length < 3) {
                    errorMessage = 'Username must be at least 3 characters long';
                    isValid = false;
                }
                break;
        }
    }
    
    if (!isValid) {
        showFieldError(field, errorMessage);
    } else {
        clearFieldError(field);
    }
    
    return isValid;
}

// Show field error
function showFieldError(field, message) {
    clearFieldError(field);
    
    field.style.borderColor = '#ff6b6b';
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error';
    errorDiv.textContent = message;
    
    field.parentNode.appendChild(errorDiv);
}

// Clear field error
function clearFieldError(field) {
    field.style.borderColor = '#444';
    
    const existingError = field.parentNode.querySelector('.field-error');
    if (existingError) {
        existingError.remove();
    }
}

// Get field label
function getFieldLabel(fieldName) {
    const labels = {
        'username': 'Username',
        'email': 'Email',
        'phone': 'Phone',
        'password': 'Password',
        'fullName': 'Full Name',
        'age': 'Age',
        'address': 'Address',
        'drivingLicense': 'Driving License'
    };
    
    return labels[fieldName] || fieldName;
}

// Email validation
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Phone validation
function isValidPhone(phone) {
    const phoneRegex = /^[\+]?[1-9][\d]{0,15}$/;
    return phoneRegex.test(phone.replace(/\s/g, ''));
}

// Password strength indicator
function showPasswordStrength(password) {
    let strength = 0;
    let strengthText = '';
    
    if (password.length >= 6) strength++;
    if (password.match(/[a-z]/)) strength++;
    if (password.match(/[A-Z]/)) strength++;
    if (password.match(/[0-9]/)) strength++;
    if (password.match(/[^a-zA-Z0-9]/)) strength++;
    
    switch (strength) {
        case 0:
        case 1:
            strengthText = 'Very Weak';
            break;
        case 2:
            strengthText = 'Weak';
            break;
        case 3:
            strengthText = 'Fair';
            break;
        case 4:
            strengthText = 'Good';
            break;
        case 5:
            strengthText = 'Strong';
            break;
    }
    
    // You can add a visual strength indicator here if needed
    console.log('Password strength:', strengthText);
}
