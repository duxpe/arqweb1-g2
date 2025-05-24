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
        try {
        	System.out.println("Entrou no create!");
            request.setCharacterEncoding("UTF-8");

            String titulo = request.getParameter("titulo");
            String conteudo = request.getParameter("conteudo");
            String resumo = request.getParameter("resumo");
            String dataPubStr = request.getParameter("dataPublicacao");
            String nomeAutor = request.getParameter("nomeAutor");
            String categoria = request.getParameter("categoria");
            
        	System.out.println("ch1!");

            if (titulo == null || titulo.isEmpty()
             || conteudo  == null || conteudo.isEmpty()
             || resumo == null || resumo.isEmpty()
             || dataPubStr == null || dataPubStr.isEmpty()
             || nomeAutor  == null || nomeAutor.isEmpty()
             || categoria  == null || categoria.isEmpty()) {
                throw new RuntimeException("Todos os campos são obrigatórios.");
            }
            
        	System.out.println("ch2!");

            Part imagemPart = request.getPart("imagem");
            byte[] imagemBytes = null;
            if (imagemPart != null && imagemPart.getSize() > 0) {
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

            System.out.println("ch3");

            dao.addNoticia(nova);

            response.sendRedirect(request.getContextPath() + "/listar-noticia");
            return;
        }
        catch (DateTimeParseException ex) {
            request.setAttribute("erro", "Data de publicação inválida.");
        }
        catch (RuntimeException ex) {
            request.setAttribute("erro", ex.getMessage());
        }
        catch (Exception ex) {
            request.setAttribute("erro", "Erro ao criar notícia: " + ex.getMessage());
        }
        
    	System.out.println("passou do ponto ch4!");

        request.getRequestDispatcher("addNoticia.jsp")
               .forward(request, response);
    }
}
