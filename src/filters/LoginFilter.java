package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import utilities.Encryption;
import beans.User;
import dao.DAOFactory;
import dao.FactTableDao;

public class LoginFilter implements Filter {

    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String USER_SESSION_ATT = "userSession";

    private FactTableDao       factTableDao;

    public void init( FilterConfig filterConfig ) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        this.factTableDao = ( (DAOFactory) servletContext.getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    @Override
    public void doFilter( ServletRequest req, ServletResponse res,
            FilterChain chain ) throws IOException, ServletException {
        Encryption enc = new Encryption();

        HttpServletRequest request = (HttpServletRequest) req;

        String path = request.getRequestURI().substring( request.getContextPath().length() );
        if ( path.startsWith( "/inc" ) ) {
            chain.doFilter( req, res );
            return;
        }

        HttpSession session = request.getSession();
        User user = new User();
        user = (User) session.getAttribute( USER_SESSION_ATT );
        if ( user == null ) {
            request.getRequestDispatcher( "/login" ).forward( req, res );
        } else {
            factTableDao.addFact( enc.decrypt( user.getUsername() ), path );
            chain.doFilter( req, res );
        }
    }

    public void destroy() {
    }

}