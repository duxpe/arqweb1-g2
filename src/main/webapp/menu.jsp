<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<div class="container-fluid">
		<a class="navbar-brand"
			href="${pageContext.request.contextPath}/listar-noticia"> G2 News
		</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarNav"
			aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle
			navigation"> <span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="navbarNav">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/sobre.jsp"> Sobre o
						Sistema </a></li>
				<c:if test="${not empty sessionScope.usuarioLogado}">
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/addNoticia.jsp">
							Cadastrar Not�cia </a></li>
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/listar-usuario">
							Listar Usu�rios</a></li>
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/editar-usuario?id=${sessionScope.usuarioLogado.id}">
							Configura��es Pessoais </a></li>
				</c:if>
			</ul>

			<ul class="navbar-nav">
				<c:choose>
					<c:when test="${not empty sessionScope.usuarioLogado}">
						<li class="nav-item"><a class="nav-link"
							href="${pageContext.request.contextPath}/logout"> Logout </a></li>
					</c:when>
					<c:otherwise>
						<li class="nav-item"><a class="nav-link"
							href="${pageContext.request.contextPath}/login.jsp"> Login </a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
</nav>
