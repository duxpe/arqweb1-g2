<%@include file="header.jsp"%>

<div class="container py-5">
	<h2 class="text-center mb-4">Login</h2>
	<form action="auth" method="post" class="mx-auto"
		style="max-width: 400px;">
		<div class="mb-3">
			<label for="usuario" class="form-label">Usuario</label> <input
				type="text" class="form-control" id="username" name="username"
				required>
		</div>

		<div class="mb-3">
			<label for="senha" class="form-label">Senha</label> <input
				type="password" class="form-control" id="senha" name="senha"
				required>
		</div>

		<div class="d-grid">
			<button type="submit" class="btn btn-primary">Entrar</button>

			<a href="addUsuario.jsp">
				<button type="button" class="btn btn-info">Criar Conta</button>
			</a>
		</div>

	</form>
</div>

<%@include file="footer.jsp"%>
