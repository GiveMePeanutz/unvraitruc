package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import beans.Group;
import dao.GroupDao;
import dao.DAOFactory;
import forms.LoginForm;
import beans.Group;

@WebServlet( "/displayGroups" )
public class DisplayGroups extends HttpServlet{
	
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String GROUP_REQUEST_ATT = "groups";
	public static final String VIEW= "/WEB-INF/displayGroups.jsp";
	
	private GroupDao groupDao;
	
	 public void init() throws ServletException {
	        /* Récupération d'une instance de notre DAO Utilisateur */
	        this.groupDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getGroupDao();
	    }
	
	 
	 public void doGet (HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
	 
		List<Group> listeGroups = groupDao.list();
		Map<String, Group> mapGroups = new HashMap<String, Group>();
		for (Group group : listeGroups) {
			mapGroups.put(group.getGroupName(), group);
		}
		
		
		request.setAttribute(GROUP_REQUEST_ATT, mapGroups);

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
		 
	 }
		 
	 public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

		List<Group> listeGroups = groupDao.list();
		Map<String, Group> mapGroups = new HashMap<String, Group>();
		for (Group group : listeGroups) {
			mapGroups.put(group.getGroupName(), group);
		}
		
		
		request.setAttribute(GROUP_REQUEST_ATT, mapGroups);

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    
	}
	 
	
	
}