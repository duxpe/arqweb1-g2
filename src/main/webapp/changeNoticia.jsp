<%@include file="header.jsp"%>
<%@ include file="menu.jsp" %>


<div class="container py-5">
    <h2 class="text-center mb-4">Editar Notícia</h2>
    <form action="editar-noticia" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
        <input type="hidden" name="id" value="${noticia.id}" />

        <div class="mb-3">
            <label for="titulo" class="form-label">Título</label>
            <input type="text" class="form-control" id="titulo" name="titulo" value="${noticia.titulo}" required>
        </div>

        <div class="mb-3">
            <label for="autor" class="form-label">Autor</label>
            <input type="text" class="form-control" id="autor" name="autor" value="${noticia.autor}" required>
        </div>

        <div class="mb-3">
            <label for="categoria" class="form-label">Categoria</label>
            <select class="form-select" name="categoria" required>
                <option ${noticia.categoria == 'Política' ? 'selected' : ''}>Política</option>
                <option ${noticia.categoria == 'Esportes' ? 'selected' : ''}>Esportes</option>
                <option ${noticia.categoria == 'Tecnologia' ? 'selected' : ''}>Tecnologia</option>
            </select>
        </div>

        <div class="mb-3">
            <label for="resumo" class="form-label">Resumo</label>
            <textarea class="form-control" name="resumo" rows="3">${noticia.resumo}</textarea>
        </div>

        <div class="mb-3">
            <label for="conteudo" class="form-label">Conteúdo</label>
            <textarea class="form-control" name="conteudo" rows="8">${noticia.conteudo}</textarea>
        </div>

        <div class="mb-3">
            <label for="imagem" class="form-label">Alterar Imagem de Destaque</label>
            <input type="file" class="form-control" name="imagem">
        </div>

        <button type="submit" class="btn btn-primary">Atualizar</button>
        <a href="minhasNoticias.jsp" class="btn btn-secondary">Cancelar</a>
    </form>
</div>

<%@include file="footer.jsp"%>
