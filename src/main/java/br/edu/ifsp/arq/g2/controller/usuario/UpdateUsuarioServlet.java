package br.edu.ifsp.arq.g2.controller.usuario;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import br.edu.ifsp.arq.g2.dao.UsuarioDAO;
import br.edu.ifsp.arq.g2.model.Usuario;

@WebServlet("/editar-usuario")
public class UpdateUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UsuarioDAO dao = UsuarioDAO.getInstance();

	public UpdateUsuarioServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String idParam = request.getParameter("id");
		try {
			if (idParam == null || idParam.isEmpty())
				throw new IllegalArgumentException("ID não fornecido.");
			int id = Integer.parseInt(idParam);
			Usuario u = dao.getUsuario(id);
			request.getSession().setAttribute("usuarioSelecionado", u);
			request.getRequestDispatcher("changeUsuario.jsp").forward(request, response);
		} catch (IllegalArgumentException ex) {
			request.setAttribute("erro", ex.getMessage());
			request.getRequestDispatcher("index.jsp").forward(request, response);
		} catch (NoSuchElementException ex) {
			request.setAttribute("erro", ex.getMessage());
			request.getRequestDispatcher("index.jsp").forward(request, response);
		} catch (Exception ex) {
			request.setAttribute("erro", "Erro inesperado: " + ex.getMessage());
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		try {
			String idParam = request.getParameter("id");
			String usuario = request.getParameter("usuario");
			String senha = request.getParameter("senha");
			String nome = request.getParameter("nome");
			String idadeStr = request.getParameter("idade");

			if (idParam == null || usuario == null || senha == null || nome == null || idadeStr == null
					|| usuario.isEmpty() || nome.isEmpty() || idadeStr.isEmpty()) {
				throw new RuntimeException("Todos os campos são obrigatórios.");
			}

			int id = Integer.parseInt(idParam);
			int idade = Integer.parseInt(idadeStr);

			dao.updateUsuario(id, usuario, senha, nome, idade);
			response.sendRedirect(request.getContextPath() + "/listar-usuario");
		} catch (NumberFormatException ex) {
			request.setAttribute("erro", "ID e idade devem ser números.");
			request.getRequestDispatcher("changeUsuario.jsp").forward(request, response);
		} catch (RuntimeException ex) {
			request.setAttribute("erro", ex.getMessage());
			request.getRequestDispatcher("changeUsuario.jsp").forward(request, response);
		} catch (Exception ex) {
			request.setAttribute("erro", "Erro inesperados: " + ex.getMessage());
			request.getRequestDispatcher("changeUsuario.jsp").forward(request, response);
		}
	}
}
