package beans;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable{


	private String groupName;
	private String groupDescription;
	private ArrayList<String> usernames;
	private ArrayList<Integer> privIDs;
	

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
	public ArrayList<Integer> getPrivIDs() {
		return privIDs;
	}
	public void setPrivIDs(ArrayList<Integer> privIDs) {
		this.privIDs = privIDs;
	}
	
	
}
