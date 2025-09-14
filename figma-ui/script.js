// SpinGO Bike Rental Platform - JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    // Initialize all components
    initializeNavigation();
    initializeHero();
    initializeStats();
    initializeBikes();
    initializeModals();
    initializeAnimations();
    initializeFilters();
    initializeScrollEffects();
}

// Navigation functionality
function initializeNavigation() {
    const navbar = document.querySelector('.navbar');
    const navLinks = document.querySelectorAll('.nav-link');
    const hamburger = document.getElementById('hamburger');
    const navMenu = document.getElementById('nav-menu');
    
    // Smooth scrolling for navigation links
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            const targetSection = document.querySelector(targetId);
            
            if (targetSection) {
                const offsetTop = targetSection.offsetTop - 80;
                window.scrollTo({
                    top: offsetTop,
                    behavior: 'smooth'
                });
                
                // Update active link
                navLinks.forEach(l => l.classList.remove('active'));
                this.classList.add('active');
            }
        });
    });
    
    // Mobile menu toggle
    if (hamburger && navMenu) {
        hamburger.addEventListener('click', function() {
            navMenu.classList.toggle('active');
            hamburger.classList.toggle('active');
        });
    }
    
    // Navbar scroll effect
    window.addEventListener('scroll', function() {
        if (window.scrollY > 100) {
            navbar.style.background = 'rgba(255, 255, 255, 0.98)';
            navbar.style.boxShadow = '0 2px 20px rgba(0, 0, 0, 0.1)';
        } else {
            navbar.style.background = 'rgba(255, 255, 255, 0.95)';
            navbar.style.boxShadow = 'none';
        }
    });
}

// Hero section functionality
function initializeHero() {
    const heroButtons = document.querySelectorAll('.hero .btn');
    
    heroButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (this.textContent.includes('Start Your Journey')) {
                e.preventDefault();
                document.getElementById('signup-btn').click();
            }
        });
    });
}

// Statistics animation
function initializeStats() {
    const statNumbers = document.querySelectorAll('.stat-number');
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                animateNumber(entry.target);
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.5 });
    
    statNumbers.forEach(stat => {
        observer.observe(stat);
    });
}

function animateNumber(element) {
    const target = parseInt(element.dataset.target);
    const duration = 2000;
    const increment = target / (duration / 16);
    let current = 0;
    
    const timer = setInterval(() => {
        current += increment;
        if (current >= target) {
            current = target;
            clearInterval(timer);
        }
        
        element.textContent = Math.floor(current).toLocaleString();
    }, 16);
}

// Bikes section functionality
function initializeBikes() {
    const bikesGrid = document.getElementById('bikes-grid');
    
    // Sample bike data
    const bikes = [
        {
            id: 1,
            name: "Honda Activa 6G",
            brand: "Honda",
            model: "Activa 6G",
            year: 2023,
            type: "Scooter",
            location: "Mumbai",
            price: 500,
            image: "https://images.unsplash.com/photo-1558618047-3c8c76ca7d13?w=400&h=300&fit=crop",
            features: ["Helmet", "Navigation"],
            status: "Available"
        },
        {
            id: 2,
            name: "Bajaj Pulsar 150",
            brand: "Bajaj",
            model: "Pulsar 150",
            year: 2022,
            type: "Motorcycle",
            location: "Delhi",
            price: 600,
            image: "https://images.unsplash.com/photo-1558618047-3c8c76ca7d13?w=400&h=300&fit=crop",
            features: ["Helmet", "Navigation", "Insured"],
            status: "Available"
        },
        {
            id: 3,
            name: "TVS Jupiter",
            brand: "TVS",
            model: "Jupiter",
            year: 2023,
            type: "Scooter",
            location: "Bangalore",
            price: 450,
            image: "https://images.unsplash.com/photo-1558618047-3c8c76ca7d13?w=400&h=300&fit=crop",
            features: ["Helmet"],
            status: "Available"
        },
        {
            id: 4,
            name: "Yamaha R15 V4",
            brand: "Yamaha",
            model: "R15 V4",
            year: 2023,
            type: "Sports Bike",
            location: "Chennai",
            price: 800,
            image: "https://images.unsplash.com/photo-1558618047-3c8c76ca7d13?w=400&h=300&fit=crop",
            features: ["Helmet", "Navigation", "Insured"],
            status: "Available"
        },
        {
            id: 5,
            name: "Hero Electric Optima",
            brand: "Hero Electric",
            model: "Optima",
            year: 2023,
            type: "Electric",
            location: "Pune",
            price: 400,
            image: "https://images.unsplash.com/photo-1558618047-3c8c76ca7d13?w=400&h=300&fit=crop",
            features: ["Helmet"],
            status: "Available"
        },
        {
            id: 6,
            name: "Royal Enfield Classic 350",
            brand: "Royal Enfield",
            model: "Classic 350",
            year: 2023,
            type: "Cruiser",
            location: "Mumbai",
            price: 700,
            image: "https://images.unsplash.com/photo-1558618047-3c8c76ca7d13?w=400&h=300&fit=crop",
            features: ["Helmet", "Navigation", "Insured"],
            status: "Available"
        }
    ];
    
    // Render bikes
    renderBikes(bikes);
    
    // Store bikes data for filtering
    window.bikesData = bikes;
}

