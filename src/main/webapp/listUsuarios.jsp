<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Listar Usuários - G2 Notícias</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css" integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N" crossorigin="anonymous">
    
    <style>
        .card-user {
            min-height: 200px;
        }
    </style>

    <script src="assets/js/components.js" defer></script> 
</head>
<body>
    <app-header></app-header>

    <div class="container py-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Listar Usuários</h2>
            <a href="/g2/addUsuario.html" class="btn btn-primary"> Cadastrar Usuário </a>
        </div>

        <div id="message-area"></div>

        <div class="row" id="users-container">
            </div>

        <div id="no-users-message" class="alert alert-info d-none">Nenhum usuário cadastrado.</div>
    </div>

    <app-footer></app-footer>

    <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSm5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-Fy6S3B9q64WdZWQUiU+q4/2Lc9npb8tCaSX9FK7E8HnRr0Jz8D6OP9dO5Vg3Q9ct"
        crossorigin="anonymous"></script>

    <script src="assets/js/header.js"></script> 
    <script src="assets/js/components.js" defer></script> 
    <script src="assets/js/listUsers.js" defer></script> 
</body>
</html>