package servlets;

//Controller of privileges display 

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Priv;
import dao.DAOFactory;
import dao.PrivDao;

@WebServlet( "/displayPrivs" )
public class DisplayPrivs extends HttpServlet {

    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String PRIV_REQUEST_ATT = "privs";
    public static final String VIEW             = "/WEB-INF/displayPrivs.jsp";

    private PrivDao            privDao;

    public void init() throws ServletException {

        this.privDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPrivDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Pivilege list retrieving and saving in the request as a
        // LinkedHashMap(key = privilege name)
        List<Priv> listPrivs = privDao.list();
        LinkedHashMap<String, Priv> mapPrivs = new LinkedHashMap<String, Priv>();
        for ( Priv priv : listPrivs )
        {
            priv.convertPaths();
            mapPrivs.put( priv.getPrivName(), priv );
        }
        request.setAttribute( PRIV_REQUEST_ATT, mapPrivs );

        // Pivilege list display
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

}