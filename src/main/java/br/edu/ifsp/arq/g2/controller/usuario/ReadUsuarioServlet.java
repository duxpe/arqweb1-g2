package br.edu.ifsp.arq.g2.controller.usuario;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.List; // Importar List

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson; // Importar Gson
import com.google.gson.GsonBuilder; // Importar GsonBuilder para Pretty Printing

import br.edu.ifsp.arq.g2.dao.UsuarioDAO;
import br.edu.ifsp.arq.g2.model.Usuario; // Importar Usuario

@WebServlet("/listar-usuario")
public class ReadUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsuarioDAO dao = UsuarioDAO.getInstance();
    // Use GsonBuilder para um JSON mais legível durante o desenvolvimento
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
        String contextPath = request.getContextPath(); // Para redirecionamentos

        String idParam = request.getParameter("id");
        
        if (idParam != null && !idParam.isEmpty()) {
            // Lógica para buscar um único usuário (para edição)
            try {
                int id = Integer.parseInt(idParam);
                Usuario usuarioSelecionado = dao.getUsuario(id);
                session.setAttribute("usuarioSelecionado", usuarioSelecionado); // Ainda pode ser útil para outras JSPs

                // Redireciona para a nova página HTML de edição de usuário
                // Assumindo que você terá um changeUsuario.html
                response.sendRedirect(contextPath + "/changeUsuario.html?id=" + id); 
                return;
            } catch (NumberFormatException parseEx) {
                errorMessage = "ID inválido, precisa ser número inteiro.";
            } catch (NoSuchElementException notFoundEx) {
                errorMessage = "Usuário com ID " + idParam + " não encontrado.";
            } catch (Exception ex) {
                errorMessage = "Erro inesperado ao buscar usuário por ID: " + ex.getMessage();
                ex.printStackTrace();
            }
            // Se houver erro ao buscar um único usuário por ID, retorna um erro ou redireciona
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(new ErrorResponse(errorMessage)));
            return;

        } else {
            // Lógica para listar todos os usuários (para a página listUsuarios.html)
            try {
                List<Usuario> usuarios = dao.getUsuarios();
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(gson.toJson(usuarios));
                return; // Importante para não continuar para o RequestDispatcher
            } catch (Exception ex) {
                errorMessage = "Erro ao listar usuários: " + ex.getMessage();
                ex.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
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
        // Para manter a funcionalidade, o doPost ainda pode chamar o doGet se você tiver um caso de uso.
        // No entanto, para APIs RESTful, POST seria para criação, não listagem.
        doGet(request, response);
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