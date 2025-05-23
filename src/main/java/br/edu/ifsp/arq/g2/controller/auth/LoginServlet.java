package br.edu.ifsp.arq.g2.controller.auth;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.ifsp.arq.g2.dao.UsuarioDAO;
import br.edu.ifsp.arq.g2.model.Usuario;

@WebServlet("/auth")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UsuarioDAO dao = UsuarioDAO.getInstance();

	public LoginServlet() {
		super();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String errorMessage = "";
		Usuario usuarioLogado = null;
		System.out.println(dao.getUsuarios());
		String usuario, senha;

		try {
			session.setAttribute("erro", null);
			usuario = (String) request.getParameter("username");
			senha = (String) request.getParameter("senha");
			System.out.println(dao.getUsuarios());
			System.out.println(usuario);
			System.out.println(senha);

			usuarioLogado = dao.getUsuario(usuario, senha);

			System.out.println(usuarioLogado);
			System.out.println(dao.getUsuarios());

			session.setAttribute("usuarioLogado", usuarioLogado);
			response.sendRedirect(request.getContextPath() + "/listar-noticia");
			return;

		} catch (ClassCastException castEx) {
			errorMessage = "Usuário e/ou senha contém caractéres ou dados inválidos.";
		} catch (NoSuchElementException notFoundEX) {
			errorMessage = "Usuário ou senha inválidos...";
		} catch (Exception ex) {
			errorMessage = "Erro inesperado: " + ex.getMessage();
		}

		session.setAttribute("usuarioLogado", null);

		session.setAttribute("erro", errorMessage);
		response.sendRedirect(request.getContextPath() + "/login.jsp");

	}

}
