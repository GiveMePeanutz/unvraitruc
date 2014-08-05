package servlets;

//Controller of groups display 

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Group;
import dao.DAOFactory;
import dao.GroupDao;

@WebServlet( "/displayGroups" )
public class DisplayGroups extends HttpServlet {

    public static final String CONF_DAO_FACTORY  = "daofactory";
    public static final String GROUP_REQUEST_ATT = "groups";
    public static final String VIEW              = "/WEB-INF/displayGroups.jsp";

    private GroupDao           groupDao;

    public void init() throws ServletException {

        this.groupDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getGroupDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Group list retrieving and saving in the request as a
        // LinkedHashMap(key = group name)
        List<Group> listeGroups = groupDao.list();
        LinkedHashMap<String, Group> mapGroups = new LinkedHashMap<String, Group>();
        for ( Group group : listeGroups )
        {
            mapGroups.put( group.getGroupName(), group );
        }
        request.setAttribute( GROUP_REQUEST_ATT, mapGroups );

        // Group list display
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

}