package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import beans.Course;
import dao.CourseDao;
import dao.DAOFactory;
import forms.LoginForm;
import beans.Course;

@WebServlet( "/displayCourses" )
public class DisplayCourses extends HttpServlet{
	
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String COURSE_REQUEST_ATT = "courses";
	public static final String VIEW= "/WEB-INF/displayCourses.jsp";
	
	private CourseDao courseDao;
	
	 public void init() throws ServletException {
	        /* Récupération d'une instance de notre DAO Utilisateur */
	        this.courseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
	    }
	
	 
	 public void doGet (HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
	 
		List<Course> listeCourses = courseDao.list();
		LinkedHashMap<String, Course> mapCourses = new LinkedHashMap<String, Course>();
		for (Course course : listeCourses) {
			mapCourses.put(course.getCourseName(), course);
		}
		
		
		request.setAttribute(COURSE_REQUEST_ATT, mapCourses);

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
		 
	 }
		 
	 public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

		List<Course> listeCourses = courseDao.list();
		Map<String, Course> mapCourses = new HashMap<String, Course>();
		for (Course course : listeCourses) {
			mapCourses.put(course.getCourseName(), course);
		}
		
		
		request.setAttribute(COURSE_REQUEST_ATT, mapCourses);

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    
	}
	 
	
	
}