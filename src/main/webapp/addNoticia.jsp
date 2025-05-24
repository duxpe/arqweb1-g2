<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="header.jsp"%>
<%@ include file="menu.jsp"%>


<div class="container py-5">
	<h2 class="mb-4 text-center">Adicionar Nova Notícia</h2>

	<form action="criar-noticia" method="post"
		enctype="multipart/form-data" class="needs-validation" novalidate>

		<div class="row">
			<div class="col-md-6 mb-3">
				<label for="titulo" class="form-label">Título</label> <input
					type="text" class="form-control" id="titulo" name="titulo" required>
			</div>

			<div class="col-md-6 mb-3">
				<label for="autor" class="form-label">Autor</label> <input
					type="text" class="form-control" id="nomeAutor" name="nomeAutor"
					required>
			</div>
		</div>

		<div class="row">
			<div class="col-md-6 mb-3">
				<label for="dataPublicacao" class="form-label">Data de
					Publicação</label> <input type="date" class="form-control"
					id="dataPublicacao" name="dataPublicacao" required>
			</div>

			<div class="col-md-6 mb-3">
				<label for="categoria" class="form-label">Categoria</label> <select
					class="form-select" id="categoria" name="categoria" required>
					<option value="Política">Política</option>
					<option value="Esportes">Esportes</option>
					<option value="Tecnologia">Tecnologia</option>
					<option value="Entretenimento">Entretenimento</option>
				</select>
			</div>
		</div>

		<div class="mb-3">
			<label for="resumo" class="form-label">Resumo</label>
			<textarea class="form-control" id="resumo" name="resumo" rows="3"
				maxlength="300" required></textarea>
			<div class="form-text">Máximo 300 caracteres.</div>
		</div>

		<div class="mb-3">
			<label for="conteudo" class="form-label">Conteúdo Completo</label>
			<textarea class="form-control" id="conteudo" name="conteudo" rows="8"
				required></textarea>
		</div>

		<div class="mb-3">
			<label for="imagem" class="form-label">Imagem de Destaque</label> <input
				type="file" class="form-control" id="imagem" name="imagem"
				accept="image/*">
		</div>

		<div class="d-flex justify-content-end">
			<button type="submit" class="btn btn-success me-2">Publicar</button>
			<a href="listar-noticia" class="btn btn-secondary">Cancelar</a>
		</div>
	</form>
</div>

<%@ include file="footer.jsp"%>
