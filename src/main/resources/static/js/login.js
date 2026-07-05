const loginForm = document.getElementById('loginForm');
const errorMessage = document.getElementById('errorMessage');

function showError(message) {
    errorMessage.textContent = message;
    errorMessage.style.display = 'block';
}

function hideError() {
    errorMessage.style.display = 'none';
}

loginForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    hideError();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const submitButton = loginForm.querySelector('button[type="submit"]');
    const originalText = submitButton.textContent;
    submitButton.disabled = true;
    submitButton.textContent = 'Logging in...';

    try {
        const params = new URLSearchParams();
        params.set('username', username);
        params.set('password', password);

        const response = await fetch('/api/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('userId', data.userId);
            localStorage.setItem('username', data.username);
            window.location.href = 'gallery.html';
            return;
        }

        if (response.status === 401) {
            showError('Invalid username or password');
        } else {
            showError('Something went wrong. Please try again.');
        }
    } catch (err) {
        showError('Unable to reach the server. Please try again.');
    } finally {
        submitButton.disabled = false;
        submitButton.textContent = originalText;
    }
});
