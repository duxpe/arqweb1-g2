// assets/js/editUser.js

console.log("editUser.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', async () => {
    const CONTEXT_PATH = '/g2';
    const USER_API_URL = `${CONTEXT_PATH}/editar-usuario`; // Servlet que retorna o JSON do usuário
    const editUserForm = document.getElementById('edit-user-form');
    const messageArea = document.getElementById('message-area');

    // Elementos do formulário
    const userIdInput = document.getElementById('user-id');
    const usuarioInput = document.getElementById('usuario');
    const nomeInput = document.getElementById('nome');
    const idadeInput = document.getElementById('idade');
    const senhaInput = document.getElementById('senha'); // Campo de senha, opcional

    // Função para extrair parâmetros da URL
    function getQueryParam(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }

    const userId = getQueryParam('id');

    if (!userId) {
        displayMessage("Erro: ID do usuário não fornecido. Redirecionando para a lista de usuários.", 'danger');
        // Adiciona um pequeno atraso antes de redirecionar para que a mensagem seja vista
        setTimeout(() => {
            window.location.href = `${CONTEXT_PATH}/listar-usuario`;
        }, 2000); 
        return;
    }

    // Exibir mensagem de erro vinda da URL (após um POST que falhou, por exemplo)
    const errorMessageFromUrl = getQueryParam('erro');
    if (errorMessageFromUrl) {
        displayMessage(errorMessageFromUrl, 'danger');
        // Limpa o parâmetro da URL para evitar que a mensagem apareça em recarregamentos futuros
        history.replaceState(null, '', window.location.pathname + '?id=' + userId);
    }

    try {
        // Busca os dados do usuário usando o ID da URL
        const response = await fetch(`${USER_API_URL}?id=${encodeURIComponent(userId)}`);

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ message: `Erro ao buscar usuário: ${response.status} ${response.statusText}` }));
            displayMessage(errorData.message, 'danger');
            // Redireciona para a lista de usuários em caso de erro grave (e.g., usuário não encontrado)
            setTimeout(() => {
                window.location.href = `${CONTEXT_PATH}/listar-usuario`;
            }, 2000);
            return;
        }

        const userData = await response.json();

        // Preenche o formulário com os dados do usuário
        userIdInput.value = userData.id;
        usuarioInput.value = userData.usuario;
        nomeInput.value = userData.nome;
        idadeInput.value = userData.idade;
        // O campo de senha deve permanecer em branco por padrão
        senhaInput.value = ''; 

    } catch (error) {
        console.error("Erro ao carregar dados do usuário para edição:", error);
        displayMessage(`Erro ao carregar os dados do usuário: ${error.message}`, 'danger');
        setTimeout(() => {
            window.location.href = `${CONTEXT_PATH}/listar-usuario`;
        }, 5000);
    }

    // Lógica de validação do formulário Bootstrap
    editUserForm.addEventListener('submit', (event) => {
        if (!editUserForm.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        editUserForm.classList.add('was-validated');
    }, false);

    // Função para exibir mensagens de status
    function displayMessage(message, type = 'info') {
        messageArea.innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
                                    ${message}
                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                 </div>`;
    }
});