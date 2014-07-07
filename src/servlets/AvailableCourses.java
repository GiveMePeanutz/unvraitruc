package servlets;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Course;
import dao.CourseDao;
import dao.DAOFactory;

@WebServlet( "/availableCourses" )
public class AvailableCourses extends HttpServlet {

    public static final String CONF_DAO_FACTORY   = "daofactory";
    public static final String COURSE_REQUEST_ATT = "courses";
    public static final String USERNAME_PARAM     = "username";
    public static final String VIEW               = "/WEB-INF/availableCourses.jsp";

    private CourseDao          courseDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.courseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        List<Course> listeCourses = courseDao.list();
        LinkedHashMap<String, Course> mapCourses = new LinkedHashMap<String, Course>();
        for ( Course course : listeCourses ) {
            mapCourses.put( course.getCourseName(), course );
        }

        request.setAttribute( COURSE_REQUEST_ATT, mapCourses );

        String username = getParameterValue( request, USERNAME_PARAM );
        request.setAttribute( USERNAME_PARAM, username );

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        /*
         * List<Course> listeCourses = courseDao.list(); Map<String, Course>
         * mapCourses = new HashMap<String, Course>(); for ( Course course :
         * listeCourses ) { mapCourses.put( course.getCourseName(), course ); }
         * 
         * request.setAttribute( COURSE_REQUEST_ATT, mapCourses );
         * 
         * this.getServletContext().getRequestDispatcher( VIEW ).forward(
         * request, response );
         */

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