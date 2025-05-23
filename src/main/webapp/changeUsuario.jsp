<%@include file="header.jsp" %>
<%@ include file="menu.jsp" %>


<div class="container py-5">
    <h2 class="text-center mb-4">Editar Perfil</h2>

    <form action="editar-usuario" method="post" class="mx-auto" style="max-width: 500px;">
        <input type="hidden" name="id" value="${usuario.id}" />

        <div class="mb-3">
            <label for="nome" class="form-label">Nome completo</label>
            <input type="text" class="form-control" id="nome" name="nome" value="${usuario.nome}" required>
        </div>

        <div class="mb-3">
            <label for="email" class="form-label">E-mail</label>
            <input type="email" class="form-control" id="email" name="email" value="${usuario.email}" required>
        </div>

        <div class="mb-3">
            <label for="senha" class="form-label">Nova Senha</label>
            <input type="password" class="form-control" id="senha" name="senha">
            <div class="form-text">Deixe em branco para manter a senha atual.</div>
        </div>

        <div class="d-flex justify-content-end">
            <button type="submit" class="btn btn-primary me-2">Atualizar</button>
            <a href="index.jsp" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</div>

<%@include file="footer.jsp" %>
