package br.edu.ifsp.arq.g2.controller.avaliacao;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.ifsp.arq.g2.dao.AvaliacaoDAO;
import br.edu.ifsp.arq.g2.model.Usuario; // Assumindo que você tem um modelo Usuario

@WebServlet("/avaliar-noticia") // Novo endpoint para criar avaliação
public class CreateAvaliacaoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AvaliacaoDAO avaliacaoDAO = AvaliacaoDAO.getInstance();

    public CreateAvaliacaoServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false); // Não cria nova sessão se não existir
        String contextPath = request.getContextPath();
        
        // 1. Verificar se o usuário está logado
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            // Se não está logado, redireciona para a página de login com uma mensagem de erro
            response.sendRedirect(contextPath + "/login.html?erro=Você precisa estar logado para avaliar.");
            return;
        }

        // Se você precisar do ID do usuário, ele estaria aqui:
        // Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        // int idUsuario = usuarioLogado.getId(); // Se o Usuario tem um getId()

        // 2. Obter os parâmetros da requisição
        String idNoticiaParam = request.getParameter("idNoticia");
        String notaParam = request.getParameter("nota");
        
        int idNoticia = -1;
        double nota = -1;
        String errorMessage = null;

        try {
            if (idNoticiaParam == null || idNoticiaParam.isEmpty()) {
                errorMessage = "ID da notícia não fornecido.";
            } else {
                idNoticia = Integer.parseInt(idNoticiaParam);
            }

            if (notaParam == null || notaParam.isEmpty()) {
                errorMessage = "Nota não fornecida.";
            } else {
                nota = Double.parseDouble(notaParam);
                // A validação da nota (0 a 5) é feita no modelo Avaliacao, mas podemos adicionar um pré-cheque aqui também.
                if (nota < 0 || nota > 5) {
                    errorMessage = "A nota deve ser entre 0 e 5.";
                }
            }

            if (errorMessage != null) {
                // Se houver erro de validação dos parâmetros
                response.sendRedirect(contextPath + "/viewNoticia.html?id=" + idNoticia + "&erro=" + errorMessage);
                return;
            }

            // 3. Adicionar a avaliação via DAO
            // Não estamos verificando voto único por usuário como solicitado,
            // então o mesmo usuário pode votar várias vezes.
            avaliacaoDAO.addNotaNoticia(idNoticia, nota); // idUsuario não é usado no addNotaNoticia modificado

            // 4. Redirecionar de volta para a página da notícia com mensagem de sucesso
            response.sendRedirect(contextPath + "/viewNoticia.html?id=" + idNoticia + "&sucesso=Avaliação enviada com sucesso!");

        } catch (NumberFormatException e) {
            errorMessage = "ID da notícia ou nota inválida. Por favor, insira números válidos.";
            response.sendRedirect(contextPath + "/viewNoticia.html?id=" + (idNoticia != -1 ? idNoticia : "") + "&erro=" + errorMessage);
        } catch (Exception e) {
            System.err.println("Erro ao processar avaliação: " + e.getMessage());
            e.printStackTrace();
            errorMessage = "Erro inesperado ao enviar avaliação: " + e.getMessage();
            response.sendRedirect(contextPath + "/viewNoticia.html?id=" + (idNoticia != -1 ? idNoticia : "") + "&erro=" + errorMessage);
        }
    }
    
    // O doGet pode simplesmente redirecionar para a página da notícia ou para a home
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idNoticiaParam = request.getParameter("idNoticia");
        String contextPath = request.getContextPath();
        if (idNoticiaParam != null && !idNoticiaParam.isEmpty()) {
            response.sendRedirect(contextPath + "/viewNoticia.html?id=" + idNoticiaParam);
        } else {
            response.sendRedirect(contextPath + "/index.html"); // Ou alguma página padrão
        }
    }
}