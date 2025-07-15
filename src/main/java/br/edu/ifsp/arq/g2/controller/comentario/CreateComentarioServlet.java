package br.edu.ifsp.arq.g2.controller.comentario;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.ifsp.arq.g2.dao.ComentarioDAO; // Seu DAO de comentários
import br.edu.ifsp.arq.g2.model.Comentario;   // Seu modelo Comentario
import br.edu.ifsp.arq.g2.model.Usuario;      // Para pegar o usuário logado

@WebServlet("/criar-comentario")
public class CreateComentarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ComentarioDAO comentarioDAO = ComentarioDAO.getInstance();

    public CreateComentarioServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(false);
		String contextPath = request.getContextPath();

		if (session == null || session.getAttribute("usuarioLogado") == null) {
			String idNoticiaParam = request.getParameter("idNoticia");
			String redirectUrl = contextPath + "/login.html?erro=Precisa estar logado para comentar.";
			if (idNoticiaParam != null && !idNoticiaParam.isEmpty()) {
				redirectUrl += "&redirectBack=/viewNoticia.html?id=" + idNoticiaParam;
			}
			response.sendRedirect(redirectUrl);
			return;
		}

		Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
		String nomeUsuario = usuarioLogado.getNome();

		String idNoticiaParam = request.getParameter("idNoticia");
		String comentarioTexto = request.getParameter("comentario");

		int idNoticia = -1;
		String errorMessage = null;

		try {
			if (idNoticiaParam == null || idNoticiaParam.isEmpty()) {
				errorMessage = "ID da noticia não fornecido para o comentario.";
			} else {
				idNoticia = Integer.parseInt(idNoticiaParam);
			}

			if (comentarioTexto == null || comentarioTexto.trim().isEmpty()) {
				errorMessage = "O comentario não pode ser vazio.";
			} else if (comentarioTexto.length() > 500) {
				errorMessage = "O comentario é muito longo (max 500 caracteres).";
			}

			if (errorMessage != null) {
				response.sendRedirect(contextPath + "/viewNoticia.html?id=" + idNoticia + "&erro=" + errorMessage);
				return;
			}

			Comentario novoComentario = new Comentario(idNoticia, comentarioTexto.trim(), nomeUsuario);
			comentarioDAO.addComentario(novoComentario);

			response.sendRedirect(contextPath + "/viewNoticia.html?id=" + idNoticia + "&sucesso=Comentario publicado com sucesso!");

		} catch (NumberFormatException e) {
			errorMessage = "ID da notícia invalido. Por favor, insira um numero valido.";
			response.sendRedirect(contextPath + "/viewNoticia.html?id=" + (idNoticia != -1 ? idNoticia : "") + "&erro=" + errorMessage);
		} catch (Exception e) {
			System.err.println("Erro ao processar comentario: " + e.getMessage());
			e.printStackTrace();
			errorMessage = "Erro inesperado ao enviar comentario. Por favor, tente novamente.";
			response.sendRedirect(contextPath + "/viewNoticia.html?id=" + (idNoticia != -1 ? idNoticia : "") + "&erro=" + errorMessage);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idNoticiaParam = request.getParameter("idNoticia");
		String contextPath = request.getContextPath();
		if (idNoticiaParam != null && !idNoticiaParam.isEmpty()) {
			response.sendRedirect(contextPath + "/viewNoticia.html?id=" + idNoticiaParam);
		} else {
			response.sendRedirect(contextPath + "/index.html");
		}
	}
}