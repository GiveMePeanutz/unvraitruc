package filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//Forbids access to every one who wants to delete a student but doesn't have the right privilege.
//Instead, the "not available page" will be displayed.

public class DeleteStudentFilter implements Filter {

    public static final String USER_SESSION_ACCESS_ATT = "userSessionAccess";

    public void init( FilterConfig config ) throws ServletException {

    }

    @Override
    public void doFilter( ServletRequest req, ServletResponse res,
            FilterChain chain ) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();

        List menus = ( (List<String>) session.getAttribute( USER_SESSION_ACCESS_ATT ) );
        if ( menus.contains( "Delete Student" ) ) {
            chain.doFilter( req, res );
            return;
        }
        request.getRequestDispatcher( "/notAvailable.jsp" ).forward( req, res );

    }

    public void destroy() {
    }

}