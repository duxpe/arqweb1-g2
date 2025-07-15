package br.edu.ifsp.arq.g2.controller.noticia;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import br.edu.ifsp.arq.g2.dao.NoticiaDAO;
import br.edu.ifsp.arq.g2.model.Noticia;


@WebServlet("/criar-noticia")
@MultipartConfig
public class CreateNoticiaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private NoticiaDAO dao = NoticiaDAO.getInstance();

    public CreateNoticiaServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String contextPath = request.getContextPath(); // Captura o context path dinamicamente
        try {
            request.setCharacterEncoding("UTF-8");

            String titulo = request.getParameter("titulo");
            String conteudo = request.getParameter("conteudo");
            String resumo = request.getParameter("resumo");
            String dataPubStr = request.getParameter("dataPublicacao");
            String nomeAutor = request.getParameter("nomeAutor");
            String categoria = request.getParameter("categoria");
            
            if (titulo == null || titulo.isEmpty()
             || conteudo  == null || conteudo.isEmpty()
             || resumo == null || resumo.isEmpty()
             || dataPubStr == null || dataPubStr.isEmpty()
             || nomeAutor  == null || nomeAutor.isEmpty()
             || categoria  == null || categoria.isEmpty()) {
                throw new RuntimeException("Todos os campos são obrigatórios.");
            }
            
            Part imagemPart = request.getPart("imagem");
            byte[] imagemBytes = null;
            if (imagemPart != null && imagemPart.getSize() > 0 && imagemPart.getSubmittedFileName() != null && !imagemPart.getSubmittedFileName().isEmpty()) {
                try (InputStream is = imagemPart.getInputStream()) {
                    imagemBytes = is.readAllBytes();
                }
            }

            LocalDate dataPublicacao = LocalDate.parse(dataPubStr);

            Noticia nova = new Noticia(
                titulo,
                conteudo,
                resumo,
                dataPublicacao,
                nomeAutor,
                categoria
            );
            
            nova.setImagem(imagemBytes);

            dao.addNoticia(nova);

            response.sendRedirect(contextPath + "/"); // Redireciona para a lista após sucesso
            return;
        }
        catch (DateTimeParseException ex) {
            // Em caso de erro, redireciona para a página do formulário com uma mensagem de erro
            response.sendRedirect(contextPath + "/addNoticia.html?erro=" + "Data de publicação inválida.");
        }
        catch (RuntimeException ex) {
            response.sendRedirect(contextPath + "/addNoticia.html?erro=" + ex.getMessage());
        }
        catch (Exception ex) {
            response.sendRedirect(contextPath + "/addNoticia.html?erro=" + "Erro ao criar notícia: " + ex.getMessage());
        }
    }
}