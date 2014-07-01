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

import beans.Priv;
import dao.DAOException;
import dao.DAOFactory;
import dao.PrivDao;

@WebServlet( "/deletePriv" )
public class DeletePriv extends HttpServlet{
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String PRIVNAME_PARAM = "privName";

	public static final String VIEW = "/displayPrivs";

	private PrivDao PrivDao;

	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
		this.PrivDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPrivDao();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Récupération du paramètre */
		String privName = getParameterValue(request, PRIVNAME_PARAM);

		
		/* Si l'id du client et la Map des clients ne sont pas vides */
		if (privName!= null) {
			try {
				/* Alors suppression du client de la BDD */
				PrivDao.delete(privName);
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