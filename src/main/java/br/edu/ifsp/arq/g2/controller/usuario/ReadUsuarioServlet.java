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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String errorMessage = "";

        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                session.setAttribute("usuarioSelecionado", dao.getUsuario(id));
                request.getRequestDispatcher("changeUsuario.jsp").forward(request, response);
                return;
            } catch (NumberFormatException parseEx) {
                errorMessage = "ID inválido, precisa ser número inteiro.";
            } catch (NoSuchElementException notFoundEx) {
                errorMessage = "Usuário com ID " + idParam + " não encontrado.";
            } catch (Exception ex) {
                errorMessage = "Erro inesperado: " + ex.getMessage();
            }
        }

        session.setAttribute("usuarios", dao.getUsuarios());

        if (!errorMessage.isEmpty()) {
            request.setAttribute("erro", errorMessage);
        }

        request.getRequestDispatcher("listUsuarios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
