package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Group;
import dao.DAOFactory;
import dao.FactTableDao;
import dao.GroupDao;

@WebServlet( "/displayGroups" )
public class DisplayGroups extends HttpServlet {

    public static final String CONF_DAO_FACTORY  = "daofactory";
    public static final String GROUP_REQUEST_ATT = "groups";
    public static final String VIEW              = "/WEB-INF/displayGroups.jsp";

    public static final String ACTIVITY_NAME     = "displayGroups";

    private GroupDao           groupDao;
    private FactTableDao       FactTableDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.groupDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getGroupDao();
        this.FactTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        List<Group> listeGroups = groupDao.list();
        LinkedHashMap<String, Group> mapGroups = new LinkedHashMap<String, Group>();

        for ( Group group : listeGroups ) {
            mapGroups.put( group.getGroupName(), group );
        }

        request.setAttribute( GROUP_REQUEST_ATT, mapGroups );

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        List<Group> listeGroups = groupDao.list();
        Map<String, Group> mapGroups = new HashMap<String, Group>();
        for ( Group group : listeGroups ) {
            mapGroups.put( group.getGroupName(), group );
        }

        request.setAttribute( GROUP_REQUEST_ATT, mapGroups );

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

}