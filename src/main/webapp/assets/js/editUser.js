console.log("editUser.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', async () => {
    const CONTEXT_PATH = '/g2';
    const USER_API_URL = `${CONTEXT_PATH}/editar-usuario`;
    const editUserForm = document.getElementById('edit-user-form');
    const messageArea = document.getElementById('message-area');

    const userIdInput = document.getElementById('user-id');
    const usuarioInput = document.getElementById('usuario');
    const nomeInput = document.getElementById('nome');
    const idadeInput = document.getElementById('idade');
    const senhaInput = document.getElementById('senha');
	const isAdminInput = document.getElementById('isAdmin');

    function getQueryParam(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }

    const userId = getQueryParam('id');

    if (!userId) {
        displayMessage("Erro: ID do usuário não fornecido. Redirecionando para a lista de usuários.", 'danger');
        setTimeout(() => {
            window.location.href = `${CONTEXT_PATH}/listar-usuario`;
        }, 2000); 
        return;
    }

    const errorMessageFromUrl = getQueryParam('erro');
    if (errorMessageFromUrl) {
        displayMessage(errorMessageFromUrl, 'danger');
        history.replaceState(null, '', window.location.pathname + '?id=' + userId);
    }

    try {
        const response = await fetch(`${USER_API_URL}?id=${encodeURIComponent(userId)}`);

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ message: `Erro ao buscar usuário: ${response.status} ${response.statusText}` }));
            displayMessage(errorData.message, 'danger');
            setTimeout(() => {
                window.location.href = `${CONTEXT_PATH}/listar-usuario`;
            }, 2000);
            return;
        }

        const userData = await response.json();

        userIdInput.value = userData.id;
        usuarioInput.value = userData.usuario;
        nomeInput.value = userData.nome;
        idadeInput.value = userData.idade;
        senhaInput.value = ''; 
		isAdminInput.checked = userData.admin === true;

    } catch (error) {
        console.error("Erro ao carregar dados do usuário para edição:", error);
        displayMessage(`Erro ao carregar os dados do usuário: ${error.message}`, 'danger');
        setTimeout(() => {
            window.location.href = `${CONTEXT_PATH}/listar-usuario`;
        }, 5000);
    }

    editUserForm.addEventListener('submit', (event) => {
        if (!editUserForm.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        editUserForm.classList.add('was-validated');
    }, false);

    function displayMessage(message, type = 'info') {
        messageArea.innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
                                    ${message}
                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                 </div>`;
    }
});