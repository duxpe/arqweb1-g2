console.log("Main.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', () => {
    const NEWS_API_URL = 'listar-noticia'; // Seu endpoint que retorna notícias

    const mainContentContainer = document.querySelector('.container.py-4'); // Seleciona o container principal do seu HTML
    
    // Novas referências para as seções que queremos ocultar/mostrar
    const defaultSections = `
        <div id="carouselDestaque" class="carousel slide mb-5" data-ride="carousel">
            <ol class="carousel-indicators" id="carousel-indicators-container"></ol>
            <div class="carousel-inner" id="carousel-inner-container"></div>
            <a class="carousel-control-prev" href="#carouselDestaque" role="button" data-slide="prev">
                <span class="carousel-control-prev-icon"></span> <span class="sr-only">Anterior</span>
            </a>
            <a class="carousel-control-next" href="#carouselDestaque" role="button" data-slide="next">
                <span class="carousel-control-next-icon"></span> <span class="sr-only">Próximo</span>
            </a>
        </div>
        <h3 class="mt-5 mb-3">Notícias Mais Recentes</h3>
        <div class="row" id="recent-news-container"></div>
        <div id="categories-container"></div>
    `;

    // A barra de busca e o formulário devem sempre estar visíveis
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
        const carouselIndicatorsContainer = document.getElementById('carousel-indicators-container');
        const carouselInnerContainer = document.getElementById('carousel-inner-container');
        if (!carouselIndicatorsContainer || !carouselInnerContainer) return; // Garante que os elementos existem

        carouselIndicatorsContainer.innerHTML = '';
        carouselInnerContainer.innerHTML = '';

        if (noticias.length === 0) {
            carouselInnerContainer.innerHTML = `<div class="carousel-item active"><div class="d-block w-100 bg-light text-center py-5">Nenhuma notícia em destaque.</div></div>`;
            return;
        }

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
        const recentNewsContainer = document.getElementById('recent-news-container');
        if (!recentNewsContainer) return; // Garante que o elemento existe

        recentNewsContainer.innerHTML = '';
        const sortedNews = [...noticias].sort((a, b) => new Date(b.dataPublicacao) - new Date(a.dataPublicacao));
        const recentThree = sortedNews.slice(0, 3);
        
        if (recentThree.length === 0) {
            recentNewsContainer.innerHTML = `<div class="col-12"><p class="text-muted">Nenhuma notícia recente para exibir.</p></div>`;
            return;
        }
        
        recentThree.forEach(noticia => {
            recentNewsContainer.innerHTML += createNewsCard(noticia);
        });
    }

    // Função para renderizar notícias por categoria
    function renderCategorizedNews(noticias) {
        const categoriesContainer = document.getElementById('categories-container');
        if (!categoriesContainer) return; // Garante que o elemento existe

        categoriesContainer.innerHTML = '';
        if (noticias.length === 0) {
            categoriesContainer.innerHTML = `<div class="col-12"><p class="text-muted">Nenhuma categoria de notícia para exibir.</p></div>`;
            return;
        }

        const categoriesMap = noticias.reduce((acc, noticia) => {
            const category = noticia.categoria || 'Sem Categoria';
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

    // NOVA FUNÇÃO: Renderizar apenas resultados de busca
    function renderSearchResults(noticias) {
        mainContentContainer.innerHTML = `
            <form id="search-form" class="mb-4">
                <div class="input-group">
                    <input type="text" name="buscar" id="search-input" class="form-control"
                        placeholder="Buscar notícias por título, autor ou categoria" />
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="submit">
                            Buscar</button>
                    </div>
                </div>
            </form>
            <h3 class="mt-5 mb-3">Resultados da Busca</h3>
            <div class="row" id="search-results-container"></div>
            <div id="no-results-message" class="alert alert-info mt-3 d-none">Nenhuma notícia encontrada para sua busca.</div>
        `;
        
        const searchResultsContainer = document.getElementById('search-results-container');
        const noResultsMessage = document.getElementById('no-results-message');
        const newSearchInput = document.getElementById('search-input'); // Referência ao novo input de busca

        // Re-atribui o evento de submit para o novo formulário de busca
        document.getElementById('search-form').addEventListener('submit', (event) => {
            event.preventDefault();
            const newSearchTerm = newSearchInput.value;
            // Limpa o conteúdo do input para evitar duplicação do termo na URL
            newSearchInput.value = ''; 
            // Redireciona para a URL com o novo termo de busca
            window.location.href = `${window.location.pathname}?buscar=${encodeURIComponent(newSearchTerm)}`;
        });
        
        // Preenche o input de busca com o termo atual (se existir na URL)
        const urlParams = new URLSearchParams(window.location.search);
        const currentSearchTerm = urlParams.get('buscar');
        if (currentSearchTerm) {
            newSearchInput.value = currentSearchTerm;
        }


        if (noticias && noticias.length > 0) {
            searchResultsContainer.innerHTML = ''; // Limpa antes de adicionar
            noticias.forEach(noticia => {
                searchResultsContainer.innerHTML += createNewsCard(noticia);
            });
            noResultsMessage.classList.add('d-none'); // Oculta a mensagem
        } else {
            searchResultsContainer.innerHTML = '';
            noResultsMessage.classList.remove('d-none'); // Mostra a mensagem de "nenhum resultado"
        }
    }

    // Função principal para buscar e renderizar todas as notícias
    async function loadNews() {
        const urlParams = new URLSearchParams(window.location.search);
        const searchTerm = urlParams.get('buscar'); // Pega o termo de busca da URL

        try {
            let url = NEWS_API_URL;
            if (searchTerm) {
                url += `?buscar=${encodeURIComponent(searchTerm)}`;
            }

            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const noticias = await response.json();

            if (searchTerm) {
                // Se houver termo de busca na URL, renderiza apenas os resultados
                renderSearchResults(noticias);
            } else {
                // Se não houver termo de busca, renderiza a página inicial completa
                // Garante que o conteúdo padrão está presente no DOM
                if (!document.getElementById('carouselDestaque')) {
                    const searchBarHtml = mainContentContainer.querySelector('#search-form').outerHTML; // Salva a barra de busca
                    mainContentContainer.innerHTML = searchBarHtml + defaultSections; // Restaura as seções padrão
                    // Re-captura as referências, pois o HTML foi reescrito
                    // Essas referências não são usadas diretamente aqui, mas são importantes se fossem.
                    // const carouselIndicatorsContainer = document.getElementById('carousel-indicators-container');
                    // const carouselInnerContainer = document.getElementById('carousel-inner-container');
                    // const recentNewsContainer = document.getElementById('recent-news-container');
                    // const categoriesContainer = document.getElementById('categories-container');
                }
                renderCarousel(noticias);
                renderRecentNews(noticias);
                renderCategorizedNews(noticias);
            }

        } catch (error) {
            console.error("Erro ao carregar notícias:", error);
            // Poderia exibir uma mensagem de erro na UI
            mainContentContainer.innerHTML = `
                <form id="search-form" class="mb-4">
                    <div class="input-group">
                        <input type="text" name="buscar" id="search-input" class="form-control"
                            placeholder="Buscar notícias por título, autor ou categoria" />
                        <div class="input-group-append">
                            <button class="btn btn-outline-secondary" type="submit">
                                Buscar</button>
                        </div>
                    </div>
                </form>
                <div class="alert alert-danger mt-3">Erro ao carregar notícias. Por favor, tente novamente mais tarde.</div>
                <div class="row" id="search-results-container"></div> `;
            // Re-atribui o evento de submit ao formulário de busca restaurado
            document.getElementById('search-form').addEventListener('submit', (event) => {
                event.preventDefault();
                const newSearchInput = document.getElementById('search-input');
                const newSearchTerm = newSearchInput.value;
                newSearchInput.value = '';
                window.location.href = `${window.location.pathname}?buscar=${encodeURIComponent(newSearchTerm)}`;
            });
        }
    }

    // Event Listener para o formulário de busca
    // Este listener agora só redireciona a página para incluir o termo de busca na URL
    searchForm.addEventListener('submit', (event) => {
        event.preventDefault(); // Impede o envio padrão do formulário
        const searchTerm = searchInput.value;
        // Limpa o conteúdo do input para evitar duplicação do termo na URL
        searchInput.value = ''; 
        // Redireciona para a URL com o termo de busca
        window.location.href = `${window.location.pathname}?buscar=${encodeURIComponent(searchTerm)}`;
    });

    // Carrega as notícias quando a página é carregada
    loadNews();
});