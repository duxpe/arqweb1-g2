// assets/js/listUsers.js

console.log("listUsers.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', async () => {
    const CONTEXT_PATH = '/g2';
    const USERS_API_URL = `${CONTEXT_PATH}/listar-usuario`;
    const usersContainer = document.getElementById('users-container');
    const noUsersMessage = document.getElementById('no-users-message');
    const messageArea = document.getElementById('message-area');

    async function fetchAndRenderUsers() {
        try {
            const response = await fetch(USERS_API_URL);
            
            if (!response.ok) {
                // Tenta ler a mensagem de erro do JSON se disponível
                const errorData = await response.json().catch(() => ({ message: `Erro HTTP: ${response.status} ${response.statusText}` }));
                displayMessage(errorData.message, 'danger');
                return;
            }

            const users = await response.json();

            if (users && users.length > 0) {
                usersContainer.innerHTML = ''; // Limpa qualquer conteúdo existente
                noUsersMessage.classList.add('d-none'); // Esconde a mensagem de nenhum usuário

                users.forEach(user => {
                    const userCard = `
                        <div class="col-md-4 mb-4">
                            <div class="card card-user h-100">
                                <div class="card-body d-flex flex-column">
                                    <h5 class="card-title">${user.usuario}</h5>
                                    <p class="card-text mb-1">
                                        <strong>Nome:</strong> ${user.nome || 'N/A'}
                                    </p>
                                    <p class="card-text mb-3">
                                        <strong>Idade:</strong> ${user.idade || 'N/A'}
                                    </p>
                                    <div class="mt-auto">
                                        <a href="${CONTEXT_PATH}/changeUsuario.html?id=${user.id}"
                                           class="btn btn-sm btn-warning me-2"> Editar </a>
                                        <form action="${CONTEXT_PATH}/excluir-usuario"
                                              method="post" style="display: inline"
                                              onsubmit="return confirm('Confirmar exclusão do usuário ${user.usuario}?');">
                                            <input type="hidden" name="id" value="${user.id}" />
                                            <button type="submit" class="btn btn-sm btn-danger">
                                                Excluir</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    `;
                    usersContainer.insertAdjacentHTML('beforeend', userCard);
                });
            } else {
                usersContainer.innerHTML = ''; // Garante que não há cards antigos
                noUsersMessage.classList.remove('d-none'); // Mostra a mensagem de nenhum usuário
            }
        } catch (error) {
            console.error("Erro ao carregar lista de usuários:", error);
            displayMessage(`Erro ao carregar usuários: ${error.message}`, 'danger');
            usersContainer.innerHTML = ''; // Limpa o container em caso de erro
            noUsersMessage.classList.add('d-none'); // Garante que a mensagem "nenhum usuário" não apareça junto com o erro
        }
    }

    // Função para exibir mensagens de status
    function displayMessage(message, type = 'info') {
        messageArea.innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
                                    ${message}
                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                 </div>`;
    }

    // Pega qualquer mensagem de erro da URL (se o servlet redirecionou com 'erro' na query)
    const urlParams = new URLSearchParams(window.location.search);
    const errorMessageFromUrl = urlParams.get('erro');
    if (errorMessageFromUrl) {
        displayMessage(errorMessageFromUrl, 'danger');
        // Limpa o parâmetro da URL para evitar que a mensagem apareça em recarregamentos futuros
        history.replaceState(null, '', window.location.pathname);
    }

    // Carrega os usuários ao carregar a página
    fetchAndRenderUsers();
});