package br.edu.ifsp.arq.g2.controller.usuario;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.ifsp.arq.g2.dao.UsuarioDAO;


@WebServlet("/listar-usuario")
public class ReadUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UsuarioDAO dao = UsuarioDAO.getInstance();

	public ReadUsuarioServlet() {
		super();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String errorMessage = "";
		int id = -1;
		
		try {
			id = Integer.parseInt(request.getParameter("id"));
			
			if (id >= 0) {
				session.setAttribute("usuarioSelecionado", dao.getUsuario(id));
				request.getRequestDispatcher("changeUsuario.jsp").forward(request, response);
			}
			session.setAttribute("usuarios", dao.getUsuarios());
			request.getRequestDispatcher("listUsuarios.jsp").forward(request, response);
		} catch (NumberFormatException parseEx) {
			errorMessage = "ID de usuário inválido, ele precisar ser um número inteiro. A busca falhou!";
		} catch (NoSuchElementException notFoundEX) {
			errorMessage = "Usuário com ID: " + id + "Não encontrado. A busca falhou!";
		} catch (Exception ex) {
			errorMessage = "Erro inesperado: "+ex.getMessage();
		}
		
		request.setAttribute("erro", errorMessage);
		request.getRequestDispatcher("/listar-noticia").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
