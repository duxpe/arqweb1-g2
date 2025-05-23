<%@include file="header.jsp" %>

<div class="container py-5">
  <h2 class="text-center mb-4">Cadastro de Novo Usuário</h2>

  <form action="${pageContext.request.contextPath}/criar-usuario" method="post" class="mx-auto" style="max-width: 500px;">
    <div class="mb-3">
      <label for="nome" class="form-label">Nome completo</label>
      <input
        type="text"
        class="form-control"
        id="nome"
        name="nome"
        placeholder="João da Silva"
        required
      >
    </div>

    <div class="mb-3">
      <label for="usuario" class="form-label">Usuário (login)</label>
      <input
        type="text"
        class="form-control"
        id="usuario"
        name="usuario"
        placeholder="joaosilva"
        required
      >
    </div>


    <div class="mb-3">
      <label for="senha" class="form-label">Senha</label>
      <input
        type="password"
        class="form-control"
        id="senha"
        name="senha"
        minlength="6"
        required
      >
    </div>

    <div class="mb-3">
      <label for="idade" class="form-label">Idade</label>
      <input
        type="number"
        class="form-control"
        id="idade"
        name="idade"
        min="0"
        max="150"
        required
      >
    </div>

    <div class="d-flex justify-content-between">
      <a href="listUsuarios.jsp" class="btn btn-secondary">Cancelar</a>
      <button type="submit" class="btn btn-success">Cadastrar</button>
    </div>
  </form>
</div>

<%@include file="footer.jsp" %>
