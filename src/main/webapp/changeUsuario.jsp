<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
<%@ include file="menu.jsp"%>

<c:set var="usuario" value="${sessionScope.usuarioSelecionado}" />

<div class="container py-5">
	<h2 class="text-center mb-4">Editar Perfil</h2>

	<form action="${pageContext.request.contextPath}/editar-usuario"
		method="post" class="mx-auto" style="max-width: 500px;">

		<input type="hidden" name="id" value="${usuario.id}" />

		<div class="mb-3">
			<label for="usuario" class="form-label">Usuário</label> <input
				type="text" class="form-control" id="usuario" name="usuario"
				value="${usuario.usuario}" required>
		</div>

		<div class="mb-3">
			<label for="nome" class="form-label">Nome completo</label> <input
				type="text" class="form-control" id="nome" name="nome"
				value="${usuario.nome}" required>
		</div>

		<div class="mb-3">
			<label for="idade" class="form-label">Idade</label> <input
				type="number" class="form-control" id="idade" name="idade"
				value="${usuario.idade}" required>
		</div>
		<div class="mb-3">
			<label for="senha" class="form-label">Nova Senha</label> <input
				type="password" class="form-control" id="senha" name="senha">
			<div class="form-text">Deixe em branco para manter a senha
				atual.</div>
		</div>

		<div class="d-flex justify-content-end">
			<button type="submit" class="btn btn-primary me-2">Atualizar</button>
			<a href="${pageContext.request.contextPath}/listar-usuario"
				class="btn btn-secondary">Cancelar</a>
		</div>
	</form>
</div>

<%@ include file="footer.jsp"%>
