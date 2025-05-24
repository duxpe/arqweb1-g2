<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.Base64" %>
<%@ page import="br.edu.ifsp.arq.g2.model.Noticia" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<style>
  .card-img-fixed {
    width: 100%;
    height: 180px;
    object-fit: cover;
  }
</style>

<div class="container py-4">
  <h2 class="mb-4">Notícias em Destaque</h2>
  <div class="row">
    <c:forEach var="noticia" items="${noticias}">
      <div class="col-md-4 mb-4">
        <div class="card h-100">
          <c:if test="${noticia.imagem ne null}">
            <% 
              Noticia n = (Noticia) pageContext.getAttribute("noticia");
              byte[] img = n.getImagem();
              String base64 = Base64.getEncoder().encodeToString(img);
            %>
            <img src="data:image/png;base64,<%= base64 %>"
                 class="card-img-fixed"
                 alt="Imagem de <%= n.getTitulo() %>"/>
          </c:if>
          <c:if test="${noticia.imagem == null}">
            <div class="card-img-fixed bg-secondary"></div>
          </c:if>

          <div class="card-body d-flex flex-column">
            <h5 class="card-title">${noticia.titulo}</h5>
            <p class="card-text flex-grow-1">${noticia.resumo}</p>
            <a href="${pageContext.request.contextPath}/listar-noticia?id=${noticia.id}"
               class="btn btn-primary mt-2">Leia Mais</a>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>

  <h3 class="mt-5">Categorias Populares</h3>
  <div>
    <c:forEach var="categoria" items="${categoriasPopulares}">
      <a href="${pageContext.request.contextPath}/buscar-categoria?nome=${categoria}"
         class="badge bg-secondary me-1">${categoria}</a>
    </c:forEach>
  </div>
</div>

<%@ include file="footer.jsp" %>
