package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Group;
import dao.DAOFactory;
import dao.FactTableDao;
import dao.GroupDao;

@WebServlet( "/profileGroup" )
public class ProfileGroup extends HttpServlet {

    public static final String CONF_DAO_FACTORY  = "daofactory";
    public static final String GROUPNAME_PARAM   = "groupName";

    public static final String GROUP_REQUEST_ATT = "group";
    public static final String VIEW              = "/WEB-INF/profileGroup.jsp";
    public static final String ACTIVITY_NAME     = "profileGroup";

    private GroupDao           groupDao;
    private FactTableDao       FactTableDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.groupDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getGroupDao();
        this.FactTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        String groupName = getParameterValue( request, GROUPNAME_PARAM );
        Group group = groupDao.find( groupName );

        request.setAttribute( GROUP_REQUEST_ATT, group );

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

    }

    private static String getParameterValue( HttpServletRequest request,
            String nomChamp ) {
        String value = request.getParameter( nomChamp );
        if ( value == null || value.trim().length() == 0 ) {
            return null;
        } else {
            return value;
        }
    }

}
