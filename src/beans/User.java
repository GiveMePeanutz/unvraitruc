package beans;

import java.io.Serializable;
import java.sql.Array;
import java.util.List;

import org.joda.time.DateTime;

public class User implements Serializable {

    private String   username;
    private String   password;
    private String   firstName;
    private String   lastName;
    private String   email;
    private String   phone;
    private String   photoURL;
    private String   address;
    private int      sex;
    private String   promotion;
    private DateTime regDate;
    private DateTime birthDate;
    private Array groups;
    private Array courses;
    

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

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion( String promotion ) {
        this.promotion = promotion;
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

	public Array getGroups() {
		return groups;
	}

	public void setGroups(Array groups) {
		this.groups = groups;
	}

	public Array getCourses() {
		return courses;
	}

	public void setCourses(Array courses) {
		this.courses = courses;
	}

}
