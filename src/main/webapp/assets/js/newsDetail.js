console.log("newsDetail.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', async () => {
    const NEWS_API_URL = 'listar-noticia';
    // Mude o endpoint para o LoginServlet
    const LOGIN_STATUS_API_URL = 'auth'; 

    const newsTitle = document.getElementById('news-title');
    const newsAuthor = document.getElementById('news-author');
    const newsDate = document.getElementById('news-date');
    const newsCategory = document.getElementById('news-category');
    const newsViews = document.getElementById('news-views');
    const newsImageContainer = document.getElementById('news-image-container');
    const newsContent = document.getElementById('news-content');
    const editLinkContainer = document.getElementById('edit-link-container');

    function getQueryParam(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }

    const newsId = getQueryParam('id');

    if (!newsId) {
        newsTitle.textContent = "Erro: ID da notícia não fornecido.";
        newsContent.innerHTML = `<p class="alert alert-danger">Por favor, forneça um ID de notícia válido na URL (ex: viewNoticia.html?id=1).</p>`;
        return;
    }

    try {
        // 1. Carregar os dados da notícia
        const newsResponse = await fetch(`${NEWS_API_URL}?id=${encodeURIComponent(newsId)}`);
        if (!newsResponse.ok) {
            throw new Error(`HTTP error! status: ${newsResponse.status} - ${newsResponse.statusText}`);
        }
        const newsData = await newsResponse.json();

        if (newsData.message) {
            newsTitle.textContent = "Erro ao carregar notícia";
            newsContent.innerHTML = `<p class="alert alert-danger">${newsData.message}</p>`;
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

        // 2. Verificar o status de login para exibir o botão "Editar"
        const loginStatusResponse = await fetch(LOGIN_STATUS_API_URL);
        if (!loginStatusResponse.ok) {
            console.error("Erro ao verificar status de login:", loginStatusResponse.status, loginStatusResponse.statusText);
        } else {
            const loginStatusData = await loginStatusResponse.json();
            if (loginStatusData.loggedIn) {
                editLinkContainer.innerHTML = `
                    <a href="editNoticia.html?id=${newsData.id}" class="btn btn-sm btn-warning mb-4">Editar</a>
                    <a href="excluir-noticia?id=${newsData.id}" class="btn btn-sm btn-danger mb-4 ml-2" onclick="return confirm('Tem certeza que deseja excluir esta notícia?');">Excluir</a>
                `;
            }
        }

    } catch (error) {
        console.error("Erro ao carregar detalhes da notícia:", error);
        newsTitle.textContent = "Erro ao carregar notícia";
        newsContent.innerHTML = `<p class="alert alert-danger">Não foi possível carregar os detalhes da notícia. Por favor, tente novamente mais tarde. Detalhes: ${error.message}</p>`;
    }
});