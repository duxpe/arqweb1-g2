<%@include file="header.jsp"%>
<%@ include file="menu.jsp" %>

<div class="container py-5">
    <div class="row">
        <div class="col-lg-8 mx-auto">
            <h1 class="mb-3">${noticiaSelecionada.titulo}</h1>
            <p class="text-muted">Por ${noticiaSelecionada.nomeAutor} em ${noticiaSelecionada.dataPublicacao}</p>
            <p><strong>Categoria:</strong> ${noticiaSelecionada.categoria}</p>
            <p><strong>Visualizações:</strong> ${noticiaSelecionada.visualizacoes}</p>
            <hr>
            <p>${noticiaSelecionada.texto}</p>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>
