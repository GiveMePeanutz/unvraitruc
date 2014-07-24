package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOFactory;
import dao.DataWarehouseDao;

@WebServlet( "/updateDataWarehouseTemp" )
public class UpdateDataWarehouseTemp extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String VIEW             = "/dataWarehouse";
    public static final String USER_SESSION_ATT = "userSession";

    private DataWarehouseDao   dataWarehouseDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.dataWarehouseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getDataWarehouseDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        dataWarehouseDao.updateDWDim();

        response.sendRedirect( request.getContextPath() + VIEW );
    }

}
