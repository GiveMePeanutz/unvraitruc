package servlets;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Course;
import beans.User;
import dao.CourseDao;
import dao.DAOFactory;

@WebServlet( "/displayCourses" )
public class DisplayCourses extends HttpServlet {

    public static final String CONF_DAO_FACTORY            = "daofactory";
    public static final String AVAILABLECOURSE_REQUEST_ATT = "availableCourses";
    public static final String USERCOURSE_REQUEST_ATT      = "userCourses";
    public static final String VIEW                        = "/WEB-INF/displayCourses.jsp";
    public static final String USER_SESSION_ATT            = "userSession";

    private CourseDao          courseDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.courseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = new User();
        user = (User) session.getAttribute( USER_SESSION_ATT );
        List<Course> listeCourses = courseDao.list();
        LinkedHashMap<String, Course> mapAvailableCourses = new LinkedHashMap<String, Course>();
        LinkedHashMap<String, Course> mapUserCourses = new LinkedHashMap<String, Course>();
        if ( user.getCourseNames().isEmpty() )
        {

            for ( Course course : listeCourses )
            {
                mapAvailableCourses.put( course.getCourseName(), course );
            }
        }
        else
        {
            List<String> userCourseList = user.getCourseNames();

            for ( String s : userCourseList ) {
                System.out.println( s );
            }

            for ( Course course : listeCourses ) {
                if ( userCourseList.contains( course.getCourseName() ) )
                {
                    mapUserCourses.put( course.getCourseName(), course );

                }
                else
                {
                    mapAvailableCourses.put( course.getCourseName(), course );
                }
            }
        }

        request.setAttribute( USERCOURSE_REQUEST_ATT, mapUserCourses );
        request.setAttribute( AVAILABLECOURSE_REQUEST_ATT, mapAvailableCourses );
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

    }

}