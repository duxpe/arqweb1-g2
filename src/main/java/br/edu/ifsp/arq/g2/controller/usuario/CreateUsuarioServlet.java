package br.edu.ifsp.arq.g2.controller.usuario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.ifsp.arq.g2.dao.UsuarioDAO;
import br.edu.ifsp.arq.g2.model.Usuario;

@WebServlet("/criar-usuario")
public class CreateUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UsuarioDAO dao = UsuarioDAO.getInstance();

	public CreateUsuarioServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();

		try {
			session.setAttribute("erro", null);
			String usuario = request.getParameter("usuario");
			String senha = request.getParameter("senha");
			String nome = request.getParameter("nome");
			String idadeStr = request.getParameter("idade");

			if (usuario == null || usuario.isEmpty() || senha == null || senha.isEmpty() || nome == null
					|| nome.isEmpty() || idadeStr == null || idadeStr.isEmpty()) {
				throw new RuntimeException("Todos os campos são obrigatórios.");
			}
			int idade = Integer.parseInt(idadeStr);
			Usuario novo = new Usuario(usuario, senha, nome, idade);
			dao.addUsuario(novo);

			request.getRequestDispatcher("login.jsp").forward(request, response);
		} catch (NumberFormatException ex) {
			session.setAttribute("erro",
					"Erro, a idade deve ser um inteiro que represente sua idade em anos e seja maior que 0");
			request.getRequestDispatcher("addUsuario.jsp").forward(request, response);
		} catch (Exception ex) {
			session.setAttribute("erro", "Erro inesperado: " + ex.getMessage());
			request.getRequestDispatcher("addUsuario.jsp").forward(request, response);
		}
	}

}
