package servlets;

//Controller to handle image uploads. 

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

@WebServlet( urlPatterns = "/images/*", initParams = @WebInitParam( name = "path", value = "files/images/" ) )
public class Image extends HttpServlet {
    public static final int TAILLE_TAMPON = 10240; // 10ko

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // path = urlPatterns annotation parameter
        String path = this.getServletConfig().getInitParameter( "path" );

        // File Path retrieving from the request
        String requiredFile = request.getPathInfo();

        if ( requiredFile == null || "/".equals( requiredFile ) ) {
            // If the file hasn't been provided, returns a error 404
            response.sendError( HttpServletResponse.SC_NOT_FOUND );
            return;
        }

        // Decode the file name (spaces and specials characters) and prepare the
        // File object
        requiredFile = URLDecoder.decode( requiredFile, "UTF-8" );
        File file = new File( path, requiredFile );

        if ( !file.exists() ) {
            // If the file doesn't exist, returns a error 404
            response.sendError( HttpServletResponse.SC_NOT_FOUND );
            return;
        }

        // File Type retrieving
        String type = getServletContext().getMimeType( file.getName() );
        // If it's a unknown type, initializes a default type
        if ( type == null ) {
            type = "application/octet-stream";
        }

        /* Initialize HTTP response */
        response.reset();
        response.setBufferSize( TAILLE_TAMPON );
        response.setContentType( type );
        response.setHeader( "Content-Length", String.valueOf( file.length() ) );
        response.setHeader( "Content-Disposition", "inline; filename=\"" + file.getName() + "\"" );

        // Prepares the streams
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try
        {

            // Opens the streams
            in = new BufferedInputStream( new FileInputStream( file ), TAILLE_TAMPON );
            out = new BufferedOutputStream( response.getOutputStream(), TAILLE_TAMPON );

            // Reads the file and writes its content on the HTTP response
            byte[] buffer = new byte[TAILLE_TAMPON];
            int longueur;
            while ( ( longueur = in.read( buffer ) ) > 0 ) {
                out.write( buffer, 0, longueur );
            }
        } finally
        {
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