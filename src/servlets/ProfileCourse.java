package servlets;

//Controller of courses profile display 

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.UtilitiesForm;
import beans.Course;
import dao.CourseDao;
import dao.DAOFactory;

@WebServlet( "/profileCourse" )
public class ProfileCourse extends HttpServlet {

    public static final String CONF_DAO_FACTORY   = "daofactory";
    public static final String COURSENAME_PARAM   = "courseName";

    public static final String COURSE_REQUEST_ATT = "course";
    public static final String VIEW               = "/WEB-INF/profileCourse.jsp";

    private CourseDao          courseDao;
    private UtilitiesForm      util               = new UtilitiesForm();

    public void init() throws ServletException {

        this.courseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Course name parameter retrieving from URL.
        String courseName = util.getParameterValue( request, COURSENAME_PARAM );

        // Course informations retrieving, thanks to the course name and saving
        // in a request attribute
        Course course = courseDao.find( courseName );
        request.setAttribute( COURSE_REQUEST_ATT, course );

        // Course Profile is displayed
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

}
