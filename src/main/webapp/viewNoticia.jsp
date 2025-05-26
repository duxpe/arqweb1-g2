<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.util.Base64"%>
<%@ page import="br.edu.ifsp.arq.g2.model.Noticia"%>
<%@ include file="header.jsp"%>
<%@ include file="menu.jsp"%>

<div class="container py-5">
	<div class="row">
		<div class="col-lg-8 mx-auto">

			<h1 class="mb-3">${noticiaSelecionada.titulo}</h1>
			<p class="text-muted">Por ${noticiaSelecionada.nomeAutor} em
				${noticiaSelecionada.dataPublicacao}</p>
			<p>
				<strong>Categoria:</strong> ${noticiaSelecionada.categoria}
			</p>
			<p>
				<strong>Visualizações:</strong> ${noticiaSelecionada.visualizacoes}
			</p>
			<c:if test="${not empty sessionScope.usuarioLogado}">
				<a
					href="${pageContext.request.contextPath}/editar-noticia?id=${noticiaSelecionada.id}"
					class="btn btn-sm btn-warning mb-4">Editar</a>
			</c:if>
			<hr>

			<c:if test="${noticiaSelecionada.imagem ne null}">
				<%
				Noticia n = (Noticia) session.getAttribute("noticiaSelecionada");
				byte[] img = n.getImagem();
				String base64 = Base64.getEncoder().encodeToString(img);
				%>
				<img src="data:image/png;base64,<%=base64%>"
					class="img-fluid mb-4" alt="Imagem de <%=n.getTitulo()%>" />
			</c:if>

			<div class="mt-3">
				<p>${noticiaSelecionada.conteudo}</p>
			</div>

		</div>
	</div>
</div>

<%@ include file="footer.jsp"%>
