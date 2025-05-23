package br.edu.ifsp.arq.g2.controller.noticia;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import br.edu.ifsp.arq.g2.dao.NoticiaDAO;
import br.edu.ifsp.arq.g2.model.Noticia;


@WebServlet("/criar-noticia")
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
            request.setCharacterEncoding("UTF-8");

            String titulo = request.getParameter("titulo");
            String texto = request.getParameter("texto");
            String resumo = request.getParameter("resumo");
            String dataPubStr = request.getParameter("dataPublicacao");
            String nomeAutor = request.getParameter("nomeAutor");
            String categoria = request.getParameter("categoria");

            if (titulo == null || titulo.isEmpty()
             || texto  == null || texto.isEmpty()
             || resumo == null || resumo.isEmpty()
             || dataPubStr == null || dataPubStr.isEmpty()
             || nomeAutor  == null || nomeAutor.isEmpty()
             || categoria  == null || categoria.isEmpty()) {
                throw new RuntimeException("Todos os campos são obrigatórios.");
            }

            LocalDate dataPublicacao = LocalDate.parse(dataPubStr);

            Noticia nova = new Noticia(
                titulo,
                texto,
                resumo,
                dataPublicacao,
                nomeAutor,
                categoria
            );
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

        request.getRequestDispatcher("addNoticia.jsp")
               .forward(request, response);
    }
}
