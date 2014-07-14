package servlets;

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
import dao.FactTableDao;
import dao.PrivDao;

@WebServlet( "/displayPrivs" )
public class DisplayPrivs extends HttpServlet {

    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String PRIV_REQUEST_ATT = "privs";
    public static final String VIEW             = "/WEB-INF/displayPrivs.jsp";
    public static final String ACTIVITY_NAME    = "displayPrivs";

    private PrivDao            privDao;
    private FactTableDao       FactTableDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.privDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPrivDao();
        this.FactTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        List<Priv> listPrivs = privDao.list();
        LinkedHashMap<String, Priv> mapPrivs = new LinkedHashMap<String, Priv>();
        for ( Priv priv : listPrivs ) {
            priv.convertPaths();
            mapPrivs.put( priv.getPrivName(), priv );
        }

        request.setAttribute( PRIV_REQUEST_ATT, mapPrivs );

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        List<Priv> listPrivs = privDao.list();
        LinkedHashMap<String, Priv> mapPrivs = new LinkedHashMap<String, Priv>();
        for ( Priv priv : listPrivs ) {
            priv.convertPaths();
            mapPrivs.put( priv.getPrivName(), priv );
        }

        request.setAttribute( PRIV_REQUEST_ATT, mapPrivs );

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

}