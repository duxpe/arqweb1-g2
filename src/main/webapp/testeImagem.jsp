<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Teste Enviar Imagem</h1>
	<form action="criar-imagem" method="post" enctype="multipart/form-data">
        <input type="file" name="imagem" accept=".png" required>
        <button type="submit">Enviar</button>
    </form>
</body>
</html>