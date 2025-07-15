console.log("newsDetail.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', async () => {
    // Definir o context path da sua aplicação.
    // Se a aplicação está em http://localhost:8080/g2/, use '/g2'.
    // Se a aplicação está na raiz http://localhost:8080/, use ''.
    const CONTEXT_PATH = '/g2'; 

    const NEWS_API_URL = `${CONTEXT_PATH}/listar-noticia`;
    const LOGIN_STATUS_API_URL = `${CONTEXT_PATH}/auth`; // Seu Servlet de autenticação para verificar login
    const RATING_API_URL = `${CONTEXT_PATH}/obter-avaliacao`; // Servlet para obter avaliação média

    const newsTitle = document.getElementById('news-title');
    const newsAuthor = document.getElementById('news-author');
    const newsDate = document.getElementById('news-date');
    const newsCategory = document.getElementById('news-category');
    const newsViews = document.getElementById('news-views');
    const newsImageContainer = document.getElementById('news-image-container');
    const newsContent = document.getElementById('news-content');
    const editLinkContainer = document.getElementById('edit-link-container');
    const messageArea = document.getElementById('message-area'); // Área de mensagens

    // Elementos da avaliação (NOVOS)
    const averageRatingValue = document.getElementById('average-rating-value');
    const averageRatingStars = document.getElementById('average-rating-stars');
    const ratingCount = document.getElementById('rating-count');
    const userRatingSection = document.getElementById('user-rating-section');
    const notLoggedInMessage = document.getElementById('not-logged-in-message');
    const ratingForm = document.getElementById('rating-form');
    const ratingNewsIdInput = document.getElementById('rating-news-id'); // Input hidden para o ID da notícia

    function getQueryParam(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }

    const newsId = getQueryParam('id');

    // --- Funções de Ajuda ---
    function displayMessage(message, type = 'info') {
        messageArea.innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
                                    ${message}
                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                 </div>`;
        // Limpa os parâmetros de URL para que a mensagem não apareça em um refresh
        const currentUrl = new URL(window.location.href);
        currentUrl.searchParams.delete('sucesso');
        currentUrl.searchParams.delete('erro');
        window.history.replaceState({}, document.title, currentUrl.toString());
    }

    function renderStars(rating, container) {
        container.innerHTML = ''; // Limpa o container antes de adicionar as estrelas
        const fullStars = Math.floor(rating);
        const halfStar = rating - fullStars >= 0.5;
        const emptyStars = 5 - fullStars - (halfStar ? 1 : 0);

        for (let i = 0; i < fullStars; i++) {
            container.innerHTML += '<i class="fas fa-star"></i>';
        }
        if (halfStar) {
            container.innerHTML += '<i class="fas fa-star-half-alt"></i>';
        }
        for (let i = 0; i < emptyStars; i++) {
            container.innerHTML += '<i class="far fa-star"></i>'; // Estrela vazia
        }
    }
    // --- Fim Funções de Ajuda ---


    if (!newsId) {
        newsTitle.textContent = "Erro: ID da notícia não fornecido.";
        newsContent.innerHTML = `<p class="alert alert-danger">Por favor, forneça um ID de notícia válido na URL (ex: viewNoticia.html?id=1).</p>`;
        return;
    }

    // Exibir mensagens de sucesso/erro vindas da URL (após o envio da avaliação)
    const urlSuccessMessage = getQueryParam('sucesso');
    const urlErrorMessage = getQueryParam('erro');
    if (urlSuccessMessage) {
        displayMessage(urlSuccessMessage, 'success');
    } else if (urlErrorMessage) {
        displayMessage(urlErrorMessage, 'danger');
    }

    try {
        // 1. Carregar os dados da notícia
        const newsResponse = await fetch(`${NEWS_API_URL}?id=${encodeURIComponent(newsId)}`);
        if (!newsResponse.ok) {
            const errorText = await newsResponse.text(); 
            throw new Error(`HTTP error! status: ${newsResponse.status} - ${newsResponse.statusText}. Resposta: ${errorText.substring(0, 200)}...`);
        }
        const newsJson = await newsResponse.json();
        console.log("Dados da notícia (listar-noticia) recebidos:", newsJson); // Log para depuração

        let newsData = null;
        // Se o servlet retornar um array (mesmo que com 1 elemento)
        if (Array.isArray(newsJson) && newsJson.length > 0) {
            newsData = newsJson[0];
        } 
        // Se o servlet retornar o objeto Noticia diretamente
        else if (newsJson && typeof newsJson === 'object' && newsJson.titulo) {
            newsData = newsJson;
        }

        if (!newsData || !newsData.titulo) { // Verifica se newsData foi preenchido corretamente
            newsTitle.textContent = "Notícia não encontrada";
            newsContent.innerHTML = `<p class="alert alert-warning">A notícia com ID ${newsId} não foi encontrada ou os dados estão incompletos.</p>`;
            return;
        }

        // Preenche os elementos HTML com os dados da notícia
        newsTitle.textContent = newsData.titulo;
        newsAuthor.textContent = newsData.nomeAutor;
        newsDate.textContent = newsData.dataPublicacao;
        newsCategory.textContent = newsData.categoria;
        newsViews.textContent = newsData.visualizacoes || 0;

        if (newsData.imagem) {
            const imgElement = document.createElement('img');
            imgElement.src = `data:image/png;base64,${newsData.imagem}`;
            imgElement.alt = `Imagem de ${newsData.titulo}`;
            imgElement.classList.add('img-fluid', 'news-image');
            newsImageContainer.appendChild(imgElement);
        } else {
            newsImageContainer.innerHTML = `<div class="bg-light text-center py-5 rounded-lg">Imagem não disponível</div>`;
        }
        
        newsContent.innerHTML = newsData.conteudo || '<p>Conteúdo da notícia não disponível.</p>'; 

        // Preenche o ID da notícia no input hidden do formulário de avaliação
        ratingNewsIdInput.value = newsId;


        // 2. Carregar a avaliação média da notícia (NOVO BLOCO)
        try {
            const ratingResponse = await fetch(`${RATING_API_URL}?idNoticia=${encodeURIComponent(newsId)}`);
            if (!ratingResponse.ok) {
                const errorData = await ratingResponse.json().catch(() => ({ message: `Erro HTTP: ${ratingResponse.status}` }));
                throw new Error(errorData.message || `Falha ao carregar avaliação: ${ratingResponse.status}`);
            }
            const avaliacaoData = await ratingResponse.json();
            console.log("Dados da avaliação (obter-avaliacao) recebidos:", avaliacaoData); // Log para depuração

            const media = avaliacaoData.mediaNotas !== undefined ? avaliacaoData.mediaNotas.toFixed(1) : '0.0';
            const totalNotas = avaliacaoData.totalNotas !== undefined ? avaliacaoData.totalNotas : 0;

            averageRatingValue.textContent = media;
            ratingCount.textContent = `(${totalNotas} avaliações)`;
            renderStars(parseFloat(media), averageRatingStars);

        } catch (ratingError) {
            console.error("Erro ao carregar a avaliação da notícia:", ratingError);
            averageRatingValue.textContent = "N/A";
            ratingCount.textContent = "(Erro ao carregar)";
            averageRatingStars.innerHTML = '<span class="text-danger">Erro ao carregar avaliação.</span>';
        }


        // 3. Verificar o status de login para exibir o formulário de avaliação e botões de edição (AJUSTADO)
        const loginStatusResponse = await fetch(LOGIN_STATUS_API_URL);
        if (!loginStatusResponse.ok) {
            console.error("Erro ao verificar status de login:", loginStatusResponse.status, loginStatusResponse.statusText);
            // Assume não logado ou exibe mensagem se não for possível verificar
            userRatingSection.innerHTML = `<p class="alert alert-warning">Não foi possível verificar seu status de login. Tente recarregar a página.</p>`;
        } else {
            const loginStatusData = await loginStatusResponse.json();
            console.log("Status de Login (auth) recebido:", loginStatusData); // Log para depuração

            if (loginStatusData.loggedIn) { // Supondo que o JSON retornado tenha { "loggedIn": true/false }
                notLoggedInMessage.classList.add('d-none'); // Esconde a mensagem de "não logado"
                ratingForm.classList.remove('d-none'); // Mostra o formulário de avaliação
                
                // Adiciona validação do Bootstrap ao formulário de avaliação
                ratingForm.addEventListener('submit', (event) => {
                    if (!ratingForm.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    ratingForm.classList.add('was-validated');
                });

            } else {
                notLoggedInMessage.classList.remove('d-none'); // Mostra a mensagem de "não logado"
                ratingForm.classList.add('d-none'); // Esconde o formulário de avaliação
            }

            // Exibir/ocultar link de edição/exclusão para admins/editores
            // Adapte 'userRole' para o nome da propriedade que seu AuthServlet retorna
            // E 'admin' para o valor do papel do administrador
            if (loginStatusData.loggedIn && loginStatusData.userRole === 'admin') { 
                editLinkContainer.innerHTML = `
                    <a href="${CONTEXT_PATH}/editNoticia.html?id=${newsData.id}" class="btn btn-sm btn-warning mb-4">Editar</a>
                    <a href="${CONTEXT_PATH}/excluir-noticia?id=${newsData.id}" class="btn btn-sm btn-danger mb-4 ml-2" onclick="return confirm('Tem certeza que deseja excluir esta notícia?');">Excluir</a>
                `;
            }
        }

    } catch (error) {
        console.error("Erro geral ao carregar detalhes da notícia, avaliação ou status de login:", error);
        newsTitle.textContent = "Erro ao carregar notícia";
        newsContent.innerHTML = `<p class="alert alert-danger">Não foi possível carregar os detalhes da notícia. Por favor, tente novamente mais tarde. Detalhes: ${error.message}</p>`;
        userRatingSection.innerHTML = `<p class="alert alert-danger">Erro ao carregar formulário de avaliação: ${error.message}</p>`;
    }
});