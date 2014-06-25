package beans;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Priv implements Serializable {


    private String        privName;
    private String        privDescrition;
    private ArrayList<Integer> groupIDs;
    private ArrayList<Integer> menuPaths;

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


	public ArrayList<Integer> getGroupIDs() {
		return groupIDs;
	}

	public void setGroupIDs(ArrayList<Integer> groupIDs) {
		this.groupIDs = groupIDs;
	}

	public ArrayList<Integer> getMenuPaths() {
		return menuPaths;
	}

	public void setMenuPaths(ArrayList<Integer> menuPaths) {
		this.menuPaths = menuPaths;
	}

}
