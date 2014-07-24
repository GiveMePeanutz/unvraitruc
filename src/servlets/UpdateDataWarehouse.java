package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;
import dao.DAOFactory;
import dao.DataWarehouseDao;
import dao.FactTableDao;

@WebServlet( "/updateDataWarehouse" )
public class UpdateDataWarehouse extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String VIEW             = "/dataWarehouse";
    public static final String USER_SESSION_ATT = "userSession";

    private DataWarehouseDao   dataWarehouseDao;
    private FactTableDao       factTableDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.dataWarehouseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getDataWarehouseDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        dataWarehouseDao.updateDW();

        HttpSession session = request.getSession();
        User userSession = new User();
        userSession = (User) session.getAttribute( USER_SESSION_ATT );
        factTableDao.addFact( userSession.getUsername(), "Count something" );

        response.sendRedirect( request.getContextPath() + VIEW );
    }

}
