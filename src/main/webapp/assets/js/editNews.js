console.log("editNews.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', async () => {
    const NEWS_API_URL = 'listar-noticia'; // Endpoint para buscar a notícia
    const editNewsForm = document.getElementById('edit-news-form');

    // Mapeamento dos elementos do formulário
    const newsIdInput = document.getElementById('news-id');
    const tituloInput = document.getElementById('titulo');
    const nomeAutorInput = document.getElementById('nomeAutor');
    const categoriaSelect = document.getElementById('categoria');
    const dataPublicacaoInput = document.getElementById('dataPublicacao');
    const resumoTextarea = document.getElementById('resumo');
    const conteudoTextarea = document.getElementById('conteudo');
    const currentImageContainer = document.getElementById('current-image-container');
    const cancelButton = document.getElementById('cancel-button'); // Botão Cancelar

    // Função para extrair parâmetros da URL
    function getQueryParam(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }

    const newsId = getQueryParam('id');

    if (!newsId) {
        alert("Erro: ID da notícia não fornecido. Redirecionando para a lista de notícias.");
        window.location.href = 'listar-noticia'; // Redireciona para a lista se não houver ID
        return;
    }

    // Atualiza o href do botão Cancelar
    cancelButton.href = `viewNoticia.html?id=${newsId}`;

    try {
        // Busca a notícia existente pelo ID
        const response = await fetch(`${NEWS_API_URL}?id=${encodeURIComponent(newsId)}`);

        if (!response.ok) {
            // Se a notícia não for encontrada ou houver outro erro HTTP
            if (response.status === 404) {
                 alert("Notícia não encontrada. Redirecionando para a lista de notícias.");
                 window.location.href = 'listar-noticia';
            } else {
                 throw new Error(`HTTP error! status: ${response.status} - ${response.statusText}`);
            }
        }

        const newsData = await response.json();

        // Se o backend retornar uma mensagem de erro (ex: notícia não encontrada)
        if (newsData.message) {
            alert(`Erro: ${newsData.message}. Redirecionando para a lista de notícias.`);
            window.location.href = 'listar-noticia';
            return;
        }

        // Preenche o formulário com os dados da notícia
        newsIdInput.value = newsData.id;
        tituloInput.value = newsData.titulo;
        nomeAutorInput.value = newsData.nomeAutor;
        categoriaSelect.value = newsData.categoria;
        // Formata a data para o formato YYYY-MM-DD para input[type="date"]
        dataPublicacaoInput.value = newsData.dataPublicacao; 
        resumoTextarea.value = newsData.resumo;
        conteudoTextarea.value = newsData.conteudo;

        // Exibe a imagem atual se existir
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
        window.location.href = 'listar-noticia'; // Redireciona em caso de erro grave
    }

    // Lógica de validação do formulário Bootstrap
    editNewsForm.addEventListener('submit', (event) => {
        if (!editNewsForm.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        editNewsForm.classList.add('was-validated');
    }, false);
});