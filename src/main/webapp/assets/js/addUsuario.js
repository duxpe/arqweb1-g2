console.log("addUsuario.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', () => {
    const addUsuarioForm = document.querySelector('.needs-validation');
    const messageArea = document.getElementById('message-area');

    function displayMessage(message, type = 'info') {
        messageArea.innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
                                    ${message}
                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                 </div>`;
    }

    if (addUsuarioForm) {
        addUsuarioForm.addEventListener('submit', (event) => {
            if (!addUsuarioForm.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            addUsuarioForm.classList.add('was-validated');
        }, false);
    }

    const urlParams = new URLSearchParams(window.location.search);
    const errorMessage = urlParams.get('erro');
    const successMessage = urlParams.get('msg');

    if (errorMessage) {
        displayMessage(errorMessage, 'danger');
        history.replaceState(null, '', window.location.pathname);
    } else if (successMessage) {
        displayMessage(successMessage, 'success');
        history.replaceState(null, '', window.location.pathname);
    }
});