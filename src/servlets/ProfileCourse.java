package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Course;
import dao.CourseDao;
import dao.DAOFactory;
import dao.FactTableDao;

@WebServlet( "/profileCourse" )
public class ProfileCourse extends HttpServlet {

    public static final String CONF_DAO_FACTORY   = "daofactory";
    public static final String COURSENAME_PARAM   = "courseName";

    public static final String COURSE_REQUEST_ATT = "course";
    public static final String VIEW               = "/WEB-INF/profileCourse.jsp";
    public static final String ACTIVITY_NAME      = "profileCourse";

    private CourseDao          courseDao;
    private FactTableDao       FactTableDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.courseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
        this.FactTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        String courseName = getParameterValue( request, COURSENAME_PARAM );
        Course course = courseDao.find( courseName );
        request.setAttribute( COURSE_REQUEST_ATT, course );

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
