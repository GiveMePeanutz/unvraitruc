package servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet( urlPatterns = "/images/*", initParams = @WebInitParam( name = "path", value = "/files/images/" ) )
public class Image extends HttpServlet {
    public static final int TAILLE_TAMPON = 10240; // 10ko

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * Lecture du paramètre 'path' passé à la servlet via la déclaration
         * dans le web.xml
         */
        String path = this.getServletConfig().getInitParameter( "path" );

        /*
         * Récupération du path du file demandé au sein de l'URL de la requête
         */
        String requiredFile = request.getPathInfo();

        /* Vérifie qu'un file a bien été fourni */
        if ( requiredFile == null || "/".equals( requiredFile ) ) {
            /*
             * Si non, alors on envoie une erreur 404, qui signifie que la
             * ressource demandée n'existe pas
             */
            response.sendError( HttpServletResponse.SC_NOT_FOUND );
            return;
        }

        /*
         * Décode le nom de file récupéré, susceptible de contenir des espaces
         * et autres caractères spéciaux, et prépare l'objet File
         */
        requiredFile = URLDecoder.decode( requiredFile, "UTF-8" );
        File file = new File( path, requiredFile );

        /* Vérifie que le file existe bien */
        if ( !file.exists() ) {
            /*
             * Si non, alors on envoie une erreur 404, qui signifie que la
             * ressource demandée n'existe pas
             */
            response.sendError( HttpServletResponse.SC_NOT_FOUND );
            return;
        }

        /* Récupère le type du file */
        String type = getServletContext().getMimeType( file.getName() );

        /*
         * Si le type de file est inconnu, alors on initialise un type par
         * défaut
         */
        if ( type == null ) {
            type = "application/octet-stream";
        }

        /* Initialise la réponse HTTP */
        response.reset();
        response.setBufferSize( TAILLE_TAMPON );
        response.setContentType( type );
        response.setHeader( "Content-Length", String.valueOf( file.length() ) );
        response.setHeader( "Content-Disposition", "inline; filename=\"" + file.getName() + "\"" );

        /* Prépare les flux */
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            /* Ouvre les flux */
            in = new BufferedInputStream( new FileInputStream( file ), TAILLE_TAMPON );
            out = new BufferedOutputStream( response.getOutputStream(), TAILLE_TAMPON );

            /* Lit le file et écrit son contenu dans la réponse HTTP */
            byte[] buffer = new byte[TAILLE_TAMPON];
            int longueur;
            while ( ( longueur = in.read( buffer ) ) > 0 ) {
                out.write( buffer, 0, longueur );
            }
        } finally {
            try {
                out.close();
            } catch ( IOException ignore ) {
            }
            try {
                in.close();
            } catch ( IOException ignore ) {
            }
        }
    }
}