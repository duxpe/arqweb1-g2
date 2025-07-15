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
    private Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Para JSON formatado

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
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
                response.getWriter().write(gson.toJson(new ErrorResponse("ID não fornecido.")));
                return;
            }
            
            int id = Integer.parseInt(idParam);
            Usuario u = dao.getUsuario(id);
            
            // Retorna o objeto Usuario como JSON
            response.getWriter().write(gson.toJson(u));
            return;
        } catch (NumberFormatException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            response.getWriter().write(gson.toJson(new ErrorResponse("ID inválido, precisa ser um número inteiro.")));
        } catch (NoSuchElementException ex) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
            response.getWriter().write(gson.toJson(new ErrorResponse("Usuário com ID " + idParam + " não encontrado.")));
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write(gson.toJson(new ErrorResponse("Erro inesperado ao buscar usuário: " + ex.getMessage())));
            ex.printStackTrace(); // Para depuração
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String contextPath = request.getContextPath(); // Captura o context path dinamicamente
        
        try {
            String idParam = request.getParameter("id");
            String usuario = request.getParameter("usuario");
            String senha = request.getParameter("senha"); // Pode ser nula/vazia
            String nome = request.getParameter("nome");
            String idadeStr = request.getParameter("idade");

            if (idParam == null || usuario == null || nome == null || idadeStr == null
                    || idParam.isEmpty() || usuario.isEmpty() || nome.isEmpty() || idadeStr.isEmpty()) {
                throw new RuntimeException("Todos os campos obrigatórios (ID, Usuário, Nome, Idade) devem ser preenchidos.");
            }

            int id = Integer.parseInt(idParam);
            int idade = Integer.parseInt(idadeStr);

            // Se a senha estiver vazia, o DAO deve ser capaz de manter a senha existente
            // A lógica de "deixe em branco para manter a senha atual" deve ser tratada no DAO.
            dao.updateUsuario(id, usuario, senha, nome, idade); 
            
            // Redireciona para a página inicial ou para a lista de usuários após o sucesso
            response.sendRedirect(contextPath + "/listUsuarios.html"); 
        } catch (NumberFormatException ex) {
            // Em caso de erro, redireciona de volta para a página de edição com a mensagem de erro
            response.sendRedirect(contextPath + "/changeUsuario.html?id=" + request.getParameter("id") + "&erro=" + "ID e idade devem ser números.");
        } catch (RuntimeException ex) {
            response.sendRedirect(contextPath + "/changeUsuario.html?id=" + request.getParameter("id") + "&erro=" + ex.getMessage());
        } catch (Exception ex) {
            response.sendRedirect(contextPath + "/changeUsuario.html?id=" + request.getParameter("id") + "&erro=" + "Erro inesperado ao atualizar usuário: " + ex.getMessage());
            ex.printStackTrace(); // Para depuração
        }
    }

    // Classe auxiliar para retorno de erro em JSON
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