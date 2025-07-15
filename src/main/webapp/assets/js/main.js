console.log("Main.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', () => {
    const NEWS_API_URL = 'listar-noticia';

    const mainContentContainer = document.querySelector('.container.py-4');
    
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

    const searchForm = document.getElementById('search-form');
    const searchInput = document.getElementById('search-input');

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

    function renderCarousel(noticias) {
        const carouselIndicatorsContainer = document.getElementById('carousel-indicators-container');
        const carouselInnerContainer = document.getElementById('carousel-inner-container');
        if (!carouselIndicatorsContainer || !carouselInnerContainer) return;

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

        $('#carouselDestaque').carousel();
    }

    function renderRecentNews(noticias) {
        const recentNewsContainer = document.getElementById('recent-news-container');
        if (!recentNewsContainer) return;

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

    function renderCategorizedNews(noticias) {
        const categoriesContainer = document.getElementById('categories-container');
        if (!categoriesContainer) return;

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
        const newSearchInput = document.getElementById('search-input');

        document.getElementById('search-form').addEventListener('submit', (event) => {
            event.preventDefault();
            const newSearchTerm = newSearchInput.value;
            newSearchInput.value = ''; 
            window.location.href = `${window.location.pathname}?buscar=${encodeURIComponent(newSearchTerm)}`;
        });
        
        const urlParams = new URLSearchParams(window.location.search);
        const currentSearchTerm = urlParams.get('buscar');
        if (currentSearchTerm) {
            newSearchInput.value = currentSearchTerm;
        }


        if (noticias && noticias.length > 0) {
            searchResultsContainer.innerHTML = '';
            noticias.forEach(noticia => {
                searchResultsContainer.innerHTML += createNewsCard(noticia);
            });
            noResultsMessage.classList.add('d-none');
        } else {
            searchResultsContainer.innerHTML = '';
            noResultsMessage.classList.remove('d-none');
        }
    }

    async function loadNews() {
        const urlParams = new URLSearchParams(window.location.search);
        const searchTerm = urlParams.get('buscar');

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
                renderSearchResults(noticias);
            } else {
                if (!document.getElementById('carouselDestaque')) {
                    const searchBarHtml = mainContentContainer.querySelector('#search-form').outerHTML;
                    mainContentContainer.innerHTML = searchBarHtml + defaultSections;
                }
                renderCarousel(noticias);
                renderRecentNews(noticias);
                renderCategorizedNews(noticias);
            }

        } catch (error) {
            console.error("Erro ao carregar notícias:", error);
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
            document.getElementById('search-form').addEventListener('submit', (event) => {
                event.preventDefault();
                const newSearchInput = document.getElementById('search-input');
                const newSearchTerm = newSearchInput.value;
                newSearchInput.value = '';
                window.location.href = `${window.location.pathname}?buscar=${encodeURIComponent(newSearchTerm)}`;
            });
        }
    }

    searchForm.addEventListener('submit', (event) => {
        event.preventDefault();
        const searchTerm = searchInput.value;
        searchInput.value = ''; 
        window.location.href = `${window.location.pathname}?buscar=${encodeURIComponent(searchTerm)}`;
    });

    loadNews();
});