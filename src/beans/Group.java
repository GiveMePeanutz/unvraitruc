package beans;

import java.io.Serializable;
import java.sql.Array;
import java.util.List;

public class Group implements Serializable{

	private int groupID;
	private String groupName;
	private String groupDescription;
	private Array users;
	private Array privs;
	
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
	public Array getUsers() {
		return users;
	}
	public void setUsers(Array users) {
		this.users = users;
	}
	public Array getPrivs() {
		return privs;
	}
	public void setPrivs(Array privs) {
		this.privs = privs;
	}
	
	
}