function renderBikes(bikes) {
    const bikesGrid = document.getElementById('bikes-grid');
    
    if (!bikesGrid) return;
    
    bikesGrid.innerHTML = bikes.map(bike => `
        <div class="bike-card" data-bike-id="${bike.id}">
            <div class="bike-image">
                <img src="${bike.image}" alt="${bike.name}" loading="lazy">
                <div class="bike-status">${bike.status}</div>
            </div>
            <div class="bike-info">
                <h3>${bike.name}</h3>
                <p class="bike-details">
                    <span>${bike.brand} ${bike.model}</span>
                    <span>${bike.year}</span>
                </p>
                <div class="bike-features">
                    ${bike.features.map(feature => `
                        <span class="feature">
                            <i class="fas fa-${getFeatureIcon(feature)}"></i> ${feature}
                        </span>
                    `).join('')}
                </div>
                <div class="bike-location">
                    <i class="fas fa-map-marker-alt"></i>
                    <span>${bike.location}</span>
                </div>
                <div class="bike-pricing">
                    <div class="price-item">
                        <span class="price-label">Hourly</span>
                        <span class="price-value">₹${Math.floor(bike.price / 10)}</span>
                    </div>
                    <div class="price-item">
                        <span class="price-label">Daily</span>
                        <span class="price-value">₹${bike.price}</span>
                    </div>
                    <div class="price-item">
                        <span class="price-label">Monthly</span>
                        <span class="price-value">₹${bike.price * 10}</span>
                    </div>
                </div>
                <div class="bike-actions">
                    <button class="btn btn-outline" onclick="viewBikeDetails(${bike.id})">
                        <i class="fas fa-eye"></i> View Details
                    </button>
                    <button class="btn btn-primary" onclick="bookBike(${bike.id})">
                        <i class="fas fa-calendar-plus"></i> Book Now
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

function getFeatureIcon(feature) {
    const icons = {
        'Helmet': 'hard-hat',
        'Navigation': 'map-marker-alt',
        'Insured': 'shield-alt'
    };
    return icons[feature] || 'check';
}

// Modal functionality
function initializeModals() {
    const loginBtn = document.getElementById('login-btn');
    const signupBtn = document.getElementById('signup-btn');
    const loginModal = document.getElementById('login-modal');
    const signupModal = document.getElementById('signup-modal');
    const loginClose = document.getElementById('login-close');
    const signupClose = document.getElementById('signup-close');
    const showSignup = document.getElementById('show-signup');
    const showLogin = document.getElementById('show-login');
    
    // Open modals
    if (loginBtn && loginModal) {
        loginBtn.addEventListener('click', () => openModal(loginModal));
    }
    
    if (signupBtn && signupModal) {
        signupBtn.addEventListener('click', () => openModal(signupModal));
    }
    
    // Close modals
    if (loginClose && loginModal) {
        loginClose.addEventListener('click', () => closeModal(loginModal));
    }
    
    if (signupClose && signupModal) {
        signupClose.addEventListener('click', () => closeModal(signupModal));
    }
    
    // Switch between modals
    if (showSignup && signupModal && loginModal) {
        showSignup.addEventListener('click', (e) => {
            e.preventDefault();
            closeModal(loginModal);
            openModal(signupModal);
        });
    }
    
    if (showLogin && loginModal && signupModal) {
        showLogin.addEventListener('click', (e) => {
            e.preventDefault();
            closeModal(signupModal);
            openModal(loginModal);
        });
    }
    
    // Close modal on outside click
    [loginModal, signupModal].forEach(modal => {
        if (modal) {
            modal.addEventListener('click', (e) => {
                if (e.target === modal) {
                    closeModal(modal);
                }
            });
        }
    });
    
    // Account type selection
    initializeAccountTypeSelection();
    
    // Form handling
    initializeForms();
}

function openModal(modal) {
    if (modal) {
        modal.classList.add('active');
        document.body.style.overflow = 'hidden';
    }
}

function closeModal(modal) {
    if (modal) {
        modal.classList.remove('active');
        document.body.style.overflow = 'auto';
    }
}

function initializeAccountTypeSelection() {
    const accountTypeButtons = document.querySelectorAll('.account-type-btn');
    const accountTypeInfos = document.querySelectorAll('.account-type-info');
    
    const accountTypeMap = {
        'individual': {
            text: 'Individual Owner',
            icon: 'fas fa-bicycle'
        },
        'business': {
            text: 'Business Owner',
            icon: 'fas fa-building'
        },
        'delivery': {
            text: 'Delivery Partner',
            icon: 'fas fa-truck'
        },
        'customer': {
            text: 'Customer',
            icon: 'fas fa-user'
        }
    };
    
    accountTypeButtons.forEach(button => {
        button.addEventListener('click', function() {
            const type = this.dataset.type;
            
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
            
            // Update button text
            const submitButtons = document.querySelectorAll('.btn-primary');
            submitButtons.forEach(btn => {
                if (btn.textContent.includes('Sign In') || btn.textContent.includes('Create Account')) {
                    btn.innerHTML = btn.textContent.includes('Sign In') 
                        ? `Sign In as ${accountTypeMap[type].text}`
                        : `Create Account as ${accountTypeMap[type].text}`;
                }
            });
        });
    });
}

function initializeForms() {
    const forms = document.querySelectorAll('.auth-form');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const submitButton = this.querySelector('button[type="submit"]');
            const originalText = submitButton.innerHTML;
            
            // Show loading state
            submitButton.innerHTML = '<div class="loading"></div> Processing...';
            submitButton.disabled = true;
            
            // Simulate form submission
            setTimeout(() => {
                submitButton.innerHTML = originalText;
                submitButton.disabled = false;
                
                // Show success message
                showNotification('Form submitted successfully!', 'success');
                
                // Close modal
                const modal = this.closest('.modal');
                if (modal) {
                    closeModal(modal);
                }
            }, 2000);
        });
    });
    
    // Password toggle functionality
    const passwordToggles = document.querySelectorAll('.password-toggle');
    passwordToggles.forEach(toggle => {
        toggle.addEventListener('click', function() {
            const input = this.parentNode.querySelector('input');
            const icon = this.querySelector('i');
            
            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                input.type = 'password';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        });
    });
}

// Filter functionality
function initializeFilters() {
    const locationFilter = document.getElementById('location-filter');
    const typeFilter = document.getElementById('type-filter');
    const priceFilter = document.getElementById('price-filter');
    
    [locationFilter, typeFilter, priceFilter].forEach(filter => {
        if (filter) {
            filter.addEventListener('change', filterBikes);
        }
    });
}

function filterBikes() {
    const locationFilter = document.getElementById('location-filter');
    const typeFilter = document.getElementById('type-filter');
    const priceFilter = document.getElementById('price-filter');
    
    const location = locationFilter ? locationFilter.value : '';
    const type = typeFilter ? typeFilter.value : '';
    const price = priceFilter ? priceFilter.value : '';
    
    let filteredBikes = window.bikesData || [];
    
    // Apply filters
    if (location) {
        filteredBikes = filteredBikes.filter(bike => 
            bike.location.toLowerCase() === location.toLowerCase()
        );
    }
    
    if (type) {
        filteredBikes = filteredBikes.filter(bike => 
            bike.type.toLowerCase() === type.toLowerCase()
        );
    }
    
    if (price) {
        const [min, max] = price.split('-').map(p => parseInt(p.replace('₹', '').replace('+', '')));
        filteredBikes = filteredBikes.filter(bike => {
            if (max) {
                return bike.price >= min && bike.price <= max;
            } else {
                return bike.price >= min;
            }
        });
    }
    
    // Render filtered bikes
    renderBikes(filteredBikes);
    
    // Show no results message if needed
    const bikesGrid = document.getElementById('bikes-grid');
    if (filteredBikes.length === 0 && bikesGrid) {
        bikesGrid.innerHTML = `
            <div class="no-results">
                <i class="fas fa-search"></i>
                <h3>No bikes found</h3>
                <p>Try adjusting your search criteria</p>
            </div>
        `;
    }
}

// Animation functionality
function initializeAnimations() {
    const animatedElements = document.querySelectorAll('.fade-in, .slide-in-left, .slide-in-right');
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
            }
        });
    }, { threshold: 0.1 });
    
    animatedElements.forEach(element => {
        observer.observe(element);
    });
}

// Scroll effects
function initializeScrollEffects() {
    // Parallax effect for hero section
    window.addEventListener('scroll', () => {
        const scrolled = window.pageYOffset;
        const hero = document.querySelector('.hero');
        
        if (hero) {
            hero.style.transform = `translateY(${scrolled * 0.5}px)`;
        }
    });
    
    // Update active navigation link based on scroll position
    window.addEventListener('scroll', () => {
        const sections = document.querySelectorAll('section[id]');
        const navLinks = document.querySelectorAll('.nav-link');
        
        let current = '';
        sections.forEach(section => {
            const sectionTop = section.offsetTop - 100;
            if (window.pageYOffset >= sectionTop) {
                current = section.getAttribute('id');
            }
        });
        
        navLinks.forEach(link => {
            link.classList.remove('active');
            if (link.getAttribute('href') === `#${current}`) {
                link.classList.add('active');
            }
        });
    });
}

