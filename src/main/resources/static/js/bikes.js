// Bikes page JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Initialize bike page functionality
    initializeBikePage();
    
    // Add smooth scrolling for anchor links
    const anchorLinks = document.querySelectorAll('a[href^="#"]');
    anchorLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            const targetElement = document.querySelector(targetId);
            if (targetElement) {
                targetElement.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
});

function initializeBikePage() {
    // Initialize search form
    initializeSearchForm();
    
    // Initialize bike cards
    initializeBikeCards();
    
    // Initialize booking buttons
    initializeBookingButtons();
    
    // Initialize image lazy loading
    initializeLazyLoading();
}

function initializeSearchForm() {
    const searchForm = document.querySelector('.search-form');
    if (!searchForm) return;
    
    // Add real-time search suggestions
    const locationInput = document.getElementById('location');
    if (locationInput) {
        locationInput.addEventListener('input', function() {
            const query = this.value.trim();
            if (query.length >= 2) {
                showLocationSuggestions(query);
            } else {
                hideLocationSuggestions();
            }
        });
    }
    
    // Add price range slider
    const maxRateInput = document.getElementById('maxRate');
    if (maxRateInput) {
        maxRateInput.addEventListener('input', function() {
            updatePriceDisplay(this.value);
        });
    }
    
    // Form submission with loading state
    searchForm.addEventListener('submit', function(e) {
        const submitButton = this.querySelector('button[type="submit"]');
        if (submitButton) {
            submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Searching...';
            submitButton.disabled = true;
        }
    });
}

function initializeBikeCards() {
    const bikeCards = document.querySelectorAll('.bike-card');
    
    bikeCards.forEach(card => {
        // Add hover effects
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-8px)';
            this.style.boxShadow = '0 15px 40px rgba(0, 0, 0, 0.4)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
            this.style.boxShadow = '0 10px 30px rgba(0, 0, 0, 0.3)';
        });
        
        // Add click tracking
        const bookButton = card.querySelector('.btn-primary');
        if (bookButton) {
            bookButton.addEventListener('click', function(e) {
                trackBookingClick(card);
            });
        }
    });
}

function initializeBookingButtons() {
    const bookingButtons = document.querySelectorAll('.btn-primary[href*="/bookings/new/"]');
    
    bookingButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            // Add loading state
            const originalText = this.innerHTML;
            this.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Loading...';
            this.disabled = true;
            
            // Re-enable after a short delay (in case of navigation issues)
            setTimeout(() => {
                this.innerHTML = originalText;
                this.disabled = false;
            }, 3000);
        });
    });
}

function initializeLazyLoading() {
    const images = document.querySelectorAll('img[data-src]');
    
    if ('IntersectionObserver' in window) {
        const imageObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src;
                    img.classList.remove('lazy');
                    imageObserver.unobserve(img);
                }
            });
        });
        
        images.forEach(img => imageObserver.observe(img));
    } else {
        // Fallback for browsers that don't support IntersectionObserver
        images.forEach(img => {
            img.src = img.dataset.src;
            img.classList.remove('lazy');
        });
    }
}

function showLocationSuggestions(query) {
    // Mock location suggestions - replace with actual API call
    const suggestions = [
        'Mumbai, Maharashtra',
        'Delhi, NCR',
        'Bangalore, Karnataka',
        'Chennai, Tamil Nadu',
        'Kolkata, West Bengal',
        'Hyderabad, Telangana',
        'Pune, Maharashtra',
        'Ahmedabad, Gujarat'
    ].filter(location => 
        location.toLowerCase().includes(query.toLowerCase())
    );
    
    if (suggestions.length > 0) {
        createSuggestionsDropdown(suggestions);
    }
}

function createSuggestionsDropdown(suggestions) {
    // Remove existing dropdown
    hideLocationSuggestions();
    
    const locationInput = document.getElementById('location');
    if (!locationInput) return;
    
    const dropdown = document.createElement('div');
    dropdown.className = 'location-suggestions';
    dropdown.innerHTML = suggestions.map(suggestion => 
        `<div class="suggestion-item" data-value="${suggestion}">${suggestion}</div>`
    ).join('');
    
    // Position dropdown
    const rect = locationInput.getBoundingClientRect();
    dropdown.style.position = 'absolute';
    dropdown.style.top = (rect.bottom + window.scrollY) + 'px';
    dropdown.style.left = rect.left + 'px';
    dropdown.style.width = rect.width + 'px';
    dropdown.style.zIndex = '1000';
    
    // Add click handlers
    dropdown.addEventListener('click', function(e) {
        if (e.target.classList.contains('suggestion-item')) {
            locationInput.value = e.target.dataset.value;
            hideLocationSuggestions();
        }
    });
    
    // Add to DOM
    locationInput.parentNode.style.position = 'relative';
    locationInput.parentNode.appendChild(dropdown);
    
    // Hide on outside click
    document.addEventListener('click', function(e) {
        if (!locationInput.contains(e.target) && !dropdown.contains(e.target)) {
            hideLocationSuggestions();
        }
    });
}

function hideLocationSuggestions() {
    const existingDropdown = document.querySelector('.location-suggestions');
    if (existingDropdown) {
        existingDropdown.remove();
    }
}

function updatePriceDisplay(value) {
    const priceDisplay = document.getElementById('price-display');
    if (priceDisplay) {
        priceDisplay.textContent = value ? `₹${value}/day` : 'Any price';
    }
}

function trackBookingClick(card) {
    const bikeName = card.querySelector('h3').textContent;
    const bikeLocation = card.querySelector('.bike-location span').textContent;
    
    // Track booking attempt (you can integrate with analytics here)
    console.log('Booking attempt:', {
        bike: bikeName,
        location: bikeLocation,
        timestamp: new Date().toISOString()
    });
}

// Utility functions
function formatPrice(price) {
    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR',
        minimumFractionDigits: 0
    }).format(price);
}

function formatDate(date) {
    return new Intl.DateTimeFormat('en-IN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    }).format(new Date(date));
}

// Add CSS for suggestions dropdown
const style = document.createElement('style');
style.textContent = `
    .location-suggestions {
        background: #2d2d2d;
        border: 1px solid #444;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
        max-height: 200px;
        overflow-y: auto;
    }
    
    .suggestion-item {
        padding: 0.75rem 1rem;
        cursor: pointer;
        border-bottom: 1px solid #444;
        transition: background-color 0.2s ease;
    }
    
    .suggestion-item:last-child {
        border-bottom: none;
    }
    
    .suggestion-item:hover {
        background: rgba(255, 107, 107, 0.1);
    }
    
    .lazy {
        opacity: 0;
        transition: opacity 0.3s ease;
    }
    
    .lazy.loaded {
        opacity: 1;
    }
`;
document.head.appendChild(style);
