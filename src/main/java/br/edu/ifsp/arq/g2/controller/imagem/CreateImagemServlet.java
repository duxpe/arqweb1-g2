package br.edu.ifsp.arq.g2.controller.imagem;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/criar-imagem")
@MultipartConfig(
		  fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		  maxFileSize = 1024 * 1024 * 10,      // 10 MB
		  maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class CreateImagemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CreateImagemServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Part arquivoPart = request.getPart("imagem");
		String fileName = arquivoPart.getSubmittedFileName();

		for(Part part : request.getParts()) {
			part.write("C:\\Users\\ejunior\\OneDrive - beonup.com.br\\Documentos\\Faculdade\\projetos\\arqweb1-g2\\src\\main\\webapp\\img\\" + fileName);
		}
		response.getWriter().print("o arquivo foi incluido com sucesso!");
	}

}
