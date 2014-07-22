package utilities;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    public String encrypt( String password, String key ) {
        try
        {
            Key clef = new SecretKeySpec( key.getBytes( "UTF-8" ), "Blowfish" );
            Cipher cipher = Cipher.getInstance( "Blowfish" );
            cipher.init( Cipher.ENCRYPT_MODE, clef );
            return new String( cipher.doFinal( password.getBytes() ) );
        } catch ( Exception e )
        {
            return null;
        }
    }

    public String encrypt( String password ) {
        String crypte = "";
        for ( int i = 0; i < password.length(); i++ ) {
            int c = password.charAt( i ) ^ 48;
            crypte = crypte + (char) c;
        }
        return crypte;
    }

    public String decrypt( String password, String key ) {
        try
        {
            Key clef = new SecretKeySpec( key.getBytes( "UTF-8" ), "Blowfish" );
            Cipher cipher = Cipher.getInstance( "Blowfish" );
            cipher.init( Cipher.DECRYPT_MODE, clef );
            return new String( cipher.doFinal( password.getBytes() ) );
        } catch ( Exception e )
        {
            System.out.println( e );
            return null;
        }
    }

    public String decrypt( String password ) {
        String aCrypter = "";
        for ( int i = 0; i < password.length(); i++ ) {
            int c = password.charAt( i ) ^ 48;
            aCrypter = aCrypter + (char) c;
        }
        return aCrypter;
    }
}
