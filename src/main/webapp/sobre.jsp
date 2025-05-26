<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="header.jsp"%>
<%@ include file="menu.jsp"%>

<div class="container py-5">
	<h2 class="mb-4">Sobre o Sistema</h2>

	<p>
		<strong>G2 News</strong> é um sistema CRUD simples de notícias
		desenvolvido em Java para a disciplina <em>ARQWEB1</em>.
	</p>

	<h5>Autores</h5>
	<ul>
		<li>Eduardo Antonio Faustino Junior</li>
		<li>Natan Mendes Araujo</li>
		<li>Pedro Geraldi Aguiar</li>
	</ul>

	<h5>Tecnologias Utilizadas</h5>
	<ul>
		<li>Java EE (Servlets, JSP)</li>
		<li>JSTL (JSP Standard Tag Library)</li>
		<li>Bootstrap 4 (layouts e componentes responsivos)</li>
		<li>Padrão DAO para acesso a dados em memória</li>
		<li>Session scope para controle de sessão e contagem de
			visualizações</li>
		<li>Base64 para exibição de imagens armazenadas como byte arrays</li>
	</ul>

	<h5>Funcionalidades Principais</h5>
	<ul>
		<li>CRUD completo de notícias (criar, listar, visualizar, editar,
			excluir)</li>
		<li>Autenticação básica de usuários</li>
		<li>Busca por título, autor ou categoria</li>
		<li>Contador de visualizações por notícia</li>
		<li>Exibição de notícias em destaque via carrossel</li>
		<li>Navegação responsiva via navbar com opções de gerenciamento</li>
	</ul>

	<p class="text-muted small">Projeto entregue como requisito da
		disciplina ARQWEB1 – 2025.</p>
</div>

<%@ include file="footer.jsp"%>
