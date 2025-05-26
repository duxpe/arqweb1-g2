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
  <h2 class="mb-4">
    Resultados da busca por “<c:out value="${param.buscar}"/>”
  </h2>

  <c:if test="${empty resultadoBusca}">
    <div class="alert alert-warning">
      Nenhum resultado encontrado.
    </div>
  </c:if>

  <div class="row">
    <c:forEach var="noticia" items="${resultadoBusca}">
      <div class="col-md-4 mb-4">
        <div class="card h-100">
          <c:choose>
            <c:when test="${noticia.imagem ne null}">
              <% 
                Noticia n = (Noticia) pageContext.getAttribute("noticia");
                byte[] img = n.getImagem();
                String b64 = Base64.getEncoder().encodeToString(img);
              %>
              <img src="data:image/png;base64,<%= b64 %>"
                   class="card-img-fixed"
                   alt="${noticia.titulo}"/>
            </c:when>
            <c:otherwise>
              <div class="card-img-fixed bg-secondary"></div>
            </c:otherwise>
          </c:choose>
          <div class="card-body d-flex flex-column">
            <h5 class="card-title">${noticia.titulo}</h5>
            <p class="card-text flex-grow-1">${noticia.resumo}</p>
            <small class="text-muted mb-2">
              ${noticia.dataPublicacao} • ${noticia.nomeAutor} • ${noticia.visualizacoes} visualizações
            </small>
            <a href="${pageContext.request.contextPath}/listar-noticia?id=${noticia.id}"
               class="btn btn-primary mt-auto">Leia Mais</a>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>
</div>

<%@ include file="footer.jsp" %>
