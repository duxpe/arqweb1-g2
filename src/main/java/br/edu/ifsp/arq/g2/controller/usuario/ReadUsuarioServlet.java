package br.edu.ifsp.arq.g2.controller.usuario;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.edu.ifsp.arq.g2.dao.UsuarioDAO;
import br.edu.ifsp.arq.g2.model.Usuario;

@WebServlet("/listar-usuario")
public class ReadUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsuarioDAO dao = UsuarioDAO.getInstance();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create(); 

    public ReadUsuarioServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String errorMessage = "";
        String contextPath = request.getContextPath();

        String idParam = request.getParameter("id");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                Usuario usuarioSelecionado = dao.getUsuario(id);
                session.setAttribute("usuarioSelecionado", usuarioSelecionado);
                response.sendRedirect(contextPath + "/changeUsuario.html?id=" + id); 
                return;
            } catch (NumberFormatException parseEx) {
                errorMessage = "ID invalido, precisa ser numero inteiro.";
            } catch (NoSuchElementException notFoundEx) {
                errorMessage = "Usuario com ID " + idParam + " nao encontrado.";
            } catch (Exception ex) {
                errorMessage = "Erro inesperado ao buscar usuario por ID: " + ex.getMessage();
                ex.printStackTrace();
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(new ErrorResponse(errorMessage)));
            return;

        } else {
            try {
                List<Usuario> usuarios = dao.getUsuarios();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(gson.toJson(usuarios));
                return;
            } catch (Exception ex) {
                errorMessage = "Erro ao listar usuarios: " + ex.getMessage();
                ex.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(gson.toJson(new ErrorResponse(errorMessage)));
                return;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
