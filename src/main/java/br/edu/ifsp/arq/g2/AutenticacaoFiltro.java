package br.edu.ifsp.arq.g2;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

@WebFilter("/*")
public class AutenticacaoFiltro implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest  request  = (HttpServletRequest) req;
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
                        || uri.startsWith(context + "/viewNoticia.html")
                        || uri.contains("/assets/")
                        || uri.contains("/logout")
                        || uri.equals(context + "/sobre.html");
        
        System.out.println(openPage + " - " + uri);	

        HttpSession session = request.getSession(false);
        boolean logged = (session != null && session.getAttribute("usuarioLogado") != null);

        if (!openPage && !logged) {
            response.sendRedirect(context + "/login.html");
            return;
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
