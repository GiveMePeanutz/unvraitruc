package beans;

import java.io.Serializable;
import java.sql.Array;
import java.util.List;

public class Priv implements Serializable {

    private int           privID;
    private String        privName;
    private String        privDescrition;
    private Array  		  groups;
    private Array 		  menus;

    public int getPrivID() {
        return privID;
    }

    public void setPrivID( int privID ) {
        this.privID = privID;
    }

    public String getPrivName() {
        return privName;
    }

    public void setPrivName( String privName ) {
        this.privName = privName;
    }

    public String getPrivDescription() {
        return privDescrition;
    }

    public void setPrivDescription( String privDescrition ) {
        this.privDescrition = privDescrition;
    }

    public Array getGroups() {
        return groups;
    }

    public void setGroups( Array groups ) {
        this.groups = groups;
    }

    public Array getMenus() {
        return menus;
    }

    public void setMenus( Array menus ) {
        this.menus = menus;
    }

}
