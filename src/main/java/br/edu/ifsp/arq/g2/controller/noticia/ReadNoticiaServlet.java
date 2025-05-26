package br.edu.ifsp.arq.g2.controller.noticia;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import br.edu.ifsp.arq.g2.dao.NoticiaDAO;
import br.edu.ifsp.arq.g2.model.Noticia;



@WebServlet({ "/", "/listar-noticia"})
public class ReadNoticiaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private NoticiaDAO dao = NoticiaDAO.getInstance();

    public ReadNoticiaServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String errorMessage = "";

        try {
        	String buscarParam = request.getParameter("buscar");
        	if (buscarParam != null && !buscarParam.isEmpty()) {
        	    List<Noticia> resultados = dao.buscar(buscarParam);
        	    request.setAttribute("resultadoBusca", resultados);
        	    request.getRequestDispatcher("resultadoBusca.jsp")
        	           .forward(request, response);
        	    return;
        	}

            String idParam = request.getParameter("id");
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                Noticia selecionada = dao.getNoticia(id);
                selecionada.addVisualizacao();
                session.setAttribute("noticiaSelecionada", selecionada);
                request.getRequestDispatcher("viewNoticia.jsp")
                       .forward(request, response);
                return;
            }

            session.setAttribute("noticias", dao.getnoticias());
            request.getRequestDispatcher("index.jsp")
                   .forward(request, response);
            return;
        }
        catch (NumberFormatException | IOException | ServletException ex) {
            errorMessage = "Parâmetro inválido ou erro na requisição: " + ex.getMessage();
        }
        catch (Exception ex) {
            errorMessage = "Erro ao processar notícia: " + ex.getMessage();
        }

        request.setAttribute("erro", errorMessage);
        request.getRequestDispatcher("index.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
