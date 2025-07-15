console.log("newsDetail.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', async () => {
    const CONTEXT_PATH = '/g2'; 

    const NEWS_API_URL = `${CONTEXT_PATH}/listar-noticia`;
    const LOGIN_STATUS_API_URL = `${CONTEXT_PATH}/auth`;
    const RATING_API_URL = `${CONTEXT_PATH}/obter-avaliacao`;
    const COMMENTS_API_URL = `${CONTEXT_PATH}/obter-comentarios`;

    const newsTitle = document.getElementById('news-title');
    const newsAuthor = document.getElementById('news-author');
    const newsDate = document.getElementById('news-date');
    const newsCategory = document.getElementById('news-category');
    const newsViews = document.getElementById('news-views');
    const newsImageContainer = document.getElementById('news-image-container');
    const newsContent = document.getElementById('news-content');
    const editLinkContainer = document.getElementById('edit-link-container');
    const messageArea = document.getElementById('message-area');

    const averageRatingValue = document.getElementById('average-rating-value');
    const averageRatingStars = document.getElementById('average-rating-stars');
    const ratingCount = document.getElementById('rating-count');
    const userRatingSection = document.getElementById('user-rating-section');
    const notLoggedInMessage = document.getElementById('not-logged-in-message');
    const ratingForm = document.getElementById('rating-form');
    const ratingNewsIdInput = document.getElementById('rating-news-id');

    const commentsList = document.getElementById('comments-list');
    const noCommentsMessage = document.getElementById('no-comments-message');
    const commentNotLoggedInMessage = document.getElementById('comment-not-logged-in-message');
    const commentForm = document.getElementById('comment-form');
    const commentNewsIdInput = document.getElementById('comment-news-id');
    const commentTextArea = document.getElementById('comment-text');


    function getQueryParam(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }

    const newsId = getQueryParam('id');

    function displayMessage(message, type = 'info') {
        messageArea.innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
                                    ${message}
                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                 </div>`;
        const currentUrl = new URL(window.location.href);
        currentUrl.searchParams.delete('sucesso');
        currentUrl.searchParams.delete('erro');
        window.history.replaceState({}, document.title, currentUrl.toString());
    }

    function renderStars(rating, container) {
        container.innerHTML = '';
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
            container.innerHTML += '<i class="far fa-star"></i>';
        }
    }

    function renderComments(comments) {
        commentsList.innerHTML = '';
        if (comments && comments.length > 0) {
            noCommentsMessage.classList.add('d-none');
            comments.forEach(comment => {
                const commentDiv = document.createElement('div');
                commentDiv.classList.add('comment-item');
                commentDiv.innerHTML = `
                    <p class="comment-author">${comment.nomeUsuario}</p>
                    <p class="comment-text">${comment.comentario}</p>
                `;
                commentsList.appendChild(commentDiv);
            });
        } else {
            noCommentsMessage.classList.remove('d-none');
        }
    }

    if (!newsId) {
        newsTitle.textContent = "Erro: ID da notícia não fornecido.";
        newsContent.innerHTML = `<p class="alert alert-danger">Por favor, forneça um ID de notícia válido na URL (ex: viewNoticia.html?id=1).</p>`;
        return;
    }

    const urlSuccessMessage = getQueryParam('sucesso');
    const urlErrorMessage = getQueryParam('erro');
    if (urlSuccessMessage) {
        displayMessage(urlSuccessMessage, 'success');
    } else if (urlErrorMessage) {
        displayMessage(urlErrorMessage, 'danger');
    }

    try {
        const newsResponse = await fetch(`${NEWS_API_URL}?id=${encodeURIComponent(newsId)}`);
        if (!newsResponse.ok) {
            const errorText = await newsResponse.text(); 
            throw new Error(`HTTP error! status: ${newsResponse.status} - ${newsResponse.statusText}. Resposta: ${errorText.substring(0, 200)}...`);
        }
        const newsJson = await newsResponse.json();
        console.log("Dados da notícia (listar-noticia) recebidos:", newsJson);

        let newsData = null;
        if (Array.isArray(newsJson) && newsJson.length > 0) {
            newsData = newsJson[0];
        } else if (newsJson && typeof newsJson === 'object' && newsJson.titulo) {
            newsData = newsJson;
        }

        if (!newsData || !newsData.titulo) {
            newsTitle.textContent = "Notícia não encontrada";
            newsContent.innerHTML = `<p class="alert alert-warning">A notícia com ID ${newsId} não foi encontrada ou os dados estão incompletos.</p>`;
            return;
        }

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

        ratingNewsIdInput.value = newsId;
        commentNewsIdInput.value = newsId;


        try {
            const ratingResponse = await fetch(`${RATING_API_URL}?idNoticia=${encodeURIComponent(newsId)}`);
            if (!ratingResponse.ok) {
                const errorText = await ratingResponse.text();
                console.error("Resposta de erro da avaliação (HTTP Status " + ratingResponse.status + "):", errorText.substring(0, 500) + "...");
                throw new Error(`Falha ao carregar avaliação: ${ratingResponse.status} - ${ratingResponse.statusText}. Provável HTML inesperado.`);
            }
            const avaliacaoData = await ratingResponse.json();
            console.log("Dados da avaliação (obter-avaliacao) recebidos:", avaliacaoData);

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

        try {
            const commentsResponse = await fetch(`${COMMENTS_API_URL}?idNoticia=${encodeURIComponent(newsId)}`);
            if (!commentsResponse.ok) {
                const errorText = await commentsResponse.text();
                console.error("Resposta de erro dos comentários (HTTP Status " + commentsResponse.status + "):", errorText.substring(0, 500) + "...");
                throw new Error(`Falha ao carregar comentários: ${commentsResponse.status} - ${commentsResponse.statusText}. Provável HTML inesperado.`);
            }
            const commentsData = await commentsResponse.json();
            console.log("Dados dos comentários (obter-comentarios) recebidos:", commentsData);
            renderComments(commentsData);

        } catch (commentsError) {
            console.error("Erro ao carregar os comentários da notícia:", commentsError);
            commentsList.innerHTML = `<p class="alert alert-danger">Erro ao carregar comentários: ${commentsError.message}</p>`;
            noCommentsMessage.classList.add('d-none');
        }


        const loginStatusResponse = await fetch(LOGIN_STATUS_API_URL);
        if (!loginStatusResponse.ok) {
            console.error("Erro ao verificar status de login:", loginStatusResponse.status, loginStatusResponse.statusText);
            userRatingSection.innerHTML = `<p class="alert alert-warning">Não foi possível verificar seu status de login para avaliação.</p>`;
            commentNotLoggedInMessage.classList.remove('d-none');
            commentForm.classList.add('d-none');
        } else {
            const loginStatusData = await loginStatusResponse.json();
            console.log("Status de Login (auth) recebido:", loginStatusData);

            if (loginStatusData.loggedIn) {
                notLoggedInMessage.classList.add('d-none');
                ratingForm.classList.remove('d-none');
                ratingForm.addEventListener('submit', (event) => {
                    if (!ratingForm.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    ratingForm.classList.add('was-validated');
                });

                commentNotLoggedInMessage.classList.add('d-none');
                commentForm.classList.remove('d-none');
                commentForm.addEventListener('submit', (event) => {
                    if (!commentForm.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    commentForm.classList.add('was-validated');
                });
                commentTextArea.value = '';

            } else {
                notLoggedInMessage.classList.remove('d-none');
                ratingForm.classList.add('d-none');

                commentNotLoggedInMessage.classList.remove('d-none');
                commentForm.classList.add('d-none');
            }

            if (loginStatusData.loggedIn && loginStatusData.userRole === 'admin') { 
                editLinkContainer.innerHTML = `
                    <a href="${CONTEXT_PATH}/editNoticia.html?id=${newsData.id}" class="btn btn-sm btn-warning mb-4">Editar</a>
                    <a href="${CONTEXT_PATH}/excluir-noticia?id=${newsData.id}" class="btn btn-sm btn-danger mb-4 ml-2" onclick="return confirm('Tem certeza que deseja excluir esta notícia?');">Excluir</a>
                `;
            }
        }

    } catch (error) {
        console.error("Erro geral ao carregar detalhes da notícia, avaliação ou comentários:", error);
        newsTitle.textContent = "Erro ao carregar notícia";
        newsContent.innerHTML = `<p class="alert alert-danger">Não foi possível carregar os detalhes da notícia. Por favor, tente novamente mais tarde. Detalhes: ${error.message}</p>`;
        userRatingSection.innerHTML = `<p class="alert alert-danger">Erro ao carregar formulário de avaliação: ${error.message}</p>`;
        commentsList.innerHTML = `<p class="alert alert-danger">Erro ao carregar comentários: ${error.message}</p>`;
        noCommentsMessage.classList.add('d-none');
    }
});