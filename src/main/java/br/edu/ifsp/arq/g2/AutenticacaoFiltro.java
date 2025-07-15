package br.edu.ifsp.arq.g2;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

import br.edu.ifsp.arq.g2.model.Usuario;

@WebFilter(urlPatterns = {"*.html", "*.jsp", "/*"},  dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class AutenticacaoFiltro implements Filter {
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	        throws IOException, ServletException {
	    HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) res;
	    String uri = request.getRequestURI();
	    String context = request.getContextPath();

	    boolean openPage = uri.equals(context + "/login.html")
	            || uri.equals(context + "/auth")
	            || uri.startsWith(context + "/resources/")
	            || uri.equals(context + "/")
	            || uri.equals(context + "/listar-noticia")
	            || uri.equals(context + "/addUsuario.html")
	            || uri.equals(context + "/criar-usuario")
	            || uri.equals(context + "/obter-avaliacao")
	            || uri.equals(context + "/obter-comentarios")
	            || uri.equals(context + "/index.html")
	            || uri.equals(context + "/viewNoticia.html")
	            || uri.contains("/assets/")
	            || uri.contains("/logout")
	            || uri.equals(context + "/sobre.html");
	    
	    HttpSession session = request.getSession(false);
	    boolean logged = (session != null && session.getAttribute("usuarioLogado") != null);

	    if (!openPage && !logged) {
	    	System.out.println("entrou!");
	        response.sendRedirect(context + "/login.html");
	        return;
	    }

	    if (logged) {
	        Usuario user = (Usuario) session.getAttribute("usuarioLogado");
	        boolean isAdmin = user.isAdmin();

	        boolean tryingToAccessCrud =
	               uri.contains("/addNoticia")
	            || uri.contains("/editNoticia") 
	            || uri.contains("/criar-noticia") 
	            || uri.contains("/editar-noticia") 
	            || uri.contains("/excluir-noticia")
	            || uri.contains("/addNoticia.jsp")
	            || uri.contains("/listUsuarios.jsp");

	        if (tryingToAccessCrud && !isAdmin) {
	            response.sendRedirect(context + "/erroPermissao.html");
	            return;
	        }
	    }


	    chain.doFilter(req, res);
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
}
