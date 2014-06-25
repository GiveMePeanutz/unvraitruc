package beans;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Course implements Serializable{
	
	private String courseName;
	private String description;
	private int courseYear;
	private String schedule;
	private ArrayList<String> usernames;
	

	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getDescription() {
		return description;
	}
	public void setCourseDescription(String description) {
		this.description = description;
	}
	public int getCourseYear() {
		return courseYear;
	}
	public void setCourseYear(int courseYear) {
		this.courseYear = courseYear;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public ArrayList<String> getUsernames() {
		return usernames;
	}
	public void setUsernames(ArrayList<String> usernames) {
		this.usernames = usernames;
	}
	
	
	
}
