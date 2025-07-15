console.log("header.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', () => {
    // Definimos o context path aqui. Se seu app mudar de contexto, você só precisa mudar esta linha.
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
            
            // Renderiza o menu apropriado com base no status de login e o userId, se disponível
            if (data.loggedIn) {
                renderLoggedInMenu(data.userId); // Passa o userId se o backend o retornar
            } else {
                renderLoggedOutMenu(null);
            }
        } catch (error) {
            console.error("Erro na requisição de status de login:", error);
            // Em caso de erro de rede/parse, assume não logado
            renderLoggedOutMenu(null);
        }
    }

    // Função para renderizar o menu quando o usuário está logado
    function renderLoggedInMenu(userId) {
		console.log("entrou acola");

        mainMenuContainer.innerHTML = `
            <li class="nav-item"><a class="nav-link" href="${CONTEXT_PATH}/sobre.html">Sobre o Sistema</a></li>
            <li class="nav-item"><a class="nav-link" href="${CONTEXT_PATH}/addNoticia.html">Cadastrar Notícia</a></li>
            <li class="nav-item"><a class="nav-link" href="${CONTEXT_PATH}/listUsuarios.html">Listar Usuários</a></li>
            ${userId ? `<li class="nav-item"><a class="nav-link" href="${CONTEXT_PATH}/changeUsuario.html?id=${userId}">Configurações Pessoais</a></li>` : ''}
        `;

        authMenuContainer.innerHTML = `
            <li class="nav-item"><a class="nav-link" href="${CONTEXT_PATH}/logout">Logout</a></li>
        `;
    }

    // Função para renderizar o menu quando o usuário NÃO está logado
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

    // Chama a função para atualizar o menu ao carregar a página
    updateHeaderMenu();
});
