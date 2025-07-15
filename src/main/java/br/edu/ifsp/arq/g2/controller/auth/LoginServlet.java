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
import com.google.gson.Gson;

@WebServlet("/auth")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UsuarioDAO dao = UsuarioDAO.getInstance();
    private Gson gson = new Gson();

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
		String usuario, senha;

		try {
			session.setAttribute("erro", null);
			usuario = (String) request.getParameter("username");
			senha = (String) request.getParameter("senha");

			usuarioLogado = dao.getUsuario(usuario, senha);

			session.setAttribute("usuarioLogado", usuarioLogado);
			response.sendRedirect(request.getContextPath() + "/");
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
		response.sendRedirect(request.getContextPath() + "/login.html");

	}
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false); // Não cria nova sessão se não existir

        boolean loggedIn = (session != null && session.getAttribute("usuarioLogado") != null);
        Integer userId = null; // Inicializa com null
        if (loggedIn) {
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
            if (usuario != null) {
                userId = usuario.getId(); // Assume que seu objeto Usuario tem um método getId()
            }
        }

        // Retorna um JSON com o status de login e o ID do usuário (se logado)
        response.getWriter().write(gson.toJson(new LoginStatusResponse(loggedIn, userId)));
    }

    // Classe auxiliar para o retorno JSON
    private static class LoginStatusResponse {
        private boolean loggedIn;
        private Integer userId; // Adicionado para retornar o ID do usuário

        public LoginStatusResponse(boolean loggedIn, Integer userId) {
            this.loggedIn = loggedIn;
            this.userId = userId;
        }

        // Getters para Gson
        public boolean isLoggedIn() {
            return loggedIn;
        }
        public Integer getUserId() {
            return userId;
        }
    }

}
