package br.edu.ifsp.arq.g2.controller.usuario;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.edu.ifsp.arq.g2.dao.UsuarioDAO;
import br.edu.ifsp.arq.g2.model.Usuario;

@WebServlet("/editar-usuario")
public class UpdateUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsuarioDAO dao = UsuarioDAO.getInstance();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public UpdateUsuarioServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String idParam = request.getParameter("id");
        
        try {
            if (idParam == null || idParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(new ErrorResponse("ID nao fornecido.")));
                return;
            }
            
            int id = Integer.parseInt(idParam);
            Usuario u = dao.getUsuario(id);
            
            response.getWriter().write(gson.toJson(u));
            return;
        } catch (NumberFormatException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ErrorResponse("ID invalido, precisa ser um numero inteiro.")));
        } catch (NoSuchElementException ex) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(gson.toJson(new ErrorResponse("Usuario com ID " + idParam + " nao encontrado.")));
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Erro inesperado ao buscar usuario: " + ex.getMessage())));
            ex.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String contextPath = request.getContextPath();
        
        try {
            String idParam = request.getParameter("id");
            String usuario = request.getParameter("usuario");
            String senha = request.getParameter("senha");
            String nome = request.getParameter("nome");
            String idadeStr = request.getParameter("idade");
            String isAdminParam = request.getParameter("isAdmin");
            boolean isAdmin = "on".equalsIgnoreCase(isAdminParam) || "true".equalsIgnoreCase(isAdminParam);


            if (idParam == null || usuario == null || nome == null || idadeStr == null
                    || idParam.isEmpty() || usuario.isEmpty() || nome.isEmpty() || idadeStr.isEmpty()) {
                throw new RuntimeException("Todos os campos obrigatorios (ID, Usuario, Nome, Idade) devem ser preenchidos.");
            }

            int id = Integer.parseInt(idParam);
            int idade = Integer.parseInt(idadeStr);

            dao.updateUsuario(id, usuario, senha, nome, idade, isAdmin);
            
            response.sendRedirect(contextPath + "/listUsuarios.jsp"); 
        } catch (NumberFormatException ex) {
            response.sendRedirect(contextPath + "/changeUsuario.html?id=" + request.getParameter("id") + "&erro=" + "ID e idade devem ser numeros.");
        } catch (RuntimeException ex) {
            response.sendRedirect(contextPath + "/changeUsuario.html?id=" + request.getParameter("id") + "&erro=" + ex.getMessage());
        } catch (Exception ex) {
            response.sendRedirect(contextPath + "/changeUsuario.html?id=" + request.getParameter("id") + "&erro=" + "Erro inesperado ao atualizar usuario: " + ex.getMessage());
            ex.printStackTrace();
        }
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