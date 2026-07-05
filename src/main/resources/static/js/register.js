const registerForm = document.getElementById('registerForm');
const errorMessage = document.getElementById('errorMessage');

function showError(message) {
    errorMessage.textContent = message;
    errorMessage.style.display = 'block';
}

function hideError() {
    errorMessage.style.display = 'none';
}

registerForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    hideError();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const submitButton = registerForm.querySelector('button[type="submit"]');
    const originalText = submitButton.textContent;
    submitButton.disabled = true;
    submitButton.textContent = 'Registering...';

    try {
        const params = new URLSearchParams();
        params.set('username', username);
        params.set('password', password);

        const response = await fetch('/api/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params
        });

        if (response.ok) {
            window.location.href = 'login.html';
            return;
        }

        if (response.status === 409) {
            const data = await response.json();
            showError(data.message || 'The username is taken. Try another one');
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
