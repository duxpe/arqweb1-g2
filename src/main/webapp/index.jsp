<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.util.Base64"%>
<%@ page import="br.edu.ifsp.arq.g2.model.Noticia"%>
<%@ page
	import="java.util.List, java.util.ArrayList, java.util.Collections, java.util.Comparator"%>
<%@ page
	import="java.util.Map, java.util.LinkedHashMap, br.edu.ifsp.arq.g2.model.Noticia"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
@SuppressWarnings("unchecked")
List<Noticia> orig = (List<Noticia>) session.getAttribute("noticias");
List<Noticia> sorted = new ArrayList<>(orig);
Collections.sort(sorted, new Comparator<Noticia>() {
	public int compare(Noticia a, Noticia b) {
		return b.getDataPublicacao().compareTo(a.getDataPublicacao());
	}
});
request.setAttribute("noticiasRecentes", sorted);
%>

<%
@SuppressWarnings("unchecked")
List<Noticia> todas = (List<Noticia>) session.getAttribute("noticias");
Map<String, List<Noticia>> categoriasMap = new LinkedHashMap<>();
for (Noticia n : todas) {
	String cat = n.getCategoria();
	categoriasMap.computeIfAbsent(cat, k -> new ArrayList<>()).add(n); //isso aqui é bacana (https://www.w3schools.com/java/ref_hashmap_computeifabsent.asp)
}
request.setAttribute("categoriasMap", categoriasMap);
%>

<%@ include file="header.jsp"%>
<%@ include file="menu.jsp"%>


<style>
.carousel-item img {
	height: 400px;
	object-fit: cover;
}

.card-img-fixed {
	width: 100%;
	height: 200px;
	object-fit: cover;
}

.carousel-caption {
    background: rgba(0,0,0,0.5);
    padding: 0.5rem;
}
</style>

<div class="container py-4">

	<form method="get"
		action="${pageContext.request.contextPath}/listar-noticia"
		class="mb-4">
		<div class="input-group">
			<input type="text" name="buscar" class="form-control"
				placeholder="Buscar notícias por título, autor ou categoria" />
			<div class="input-group-append">
				<button class="btn btn-outline-secondary" type="submit">
					Buscar</button>
			</div>
		</div>
	</form>

	<div id="carouselDestaque" class="carousel slide mb-5"
		data-ride="carousel">
		<ol class="carousel-indicators">
			<c:forEach var="noticia" items="${noticias}" varStatus="sts">
				<li data-target="#carouselDestaque" data-slide-to="${sts.index}"
					class="${sts.first ? 'active' : ''}"></li>
			</c:forEach>
		</ol>

		<div class="carousel-inner">
			<c:forEach var="noticia" items="${noticias}" varStatus="sts">
				<div class="carousel-item ${sts.first ? 'active' : ''}">
					<c:choose>
						<c:when test="${noticia.imagem ne null}">
							<%
							Noticia n = (Noticia) pageContext.getAttribute("noticia");
							byte[] img = n.getImagem();
							String b64 = Base64.getEncoder().encodeToString(img);
							%>
							<img src="data:image/png;base64,<%= b64 %>" class="d-block w-100"
								alt="${noticia.titulo}" />
						</c:when>
						<c:otherwise>
							<div class="d-block w-100 bg-secondary" style="height: 400px;"></div>
						</c:otherwise>
					</c:choose>

					 <div class="carousel-caption text-left">
						<h5 class="text-white">${noticia.titulo}</h5>
						<p class="text-white-50">${noticia.resumo}</p>
						<a
							href="${pageContext.request.contextPath}/listar-noticia?id=${noticia.id}"
							class="btn btn-sm btn-primary">Leia Mais</a>
					</div>
				</div>
			</c:forEach>
		</div>

		<a class="carousel-control-prev" href="#carouselDestaque"
			role="button" data-slide="prev"> <span
			class="carousel-control-prev-icon"></span> <span class="sr-only">Anterior</span>
		</a> <a class="carousel-control-next" href="#carouselDestaque"
			role="button" data-slide="next"> <span
			class="carousel-control-next-icon"></span> <span class="sr-only">Próximo</span>
		</a>
	</div>

	<h3 class="mt-5 mb-3">Notícias Mais Recentes</h3>
	<div class="row">
		<c:forEach var="noticiaRecente" items="${noticiasRecentes}"
			varStatus="sts" begin="0" end="2">
			<div class="col-md-4 mb-4">
				<div class="card h-100">
					<c:choose>
						<c:when test="${noticiaRecente.imagem ne null}">
							<%
							br.edu.ifsp.arq.g2.model.Noticia temp = (br.edu.ifsp.arq.g2.model.Noticia) pageContext.getAttribute("noticiaRecente");
							byte[] img = temp.getImagem();
							String b64 = java.util.Base64.getEncoder().encodeToString(img);
							%>
							<img src="data:image/png;base64,<%= b64 %>"
								class="card-img-fixed" alt="${noticiaRecente.titulo}" />
						</c:when>
						<c:otherwise>
							<div class="card-img-fixed bg-secondary"></div>
						</c:otherwise>
					</c:choose>
					<div class="card-body d-flex flex-column">
						<h5 class="card-title">${noticiaRecente.titulo}</h5>
						<p class="card-text flex-grow-1">${noticiaRecente.resumo}</p>
						<small class="text-muted mb-2">
							${noticiaRecente.dataPublicacao} • ${noticiaRecente.nomeAutor} </small> <a
							href="${pageContext.request.contextPath}/listar-noticia?id=${noticiaRecente.id}"
							class="btn btn-primary mt-auto">Leia Mais</a>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>

	<c:forEach var="categoria" items="${categoriasMap.keySet()}">
		<h4 class="mt-5 mb-3">${categoria}</h4>
		<div class="row">
			<c:forEach var="noticia" items="${categoriasMap[categoria]}">
				<div class="col-md-4 mb-4">
					<div class="card h-100">
						<c:choose>
							<c:when test="${noticia.imagem ne null}">
								<%
								Noticia imgN = (Noticia) pageContext.getAttribute("noticia");
								byte[] bytes = imgN.getImagem();
								String b64 = java.util.Base64.getEncoder().encodeToString(bytes);
								%>
								<img src="data:image/png;base64,<%= b64 %>"
									class="card-img-fixed" alt="${noticia.titulo}" />
							</c:when>
							<c:otherwise>
								<div class="card-img-fixed bg-secondary"></div>
							</c:otherwise>
						</c:choose>
						<div class="card-body d-flex flex-column">
							<h5 class="card-title">${noticia.titulo}</h5>
							<p class="card-text flex-grow-1">${noticia.resumo}</p>
							<small class="text-muted mb-2"> ${noticia.dataPublicacao}
								• ${noticia.nomeAutor} </small> <a
								href="${pageContext.request.contextPath}/listar-noticia?id=${noticia.id}"
								class="btn btn-primary mt-auto">Leia Mais</a>
						</div>
						
					</div>
				</div>
			</c:forEach>
		</div>
	</c:forEach>

</div>
<%@ include file="footer.jsp"%>