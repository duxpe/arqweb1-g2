<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="br.edu.ifsp.arq.g2.model.Usuario"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
<%@ include file="menu.jsp"%>

<style>
.card-user {
	min-height: 200px;
}
</style>

<div class="container py-4">
	<div class="d-flex justify-content-between align-items-center mb-4">
		<h2>Listar Usuários</h2>
		<a href="${pageContext.request.contextPath}/addUsuario.jsp"
			class="btn btn-primary"> Cadastrar Usuário </a>
	</div>

	<c:if test="${not empty erro}">
		<div class="alert alert-danger">${erro}</div>
	</c:if>

	<div class="row">
		<c:forEach var="user" items="${sessionScope.usuarios}">
			<div class="col-md-4 mb-4">
				<div class="card card-user h-100">
					<div class="card-body d-flex flex-column">
						<h5 class="card-title">${user.usuario}</h5>
						<p class="card-text mb-1">
							<strong>Nome:</strong> ${user.nome}
						</p>
						<p class="card-text mb-3">
							<strong>Idade:</strong> ${user.idade}
						</p>
						<div class="mt-auto">
							<a
								href="${pageContext.request.contextPath}/editar-usuario?id=${user.id}"
								class="btn btn-sm btn-warning me-2"> Editar </a>
							<form action="${pageContext.request.contextPath}/excluir-usuario"
								method="post" style="display: inline"
								onsubmit="return confirm('Confirmar exclusão do usuário ${user.usuario}?');">
								<input type="hidden" name="id" value="${user.id}" />
								<button type="submit" class="btn btn-sm btn-danger">
									Excluir</button>
							</form>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>

	<c:if test="${empty sessionScope.usuarios}">
		<div class="alert alert-info">Nenhum usuário cadastrado.</div>
	</c:if>
</div>

<%@ include file="footer.jsp"%>
