package beans;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable, Comparable<Group>{


	private String groupName;
	private String groupDescription;
	private ArrayList<String> usernames;
	private ArrayList<String> privNames;
	

	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupDescription() {
		return groupDescription;
	}
	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
	public ArrayList<String> getUsernames() {
		return usernames;
	}
	public void setUsernames(ArrayList<String> usernames) {
		this.usernames = usernames;
	}
	public ArrayList<String> getPrivNames() {
		return privNames;
	}
	public void setPrivNames(ArrayList<String> privNames) {
		this.privNames = privNames;
	}
	
	public void addUsername(String username){
		this.usernames.add(username);
	}
	
	public void addPrivName(String privName){
		this.usernames.add(privName);
	}
	
	public int compareTo(Group other) {
        return groupName.compareTo(other.groupName);
    }
	
	
}
