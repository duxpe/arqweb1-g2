console.log("Main.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', () => {
    const NEWS_API_URL = 'listar-noticia'; // **VOCÊ PRECISA CRIAR ESTE ENDPOINT NO SEU BACKEND JAVA**
                                     // Este endpoint deve retornar todas as notícias em formato JSON.
                                     // Ex: um Servlet que responde a /api/news com um JSON de List<Noticia>.
                                     // Exemplo de retorno JSON:
                                     // [{"id":1, "titulo":"Notícia 1", "resumo":"...", "dataPublicacao":"2023-01-01", "nomeAutor":"Autor A", "imagem":"base64string"}, ...]

    const carouselIndicatorsContainer = document.getElementById('carousel-indicators-container');
    const carouselInnerContainer = document.getElementById('carousel-inner-container');
    const recentNewsContainer = document.getElementById('recent-news-container');
    const categoriesContainer = document.getElementById('categories-container');
    const searchForm = document.getElementById('search-form');
    const searchInput = document.getElementById('search-input');

    // Função auxiliar para criar o HTML de um card de notícia
    function createNewsCard(noticia) {
        const imageUrl = noticia.imagem ? `data:image/png;base64,${noticia.imagem}` : '';
        const imageHtml = noticia.imagem ?
            `<img src="${imageUrl}" class="card-img-fixed" alt="${noticia.titulo}" />` :
            `<div class="card-img-fixed bg-secondary"></div>`;

        return `
            <div class="col-md-4 mb-4">
                <div class="card h-100">
                    ${imageHtml}
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">${noticia.titulo}</h5>
                        <p class="card-text flex-grow-1">${noticia.resumo}</p>
                        <small class="text-muted mb-2">
                            ${noticia.dataPublicacao} • ${noticia.nomeAutor}
                        </small>
                        <a href="viewNoticia.html?id=${noticia.id}" class="btn btn-primary mt-auto">Leia Mais</a>
                    </div>
                </div>
            </div>
        `;
    }

    // Função para renderizar o carrossel
    function renderCarousel(noticias) {
        carouselIndicatorsContainer.innerHTML = '';
        carouselInnerContainer.innerHTML = '';

        noticias.forEach((noticia, index) => {
            const isActive = index === 0 ? 'active' : '';
            const imageUrl = noticia.imagem ? `data:image/png;base64,${noticia.imagem}` : '';
            const imageHtml = noticia.imagem ?
                `<img src="${imageUrl}" class="d-block w-100" alt="${noticia.titulo}" />` :
                `<div class="d-block w-100 bg-secondary" style="height: 400px;"></div>`;

            carouselIndicatorsContainer.innerHTML += `
                <li data-target="#carouselDestaque" data-slide-to="${index}" class="${isActive}"></li>
            `;

            carouselInnerContainer.innerHTML += `
                <div class="carousel-item ${isActive}">
                    ${imageHtml}
                    <div class="carousel-caption text-left">
                        <h5 class="text-white">${noticia.titulo}</h5>
                        <p class="text-white-50">${noticia.resumo}</p>
                        <a href="viewNoticia.html?id=${noticia.id}" class="btn btn-sm btn-primary">Leia Mais</a>
                    </div>
                </div>
            `;
        });

        // Inicializa o carrossel do Bootstrap após renderizar
        $('#carouselDestaque').carousel();
    }

    // Função para renderizar as notícias recentes (apenas as 3 mais recentes)
    function renderRecentNews(noticias) {
        recentNewsContainer.innerHTML = '';
        // Classifica as notícias por data de publicação (mais recente primeiro)
        const sortedNews = [...noticias].sort((a, b) => new Date(b.dataPublicacao) - new Date(a.dataPublicacao));
        const recentThree = sortedNews.slice(0, 3);
        recentThree.forEach(noticia => {
            recentNewsContainer.innerHTML += createNewsCard(noticia);
        });
    }

    // Função para renderizar notícias por categoria
    function renderCategorizedNews(noticias) {
        categoriesContainer.innerHTML = '';
        const categoriesMap = noticias.reduce((acc, noticia) => {
            const category = noticia.categoria || 'Sem Categoria'; // Garante que a categoria existe
            if (!acc[category]) {
                acc[category] = [];
            }
            acc[category].push(noticia);
            return acc;
        }, {});

        for (const category in categoriesMap) {
            categoriesContainer.innerHTML += `
                <h4 class="mt-5 mb-3">${category}</h4>
                <div class="row">
                    ${categoriesMap[category].map(n => createNewsCard(n)).join('')}
                </div>
            `;
        }
    }

    // Função principal para buscar e renderizar todas as notícias
    async function loadNews(searchTerm = '') {
        try {
            let url = NEWS_API_URL;
            if (searchTerm) {
                // Se houver termo de busca, envie como parâmetro de query
                url += `?buscar=${encodeURIComponent(searchTerm)}`;
            }

            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const noticias = await response.json();

            // Renderiza as seções da página com base nos dados
            renderCarousel(noticias);
            renderRecentNews(noticias);
            renderCategorizedNews(noticias);

        } catch (error) {
            console.error("Erro ao carregar notícias:", error);
            // Poderia exibir uma mensagem de erro na UI
            categoriesContainer.innerHTML = `<p class="alert alert-danger">Erro ao carregar notícias. Por favor, tente novamente mais tarde.</p>`;
        }
    }

    // Event Listener para o formulário de busca
    searchForm.addEventListener('submit', (event) => {
        event.preventDefault(); // Impede o envio padrão do formulário
        const searchTerm = searchInput.value;
        loadNews(searchTerm); // Recarrega as notícias com o termo de busca
    });

    // Carrega as notícias quando a página é carregada
    loadNews();
});