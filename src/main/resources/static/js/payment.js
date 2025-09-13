// Payment page JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initializePaymentPage();
});

function initializePaymentPage() {
    // Initialize payment method selection
    initializePaymentMethodSelection();
    
    // Initialize form validation
    initializeFormValidation();
    
    // Initialize card number formatting
    initializeCardFormatting();
    
    // Initialize form submission
    initializeFormSubmission();
}

function initializePaymentMethodSelection() {
    const paymentOptions = document.querySelectorAll('input[name="paymentMethod"]');
    const paymentForms = document.querySelectorAll('.payment-method-form');
    
    paymentOptions.forEach(option => {
        option.addEventListener('change', function() {
            // Hide all payment method forms
            paymentForms.forEach(form => {
                form.style.display = 'none';
            });
            
            // Show selected payment method form
            const selectedForm = document.getElementById(this.value.toLowerCase() + '-form');
            if (selectedForm) {
                selectedForm.style.display = 'block';
            }
            
            // Update required fields
            updateRequiredFields(this.value);
        });
    });
    
    // Set default payment method
    const defaultOption = document.querySelector('input[name="paymentMethod"]:checked');
    if (defaultOption) {
        defaultOption.dispatchEvent(new Event('change'));
    }
}

function updateRequiredFields(paymentMethod) {
    const upiInput = document.getElementById('upiId');
    const cardInput = document.getElementById('cardNumber');
    const bankSelect = document.getElementById('bank');
    const walletSelect = document.getElementById('wallet');
    
    // Remove required from all inputs
    [upiInput, cardInput, bankSelect, walletSelect].forEach(input => {
        if (input) {
            input.removeAttribute('required');
        }
    });
    
    // Add required to appropriate input based on payment method
    switch (paymentMethod) {
        case 'UPI':
            if (upiInput) upiInput.setAttribute('required', 'required');
            break;
        case 'CREDIT_CARD':
        case 'DEBIT_CARD':
            if (cardInput) cardInput.setAttribute('required', 'required');
            break;
        case 'NET_BANKING':
            if (bankSelect) bankSelect.setAttribute('required', 'required');
            break;
        case 'WALLET':
            if (walletSelect) walletSelect.setAttribute('required', 'required');
            break;
    }
}

function initializeFormValidation() {
    const form = document.querySelector('.payment-form');
    if (!form) return;
    
    form.addEventListener('submit', function(e) {
        if (!validatePaymentForm()) {
            e.preventDefault();
        }
    });
    
    // Real-time validation
    const inputs = form.querySelectorAll('input[required], select[required]');
    inputs.forEach(input => {
        input.addEventListener('blur', function() {
            validateField(this);
        });
        
        input.addEventListener('input', function() {
            clearFieldError(this);
        });
    });
}

function validatePaymentForm() {
    const form = document.querySelector('.payment-form');
    const selectedPaymentMethod = form.querySelector('input[name="paymentMethod"]:checked');
    
    if (!selectedPaymentMethod) {
        showError('Please select a payment method');
        return false;
    }
    
    let isValid = true;
    const activeForm = document.querySelector('.payment-method-form[style*="block"]');
    
    if (activeForm) {
        const requiredInputs = activeForm.querySelectorAll('input[required], select[required]');
        requiredInputs.forEach(input => {
            if (!validateField(input)) {
                isValid = false;
            }
        });
    }
    
    // Additional validations based on payment method
    const paymentMethod = selectedPaymentMethod.value;
    if (paymentMethod === 'CREDIT_CARD' || paymentMethod === 'DEBIT_CARD') {
        if (!validateCardDetails()) {
            isValid = false;
        }
    } else if (paymentMethod === 'UPI') {
        if (!validateUPI()) {
            isValid = false;
        }
    }
    
    return isValid;
}

