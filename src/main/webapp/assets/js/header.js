console.log("header.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', () => {
    const CONTEXT_PATH = '/g2'; 
    const LOGIN_STATUS_API_URL = `${CONTEXT_PATH}/auth`; 
    const mainMenuContainer = document.getElementById('main-menu-items');
    const authMenuContainer = document.getElementById('auth-menu-items');

    async function updateHeaderMenu() {
		console.log("entrou ini");

        try {
            const response = await fetch(LOGIN_STATUS_API_URL);
            if (!response.ok) {
                console.error("Erro ao verificar status de login para o cabeçalho:", response.status, response.statusText);
                renderLoggedOutMenu(null);
                return;
            }

            const data = await response.json();
            
            if (data.loggedIn) {
                renderLoggedInMenu(data.userId);
            } else {
                renderLoggedOutMenu(null);
            }
        } catch (error) {
            console.error("Erro na requisição de status de login:", error);
            renderLoggedOutMenu(null);
        }
    }

    function renderLoggedInMenu(userId) {
		console.log("entrou acola");

        mainMenuContainer.innerHTML = `
            <li class="nav-item"><a class="nav-link" href="${CONTEXT_PATH}/sobre.html">Sobre o Sistema</a></li>
            <li class="nav-item"><a class="nav-link" href="${CONTEXT_PATH}/addNoticia.jsp">Cadastrar Notícia</a></li>
            <li class="nav-item"><a class="nav-link" href="${CONTEXT_PATH}/listUsuarios.jsp">Listar Usuários</a></li>
            ${userId ? `<li class="nav-item"><a class="nav-link" href="${CONTEXT_PATH}/changeUsuario.html?id=${userId}">Configurações Pessoais</a></li>` : ''}
        `;

        authMenuContainer.innerHTML = `
            <li class="nav-item"><a class="nav-link" href="${CONTEXT_PATH}/logout">Logout</a></li>
        `;
    }

    function renderLoggedOutMenu() {
		console.log("entrou aqui");
        mainMenuContainer.innerHTML = `
            <li class="nav-item"><a class="nav-link" href="${CONTEXT_PATH}/sobre.html">Sobre o Sistema</a></li>
        `;

        authMenuContainer.innerHTML = `
            <li class="nav-item"><a class="nav-link" href="${CONTEXT_PATH}/login.html">Login</a></li>
            <li class="nav-item"><a class="nav-link" href="${CONTEXT_PATH}/addUsuario.html">Criar Conta</a></li>
        `;
    }

    updateHeaderMenu();
});