<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<div class="container py-4">
    <h2 class="mb-4">Notícias em Destaque</h2>
    <div class="row">
        <c:forEach var="noticia" items="${noticias}">
            <div class="col-md-4 mb-4">
                <div class="card h-100">
                    <div class="card-body">
                        <h5 class="card-title">${noticia.titulo}</h5>
                        <p class="card-text">${noticia.texto}</p>
                        <a href="${pageContext.request.contextPath}/listar-noticia?id=${noticia.id}"
                           class="btn btn-primary">Leia Mais</a>
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
