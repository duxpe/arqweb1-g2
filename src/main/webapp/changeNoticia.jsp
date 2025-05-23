<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<div class="container py-5">
    <h2 class="text-center mb-4">Editar Notícia</h2>

    <form action="${pageContext.request.contextPath}/editar-noticia"
          method="post"
          enctype="multipart/form-data"
          class="needs-validation"
          novalidate>
        <input type="hidden" name="id" value="${noticiaSelecionada.id}" />
        <div class="mb-3">
            <label for="titulo" class="form-label">Título</label>
            <input type="text"
                   class="form-control"
                   id="titulo"
                   name="titulo"
                   value="${noticiaSelecionada.titulo}"
                   required>
        </div>
        <div class="mb-3">
            <label for="nomeAutor" class="form-label">Autor</label>
            <input type="text"
                   class="form-control"
                   id="nomeAutor"
                   name="nomeAutor"
                   value="${noticiaSelecionada.nomeAutor}"
                   required>
        </div>
        <div class="mb-3">
            <label for="categoria" class="form-label">Categoria</label>
            <select class="form-select"
                    id="categoria"
                    name="categoria"
                    required>
                <option value="Política"
                    ${noticiaSelecionada.categoria == 'Política' ? 'selected' : ''}>
                    Política
                </option>
                <option value="Esportes"
                    ${noticiaSelecionada.categoria == 'Esportes' ? 'selected' : ''}>
                    Esportes
                </option>
                <option value="Tecnologia"
                    ${noticiaSelecionada.categoria == 'Tecnologia' ? 'selected' : ''}>
                    Tecnologia
                </option>
            </select>
        </div>

        <div class="mb-3">
            <label for="dataPublicacao" class="form-label">Data de Publicação</label>
            <input type="date"
                   class="form-control"
                   id="dataPublicacao"
                   name="dataPublicacao"
                   value="${noticiaSelecionada.dataPublicacao}"
                   required>
        </div>
        <div class="mb-3">
            <label for="resumo" class="form-label">Resumo</label>
            <textarea class="form-control"
                      id="resumo"
                      name="resumo"
                      rows="3"
                      required>${noticiaSelecionada.resumo}</textarea>
        </div>

        <div class="mb-3">
            <label for="conteudo" class="form-label">Conteúdo</label>
            <textarea class="form-control"
                      id="conteudo"
                      name="conteudo"
                      rows="8"
                      required>${noticiaSelecionada.conteudo}</textarea>
        </div>

        <!-- Exemplo de como ler imagem :) 
        <div class="mb-3">
            <label for="imagem" class="form-label">Alterar Imagem de Destaque</label>
            <input type="file"
                   class="form-control"
                   id="imagem"
                   name="imagem">
        </div> -->

        <button type="submit" class="btn btn-primary">Atualizar</button>
        <a href="${pageContext.request.contextPath}/listar-noticia"
           class="btn btn-secondary">Cancelar</a>
    </form>
</div>

<%@ include file="footer.jsp" %>