function validateField(field) {
    const value = field.value.trim();
    let isValid = true;
    let errorMessage = '';
    
    if (field.hasAttribute('required') && !value) {
        errorMessage = 'This field is required';
        isValid = false;
    } else if (value) {
        switch (field.type) {
            case 'email':
                if (!isValidEmail(value)) {
                    errorMessage = 'Please enter a valid email address';
                    isValid = false;
                }
                break;
            case 'tel':
                if (!isValidPhone(value)) {
                    errorMessage = 'Please enter a valid phone number';
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

function validateCardDetails() {
    const cardNumber = document.getElementById('cardNumber');
    const expiryDate = document.getElementById('expiryDate');
    const cvv = document.getElementById('cvv');
    
    let isValid = true;
    
    if (cardNumber && !isValidCardNumber(cardNumber.value)) {
        showFieldError(cardNumber, 'Please enter a valid card number');
        isValid = false;
    }
    
    if (expiryDate && !isValidExpiryDate(expiryDate.value)) {
        showFieldError(expiryDate, 'Please enter a valid expiry date (MM/YY)');
        isValid = false;
    }
    
    if (cvv && !isValidCVV(cvv.value)) {
        showFieldError(cvv, 'Please enter a valid CVV');
        isValid = false;
    }
    
    return isValid;
}

function validateUPI() {
    const upiInput = document.getElementById('upiId');
    if (upiInput && !isValidUPI(upiInput.value)) {
        showFieldError(upiInput, 'Please enter a valid UPI ID');
        return false;
    }
    return true;
}

function isValidCardNumber(cardNumber) {
    // Remove spaces and check if it's a valid card number
    const cleaned = cardNumber.replace(/\s/g, '');
    return /^\d{13,19}$/.test(cleaned);
}

function isValidExpiryDate(expiryDate) {
    const regex = /^(0[1-9]|1[0-2])\/\d{2}$/;
    if (!regex.test(expiryDate)) return false;
    
    const [month, year] = expiryDate.split('/');
    const currentDate = new Date();
    const currentYear = currentDate.getFullYear() % 100;
    const currentMonth = currentDate.getMonth() + 1;
    
    const expYear = parseInt(year);
    const expMonth = parseInt(month);
    
    if (expYear < currentYear || (expYear === currentYear && expMonth < currentMonth)) {
        return false;
    }
    
    return true;
}

function isValidCVV(cvv) {
    return /^\d{3,4}$/.test(cvv);
}

function isValidUPI(upiId) {
    return /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+$/.test(upiId);
}

function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function isValidPhone(phone) {
    return /^[\+]?[1-9][\d]{0,15}$/.test(phone.replace(/\s/g, ''));
}

function showFieldError(field, message) {
    clearFieldError(field);
    
    field.style.borderColor = '#f44336';
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error';
    errorDiv.textContent = message;
    
    field.parentNode.appendChild(errorDiv);
}

function clearFieldError(field) {
    field.style.borderColor = '#444';
    
    const existingError = field.parentNode.querySelector('.field-error');
    if (existingError) {
        existingError.remove();
    }
}

function showError(message) {
    // Remove existing error messages
    const existingError = document.querySelector('.error-message');
    if (existingError) {
        existingError.remove();
    }
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.innerHTML = `<i class="fas fa-exclamation-circle"></i><span>${message}</span>`;
    
    document.body.appendChild(errorDiv);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (errorDiv.parentNode) {
            errorDiv.remove();
        }
    }, 5000);
}

function initializeCardFormatting() {
    const cardNumberInput = document.getElementById('cardNumber');
    const expiryInput = document.getElementById('expiryDate');
    const cvvInput = document.getElementById('cvv');
    
    if (cardNumberInput) {
        cardNumberInput.addEventListener('input', function() {
            let value = this.value.replace(/\s/g, '').replace(/[^0-9]/gi, '');
            let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
            this.value = formattedValue;
        });
    }
    
    if (expiryInput) {
        expiryInput.addEventListener('input', function() {
            let value = this.value.replace(/\D/g, '');
            if (value.length >= 2) {
                value = value.substring(0, 2) + '/' + value.substring(2, 4);
            }
            this.value = value;
        });
    }
    
    if (cvvInput) {
        cvvInput.addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    }
}

function initializeFormSubmission() {
    const form = document.querySelector('.payment-form');
    const payButton = document.getElementById('pay-button');
    
    if (form && payButton) {
        form.addEventListener('submit', function(e) {
            // Show loading state
            payButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Processing Payment...';
            payButton.disabled = true;
            
            // Add processing class to form
            form.classList.add('processing');
            
            // Re-enable button after 10 seconds (in case of navigation issues)
            setTimeout(() => {
                payButton.innerHTML = '<i class="fas fa-credit-card"></i> Pay Now';
                payButton.disabled = false;
                form.classList.remove('processing');
            }, 10000);
        });
    }
}

// Add CSS for field errors
const style = document.createElement('style');
style.textContent = `
    .field-error {
        color: #f44336;
        font-size: 0.8rem;
        margin-top: 0.25rem;
        display: flex;
        align-items: center;
        gap: 0.25rem;
    }
    
    .field-error::before {
        content: '⚠';
        font-size: 0.7rem;
    }
`;
document.head.appendChild(style);
