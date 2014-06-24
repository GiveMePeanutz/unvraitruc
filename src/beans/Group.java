package beans;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable{

	private int groupID;
	private String groupName;
	private String groupDescription;
	private List<User> users;
	private List<Priv> privs;
	
	public int getGroupID() {
		return groupID;
	}
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
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
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<Priv> getPrivs() {
		return privs;
	}
	public void setPrivs(List<Priv> privs) {
		this.privs = privs;
	}
	
	
}
