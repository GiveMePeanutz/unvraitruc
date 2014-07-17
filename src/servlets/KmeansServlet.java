package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.KmeansDataType;
import beans.Priv;
import dao.DAOFactory;
import dao.KmeansDataDao;
import dao.PrivDao;
import dataMining.Kmeans;

@WebServlet( "/kMeans" )
public class KmeansServlet extends HttpServlet{
	
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String VIEW             = "/WEB-INF/kMeans.jsp";
	
	public static final String KMEANS_DATA_ATT  = "kmeansData";
	
	private KmeansDataDao kmeansDataDao;
	
	public void init() throws ServletException {
        this.kmeansDataDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getKmeansDataDao();
        
    }
	
	public void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		KmeansDataType[] dataArray= kmeansDataDao.getUserActivity();
		Kmeans kmeans = new Kmeans(dataArray, 2);
		kmeans.calculateClusters();
		kmeans.displayClusters();
		
		ArrayList<KmeansDataType>[] clusteredData = kmeans.getClusters();
		
		request.setAttribute(KMEANS_DATA_ATT, clusteredData);
		this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
	}
	
	
	
}
