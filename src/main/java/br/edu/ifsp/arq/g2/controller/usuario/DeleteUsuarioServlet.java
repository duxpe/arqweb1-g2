package br.edu.ifsp.arq.g2.controller.usuario;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import br.edu.ifsp.arq.g2.dao.UsuarioDAO;

@WebServlet("/excluir-usuario")
public class DeleteUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsuarioDAO dao = UsuarioDAO.getInstance();

    public DeleteUsuarioServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty())
                throw new IllegalArgumentException("ID não fornecido.");
            int id = Integer.parseInt(idParam);
            dao.removeUsuario(id);
            response.sendRedirect(request.getContextPath() + "/listar-usuario");
        } catch (NumberFormatException ex) {
            request.setAttribute("erro", "ID inválido.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (NoSuchElementException ex) {
            request.setAttribute("erro", ex.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (Exception ex) {
            request.setAttribute("erro", "Erro ao excluir usuário: " + ex.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/listar-usuario");
    }
}
