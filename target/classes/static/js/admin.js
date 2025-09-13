// Admin Dashboard JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initializeAdminDashboard();
});

function initializeAdminDashboard() {
    // Initialize dashboard functionality
    initializeStatsAnimation();
    initializeActivityCards();
    initializeQuickActions();
    initializeDataTables();
    initializeRealTimeUpdates();
}

function initializeStatsAnimation() {
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
    const text = element.textContent;
    const number = parseFloat(text.replace(/[^\d.]/g, ''));
    const suffix = text.replace(/[\d.]/g, '');
    
    if (isNaN(number)) return;
    
    const duration = 2000;
    const increment = number / (duration / 16);
    let current = 0;
    
    const timer = setInterval(() => {
        current += increment;
        if (current >= number) {
            current = number;
            clearInterval(timer);
        }
        
        if (suffix.includes('₹')) {
            element.textContent = '₹' + Math.floor(current).toLocaleString();
        } else {
            element.textContent = Math.floor(current).toLocaleString() + suffix;
        }
    }, 16);
}

function initializeActivityCards() {
    const activityCards = document.querySelectorAll('.activity-card');
    
    activityCards.forEach(card => {
        // Add hover effects
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
            this.style.boxShadow = '0 8px 25px rgba(0, 0, 0, 0.2)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
            this.style.boxShadow = 'none';
        });
    });
}

function initializeQuickActions() {
    const actionCards = document.querySelectorAll('.action-card');
    
    actionCards.forEach(card => {
        card.addEventListener('click', function(e) {
            // Add loading state
            const originalContent = this.innerHTML;
            this.innerHTML = '<div class="loading-spinner"><i class="fas fa-spinner fa-spin"></i> Loading...</div>';
            
            // Re-enable after navigation
            setTimeout(() => {
                this.innerHTML = originalContent;
            }, 1000);
        });
    });
}

function initializeDataTables() {
    const tables = document.querySelectorAll('.table');
    
    tables.forEach(table => {
        // Add sorting functionality
        const headers = table.querySelectorAll('th[data-sortable]');
        headers.forEach(header => {
            header.style.cursor = 'pointer';
            header.addEventListener('click', function() {
                sortTable(table, this.cellIndex);
            });
        });
        
        // Add search functionality
        const searchInput = table.closest('.data-table').querySelector('.table-search');
        if (searchInput) {
            searchInput.addEventListener('input', function() {
                filterTable(table, this.value);
            });
        }
    });
}

function sortTable(table, columnIndex) {
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.querySelectorAll('tr'));
    
    const isAscending = table.dataset.sortDirection !== 'asc';
    table.dataset.sortDirection = isAscending ? 'asc' : 'desc';
    
    rows.sort((a, b) => {
        const aValue = a.cells[columnIndex].textContent.trim();
        const bValue = b.cells[columnIndex].textContent.trim();
        
        // Try to parse as numbers
        const aNum = parseFloat(aValue.replace(/[^\d.-]/g, ''));
        const bNum = parseFloat(bValue.replace(/[^\d.-]/g, ''));
        
        if (!isNaN(aNum) && !isNaN(bNum)) {
            return isAscending ? aNum - bNum : bNum - aNum;
        } else {
            return isAscending ? aValue.localeCompare(bValue) : bValue.localeCompare(aValue);
        }
    });
    
    // Re-append sorted rows
    rows.forEach(row => tbody.appendChild(row));
    
    // Update sort indicators
    const headers = table.querySelectorAll('th');
    headers.forEach((header, index) => {
        header.classList.remove('sort-asc', 'sort-desc');
        if (index === columnIndex) {
            header.classList.add(isAscending ? 'sort-asc' : 'sort-desc');
        }
    });
}

function filterTable(table, searchTerm) {
    const tbody = table.querySelector('tbody');
    const rows = tbody.querySelectorAll('tr');
    
    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        const matches = text.includes(searchTerm.toLowerCase());
        row.style.display = matches ? '' : 'none';
    });
}

function initializeRealTimeUpdates() {
    // Simulate real-time updates every 30 seconds
    setInterval(() => {
        updateDashboardStats();
    }, 30000);
}

function updateDashboardStats() {
    // In a real application, this would make an AJAX call to get updated stats
    console.log('Updating dashboard stats...');
    
    // Simulate stat updates
    const statNumbers = document.querySelectorAll('.stat-number');
    statNumbers.forEach(stat => {
        const currentValue = parseInt(stat.textContent.replace(/[^\d]/g, ''));
        const newValue = currentValue + Math.floor(Math.random() * 5);
        
        // Animate to new value
        animateToValue(stat, newValue, stat.textContent.replace(/[\d]/g, ''));
    });
}

function animateToValue(element, targetValue, suffix) {
    const currentValue = parseInt(element.textContent.replace(/[^\d]/g, ''));
    const difference = targetValue - currentValue;
    const duration = 1000;
    const increment = difference / (duration / 16);
    let current = currentValue;
    
    const timer = setInterval(() => {
        current += increment;
        if ((increment > 0 && current >= targetValue) || (increment < 0 && current <= targetValue)) {
            current = targetValue;
            clearInterval(timer);
        }
        
        if (suffix.includes('₹')) {
            element.textContent = '₹' + Math.floor(current).toLocaleString();
        } else {
            element.textContent = Math.floor(current).toLocaleString() + suffix;
        }
    }, 16);
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

// Add CSS for additional admin features
const style = document.createElement('style');
style.textContent = `
    .loading-spinner {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 0.5rem;
        color: #ff6b6b;
    }
    
    .table th[data-sortable] {
        position: relative;
    }
    
    .table th[data-sortable]:after {
        content: '↕';
        position: absolute;
        right: 8px;
        opacity: 0.5;
    }
    
    .table th.sort-asc:after {
        content: '↑';
        opacity: 1;
        color: #ff6b6b;
    }
    
    .table th.sort-desc:after {
        content: '↓';
        opacity: 1;
        color: #ff6b6b;
    }
    
    .table-search {
        width: 100%;
        padding: 0.75rem;
        border: 1px solid #444;
        border-radius: 8px;
        background: #1a1a1a;
        color: #ffffff;
        margin-bottom: 1rem;
    }
    
    .table-search:focus {
        outline: none;
        border-color: #ff6b6b;
    }
    
    .notification {
        position: fixed;
        top: 100px;
        right: 20px;
        background: #2d2d2d;
        border: 1px solid #444;
        border-radius: 8px;
        padding: 1rem;
        display: flex;
        align-items: center;
        gap: 1rem;
        z-index: 1000;
        max-width: 400px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
    }
    
    .notification-success {
        border-color: #4caf50;
        background: rgba(76, 175, 80, 0.1);
    }
    
    .notification-error {
        border-color: #f44336;
        background: rgba(244, 67, 54, 0.1);
    }
    
    .notification-warning {
        border-color: #ff9800;
        background: rgba(255, 152, 0, 0.1);
    }
    
    .notification-info {
        border-color: #2196f3;
        background: rgba(33, 150, 243, 0.1);
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
        color: #888;
        cursor: pointer;
        padding: 0.25rem;
    }
    
    .notification-close:hover {
        color: #fff;
    }
`;
document.head.appendChild(style);
