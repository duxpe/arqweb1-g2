// assets/js/addUsuario.js

console.log("addUsuario.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', () => {
    const addUsuarioForm = document.querySelector('.needs-validation');
    const messageArea = document.getElementById('message-area');

    // Função para exibir mensagens de status
    function displayMessage(message, type = 'info') {
        messageArea.innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
                                    ${message}
                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                 </div>`;
    }

    // Lógica de validação do formulário Bootstrap
    if (addUsuarioForm) {
        addUsuarioForm.addEventListener('submit', (event) => {
            if (!addUsuarioForm.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            addUsuarioForm.classList.add('was-validated');
        }, false);
    }

    // Verifica se há mensagens de erro ou sucesso na URL
    const urlParams = new URLSearchParams(window.location.search);
    const errorMessage = urlParams.get('erro');
    const successMessage = urlParams.get('msg'); // Para mensagens de sucesso, como após o cadastro

    if (errorMessage) {
        displayMessage(errorMessage, 'danger');
        // Limpa o parâmetro da URL para evitar que a mensagem apareça em recarregamentos futuros
        history.replaceState(null, '', window.location.pathname);
    } else if (successMessage) {
        displayMessage(successMessage, 'success');
        history.replaceState(null, '', window.location.pathname);
    }
});