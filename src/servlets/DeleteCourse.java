package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import beans.Course;
import dao.DAOException;
import dao.DAOFactory;
import dao.CourseDao;

@WebServlet( "/deleteCourse" )
public class DeleteCourse extends HttpServlet{
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String COURSENAME_PARAM = "courseName";

	public static final String VIEW = "/displayCourses";

	private CourseDao CourseDao;

	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
		this.CourseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Récupération du paramètre */
		String courseName = getParameterValue(request, COURSENAME_PARAM);

		
		/* Si l'id du client et la Map des clients ne sont pas vides */
		if (courseName!= null) {
			try {
				/* Alors suppression du client de la BDD */
				CourseDao.delete(courseName);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}

		/* Redirection vers la fiche récapitulative */
		response.sendRedirect(request.getContextPath() + VIEW);
	}

	/*
	 * Méthode utilitaire qui retourne null si un paramètre est vide, et son
	 * contenu sinon.
	 */
	private static String getParameterValue(HttpServletRequest request,
			String nomChamp) {
		String value = request.getParameter(nomChamp);
		if (value == null || value.trim().length() == 0) {
			return null;
		} else {
			return value;
		}
	}
}