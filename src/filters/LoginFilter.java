package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



public class LoginFilter implements Filter {

	
	
	public void init(FilterConfig config) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		
		
		
		HttpServletRequest request = (HttpServletRequest) req;
		
		String path = request.getRequestURI().substring( request.getContextPath().length() );
        if ( path.startsWith( "/inc" ) ) {
            chain.doFilter( req, res );
            return;
        }
        System.out.println("/* filter   "+path);
        
		HttpSession session = request.getSession();
		if((session.getAttribute("userSession")==null)){
			request.getRequestDispatcher("/login").forward( req, res);
		}else{
			
			chain.doFilter(req, res);
		}
	}

	public void destroy() {
	}


}