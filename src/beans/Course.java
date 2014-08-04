package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {

    // Bean properties
    private String            courseName;
    private String            courseDescription;
    private int               courseYear;
    private String            schedule;
    private String            teacher;
    private ArrayList<String> usernames;

    /*----------------------------------------GETTERS AND SETTERS ---------------------------------------------------------*/

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName( String courseName ) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription( String description ) {
        this.courseDescription = description;
    }

    public int getCourseYear() {
        return courseYear;
    }

    public void setCourseYear( int courseYear ) {
        this.courseYear = courseYear;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule( String schedule ) {
        this.schedule = schedule;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }

    public void setUsernames( ArrayList<String> strings ) {
        this.usernames = strings;
    }

    public void addUsername( String username ) {
        this.usernames.add( username );
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher( String teacher ) {
        this.teacher = teacher;
    }

}
