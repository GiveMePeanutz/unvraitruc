package utilities;

public class Encryption {

    // encrypts a string into a other string stranger.
    public String encrypt( String password ) {
        String crypte = "";
        for ( int i = 0; i < password.length(); i++ ) {
            int c = password.charAt( i ) ^ 48;
            crypte = crypte + (char) c;
        }
        return crypte;
    }

    // decrypts a encrypt string
    public String decrypt( String password ) {
        String aCrypter = "";
        for ( int i = 0; i < password.length(); i++ ) {
            int c = password.charAt( i ) ^ 48;
            aCrypter = aCrypter + (char) c;
        }
        return aCrypter;
    }
}
