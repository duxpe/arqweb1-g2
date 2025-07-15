package br.edu.ifsp.arq.g2.controller.usuario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        String contextPath = request.getContextPath(); 
        System.out.println("CreateUsuarioServlet: Recebendo requisição POST para /criar-usuario"); // LOG

        try {
            String usuario = request.getParameter("usuario");
            String senha = request.getParameter("senha");
            String nome = request.getParameter("nome");
            String idadeStr = request.getParameter("idade");

            System.out.println("CreateUsuarioServlet: Dados recebidos: usuario=" + usuario + ", nome=" + nome + ", idadeStr=" + idadeStr); // LOG

            if (usuario == null || usuario.isEmpty() || senha == null || senha.isEmpty() || nome == null
                    || nome.isEmpty() || idadeStr == null || idadeStr.isEmpty()) {
                System.out.println("CreateUsuarioServlet: Campos obrigatórios faltando."); // LOG
                throw new RuntimeException("Todos os campos são obrigatórios.");
            }
            
            int idade = Integer.parseInt(idadeStr);
            if (idade < 0 || idade > 150) {
                System.out.println("CreateUsuarioServlet: Idade inválida: " + idade); // LOG
                throw new RuntimeException("A idade deve ser entre 0 e 150.");
            }

            Usuario novo = new Usuario(usuario, senha, nome, idade);
            System.out.println("CreateUsuarioServlet: Objeto Usuario criado: " + novo.toString()); // LOG

            // *** Ponto crítico: Chamar o DAO ***
            dao.addUsuario(novo); // Esta linha é onde a persistência acontece
            System.out.println("CreateUsuarioServlet: Usuário ADICIONADO via DAO. Redirecionando para login."); // LOG

            response.sendRedirect(contextPath + "/login.html?sucesso=true&msg=Usuário cadastrado com sucesso! Faça login.");
        } catch (NumberFormatException ex) {
            String errorMsg = "A idade deve ser um número inteiro válido.";
            System.err.println("CreateUsuarioServlet: Erro de formato de número: " + errorMsg + " - " + ex.getMessage()); // LOG ERRO
            ex.printStackTrace(); // Imprime o stack trace no console do servidor
            response.sendRedirect(contextPath + "/addUsuario.html?erro=" + errorMsg);
        } catch (RuntimeException ex) { // Captura exceções de validação e outras RuntimeExceptions
            System.err.println("CreateUsuarioServlet: Erro de Runtime: " + ex.getMessage()); // LOG ERRO
            ex.printStackTrace();
            response.sendRedirect(contextPath + "/addUsuario.html?erro=" + ex.getMessage());
        } catch (Exception ex) { // Captura qualquer outra exceção inesperada
            String errorMsg = "Erro inesperado ao cadastrar usuário: " + ex.getMessage();
            System.err.println("CreateUsuarioServlet: Erro geral: " + errorMsg); // LOG ERRO
            ex.printStackTrace();
            response.sendRedirect(contextPath + "/addUsuario.html?erro=" + errorMsg);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("CreateUsuarioServlet: Recebendo requisição GET para /criar-usuario. Redirecionando para addUsuario.html"); // LOG
        response.sendRedirect(request.getContextPath() + "/addUsuario.html");
    }
}