package servlets;

//Controller of groups profile display 

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.UtilitiesForm;
import beans.Group;
import dao.DAOFactory;
import dao.GroupDao;

@WebServlet( "/profileGroup" )
public class ProfileGroup extends HttpServlet {

    public static final String CONF_DAO_FACTORY  = "daofactory";
    public static final String GROUPNAME_PARAM   = "groupName";

    public static final String GROUP_REQUEST_ATT = "group";
    public static final String VIEW              = "/WEB-INF/profileGroup.jsp";

    private GroupDao           groupDao;
    private UtilitiesForm      util              = new UtilitiesForm();

    public void init() throws ServletException {

        this.groupDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getGroupDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Group name parameter retrieving from URL.
        String groupName = util.getParameterValue( request, GROUPNAME_PARAM );

        // Group informations retrieving, thanks to the course name and saving
        // in a request attribute
        Group group = groupDao.find( groupName );
        request.setAttribute( GROUP_REQUEST_ATT, group );

        // Group Profile is displayed
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

}
