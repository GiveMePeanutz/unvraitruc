package beans;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import staticData.Menu;

public class Priv implements Serializable {


    private String        privName;
    private String        privDescrition;
    private ArrayList<String> groupNames;
    private ArrayList<Integer> menuPaths;
    private ArrayList<String> menuNames;

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


	public ArrayList<String> getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(ArrayList<String> groupNames) {
		this.groupNames = groupNames;
	}

	public ArrayList<Integer> getMenuPaths() {
		return menuPaths;
	}

	public void setMenuPaths(ArrayList<Integer> menuPaths) {
		this.menuPaths = menuPaths;
	}
	
	public void addMenuPath(int menuPath){
		this.menuPaths.add(menuPath);
	}
	
	public void addGroupName(String groupName){
		this.groupNames.add(groupName)
;	}

	public ArrayList<String> getMenuNames() {
		return menuNames;
	}

	public void setMenuNames(ArrayList<String> menuNames) {
		this.menuNames = menuNames;
	}
	
	public void convertPaths(){
		ArrayList<String> menuNames = new ArrayList<String>();
		for(int i : menuPaths){
			menuNames.add(Menu.getMenuName(i));
		}
		this.setMenuNames(menuNames);
	}

}
