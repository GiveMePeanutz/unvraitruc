package filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Course;
import beans.Date;
import beans.User;
import dao.CourseDao;
import dao.DAOFactory;
import dao.DateDao;
import dao.FactTableDao;



public class LoginFilter implements Filter {

	public static final String CONF_DAO_FACTORY  = "daofactory";
	public static final String USER_SESSION_ATT  = "userSession";
	
	private DateDao          dateDao;
    private FactTableDao       factTableDao;
	
	public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
		this.dateDao = ( (DAOFactory) servletContext.getAttribute( CONF_DAO_FACTORY ) ).getDateDao();
        this.factTableDao = ( (DAOFactory) servletContext.getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
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
        
		HttpSession session = request.getSession();
		User user = new User();
		user = (User) session.getAttribute( USER_SESSION_ATT );
		if(user==null){
			request.getRequestDispatcher("/login").forward( req, res);
		}else{
			System.out.println("/* filter   "+path);
			
			Date date = dateDao.create();
			factTableDao.addFact(user.getUsername(), path, date.getDateID());
			
			
			chain.doFilter(req, res);
		}
	}

	public void destroy() {
	}


}