// Utility functions
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <i class="fas fa-${getNotificationIcon(type)}"></i>
            <span>${message}</span>
        </div>
        <button class="notification-close" onclick="this.parentNode.remove()">
            <i class="fas fa-times"></i>
        </button>
    `;
    
    // Add notification styles if not already added
    if (!document.querySelector('#notification-styles')) {
        const style = document.createElement('style');
        style.id = 'notification-styles';
        style.textContent = `
            .notification {
                position: fixed;
                top: 100px;
                right: 20px;
                background: white;
                border-radius: 8px;
                padding: 1rem 1.5rem;
                display: flex;
                align-items: center;
                gap: 1rem;
                z-index: 3000;
                max-width: 400px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
                animation: slideInRight 0.3s ease;
            }
            
            .notification-success {
                border-left: 4px solid #4caf50;
            }
            
            .notification-error {
                border-left: 4px solid #f44336;
            }
            
            .notification-warning {
                border-left: 4px solid #ff9800;
            }
            
            .notification-info {
                border-left: 4px solid #2196f3;
            }
            
            .notification-content {
                display: flex;
                align-items: center;
                gap: 0.75rem;
                flex: 1;
            }
            
            .notification-close {
                background: none;
                border: none;
                color: #999;
                cursor: pointer;
                padding: 0.25rem;
            }
            
            .notification-close:hover {
                color: #333;
            }
            
            @keyframes slideInRight {
                from {
                    transform: translateX(100%);
                    opacity: 0;
                }
                to {
                    transform: translateX(0);
                    opacity: 1;
                }
            }
        `;
        document.head.appendChild(style);
    }
    
    document.body.appendChild(notification);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (notification.parentNode) {
            notification.remove();
        }
    }, 5000);
}

function getNotificationIcon(type) {
    const icons = {
        success: 'check-circle',
        error: 'exclamation-circle',
        warning: 'exclamation-triangle',
        info: 'info-circle'
    };
    return icons[type] || 'info-circle';
}

// Bike action functions
function viewBikeDetails(bikeId) {
    const bike = window.bikesData.find(b => b.id === bikeId);
    if (bike) {
        showNotification(`Viewing details for ${bike.name}`, 'info');
        // In a real app, this would open a detailed view or navigate to a details page
    }
}

function bookBike(bikeId) {
    const bike = window.bikesData.find(b => b.id === bikeId);
    if (bike) {
        showNotification(`Booking ${bike.name}...`, 'info');
        // In a real app, this would open a booking modal or navigate to booking page
    }
}

// Add ripple effect to buttons
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('btn') || e.target.closest('.btn')) {
        const button = e.target.classList.contains('btn') ? e.target : e.target.closest('.btn');
        const ripple = document.createElement('span');
        const rect = button.getBoundingClientRect();
        const size = Math.max(rect.width, rect.height);
        const x = e.clientX - rect.left - size / 2;
        const y = e.clientY - rect.top - size / 2;
        
        ripple.style.width = ripple.style.height = size + 'px';
        ripple.style.left = x + 'px';
        ripple.style.top = y + 'px';
        ripple.classList.add('ripple');
        
        button.appendChild(ripple);
        
        setTimeout(() => {
            ripple.remove();
        }, 600);
    }
});

// Add ripple effect styles
const rippleStyle = document.createElement('style');
rippleStyle.textContent = `
    .btn {
        position: relative;
        overflow: hidden;
    }
    
    .ripple {
        position: absolute;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.3);
        transform: scale(0);
        animation: ripple-animation 0.6s linear;
        pointer-events: none;
    }
    
    @keyframes ripple-animation {
        to {
            transform: scale(4);
            opacity: 0;
        }
    }
`;
document.head.appendChild(rippleStyle);
