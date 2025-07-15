console.log("editNews.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', async () => {
    const NEWS_API_URL = 'listar-noticia';
    const editNewsForm = document.getElementById('edit-news-form');

    const newsIdInput = document.getElementById('news-id');
    const tituloInput = document.getElementById('titulo');
    const nomeAutorInput = document.getElementById('nomeAutor');
    const categoriaSelect = document.getElementById('categoria');
    const dataPublicacaoInput = document.getElementById('dataPublicacao');
    const resumoTextarea = document.getElementById('resumo');
    const conteudoTextarea = document.getElementById('conteudo');
    const currentImageContainer = document.getElementById('current-image-container');
    const cancelButton = document.getElementById('cancel-button');

    function getQueryParam(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }

    const newsId = getQueryParam('id');

    if (!newsId) {
        alert("Erro: ID da notícia não fornecido. Redirecionando para a lista de notícias.");
        window.location.href = 'listar-noticia';
        return;
    }

    cancelButton.href = `viewNoticia.html?id=${newsId}`;

    try {
        const response = await fetch(`${NEWS_API_URL}?id=${encodeURIComponent(newsId)}`);

        if (!response.ok) {
            if (response.status === 404) {
                 alert("Notícia não encontrada. Redirecionando para a lista de notícias.");
                 window.location.href = 'listar-noticia';
            } else {
                 throw new Error(`HTTP error! status: ${response.status} - ${response.statusText}`);
            }
        }

        const newsData = await response.json();

        if (newsData.message) {
            alert(`Erro: ${newsData.message}. Redirecionando para a lista de notícias.`);
            window.location.href = 'listar-noticia';
            return;
        }

        newsIdInput.value = newsData.id;
        tituloInput.value = newsData.titulo;
        nomeAutorInput.value = newsData.nomeAutor;
        categoriaSelect.value = newsData.categoria;
        dataPublicacaoInput.value = newsData.dataPublicacao; 
        resumoTextarea.value = newsData.resumo;
        conteudoTextarea.value = newsData.conteudo;

        if (newsData.imagem) {
            const currentImageDiv = document.createElement('div');
            currentImageDiv.classList.add('mb-3');
            currentImageDiv.innerHTML = `
                <label class="form-label">Imagem Atual</label><br>
                <img src="data:image/png;base64,${newsData.imagem}" alt="Imagem da notícia"
                    class="img-thumbnail" />
            `;
            currentImageContainer.appendChild(currentImageDiv);
        }

    } catch (error) {
        console.error("Erro ao carregar dados da notícia para edição:", error);
        alert("Erro ao carregar os dados da notícia. Por favor, tente novamente mais tarde.");
        window.location.href = 'listar-noticia';
    }

    editNewsForm.addEventListener('submit', (event) => {
        if (!editNewsForm.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        editNewsForm.classList.add('was-validated');
    }, false);
});