package beans;

import java.io.Serializable;
import java.util.ArrayList;

import org.joda.time.DateTime;

public class User implements Serializable {

    // Bean properties
    private String            username;
    private String            password;
    private String            firstName;
    private String            lastName;
    private String            email;
    private String            phone;
    private String            photoURL;
    private String            address;
    private int               sex;
    private String            className;
    private DateTime          regDate;
    private DateTime          birthDate;
    private ArrayList<String> groupNames;
    private ArrayList<String> courseNames;

    // Contructor
    public User() {
        super();
        ArrayList<String> list = new ArrayList<String>();
        this.courseNames = list;

    }

    /*----------------------------------------GETTERS AND SETTERS ---------------------------------------------------------*/
    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL( String photoURL ) {
        this.photoURL = photoURL;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress( String address ) {
        this.address = address;
    }

    public int getSex() {
        return sex;
    }

    public void setSex( int sex ) {
        this.sex = sex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName( String className ) {
        this.className = className;
    }

    public DateTime getRegDate() {
        return regDate;
    }

    public void setRegDate( DateTime regDate ) {
        this.regDate = regDate;
    }

    public DateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate( DateTime d ) {
        this.birthDate = d;
    }

    public ArrayList<String> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames( ArrayList<String> groupNames ) {
        this.groupNames = groupNames;
    }

    public ArrayList<String> getCourseNames() {
        return courseNames;
    }

    public void setCourseNames( ArrayList<String> courseIDs ) {
        this.courseNames = courseIDs;
    }

    // Adds a course in user's course list
    public void addCourseName( String courseName ) {
        this.courseNames.add( courseName );
    }

    // Adds a group in user's group list
    public void addGroupName( String groupName ) {
        this.groupNames.add( groupName );
    }

    // Removes a course from user's course list
    public void removeCourse( String courseName ) {
        this.courseNames.remove( courseName );
    }

}
