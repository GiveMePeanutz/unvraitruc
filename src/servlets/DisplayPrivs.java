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

import beans.Priv;
import dao.GroupDao;
import dao.DAOFactory;
import dao.PrivDao;
import forms.LoginForm;

@WebServlet( "/displayPrivs" )
public class DisplayPrivs extends HttpServlet{
	
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String PRIV_REQUEST_ATT = "privs";
	public static final String VIEW= "/WEB-INF/displayPrivs.jsp";
	
	private PrivDao privDao;
	
	 public void init() throws ServletException {
	        /* Récupération d'une instance de notre DAO Utilisateur */
	        this.privDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPrivDao();
	    }
	
	 
	 public void doGet (HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
	 
		List<Priv> listPrivs = privDao.list();
		Map<String, Priv> mapPrivs = new HashMap<String, Priv>();
		for (Priv priv : listPrivs) {
			mapPrivs.put(priv.getPrivName(), priv);
		}
		
		
		request.setAttribute(PRIV_REQUEST_ATT, mapPrivs);

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
		 
	 }
		 
	 public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

		List<Priv> listPrivs = privDao.list();
		Map<String, Priv> mapPrivs = new HashMap<String, Priv>();
		for (Priv priv : listPrivs) {
			mapPrivs.put(priv.getPrivName(), priv);
		}
		
		
		request.setAttribute(PRIV_REQUEST_ATT, mapPrivs);

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    
	}
	 
	
	
}