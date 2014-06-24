package beans;

import java.io.Serializable;
import java.util.List;

public class Priv implements Serializable {

	private int privID;
	private String privName;
	private String privDescrition;
	private List<Group> groups;
	private List<Integer> menus;
	
	public int getPrivID() {
		return privID;
	}
	public void setPrivID(int privID) {
		this.privID = privID;
	}
	public String getPrivName() {
		return privName;
	}
	public void setPrivName(String privName) {
		this.privName = privName;
	}
	public String getPrivDescription() {
		return privDescrition;
	}
	public void setPrivDescrition(String privDescrition) {
		this.privDescrition = privDescrition;
	}
	public List<Group> getGroups() {
		return groups;
	}
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	public List getMenus() {
		return menus;
	}
	public void setMenus(List menus) {
		this.menus = menus;
	}
	
	
}
