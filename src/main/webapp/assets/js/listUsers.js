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
                const errorData = await response.json().catch(() => ({ message: `Erro HTTP: ${response.status} ${response.statusText}` }));
                displayMessage(errorData.message, 'danger');
                return;
            }

            const users = await response.json();

            if (users && users.length > 0) {
                usersContainer.innerHTML = '';
                noUsersMessage.classList.add('d-none');

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
                usersContainer.innerHTML = '';
                noUsersMessage.classList.remove('d-none');
            }
        } catch (error) {
            console.error("Erro ao carregar lista de usuários:", error);
            displayMessage(`Erro ao carregar usuários: ${error.message}`, 'danger');
            usersContainer.innerHTML = '';
            noUsersMessage.classList.add('d-none');
        }
    }

    function displayMessage(message, type = 'info') {
        messageArea.innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
                                    ${message}
                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                 </div>`;
    }

    const urlParams = new URLSearchParams(window.location.search);
    const errorMessageFromUrl = urlParams.get('erro');
    if (errorMessageFromUrl) {
        displayMessage(errorMessageFromUrl, 'danger');
        history.replaceState(null, '', window.location.pathname);
    }

    fetchAndRenderUsers();
});