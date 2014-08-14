package dao;

import java.util.List;

public interface NaiveBayesDao {

    /*
     * Returns how many users are registered in database
     */
    int getUserCount() throws DAOException;

    /*
     * Returns all the ClassName of the database in the form of a list of String
     */
    List<String> listClassName() throws DAOException;

    /*
     * Returns all the Courses of the database in the form of a list of
     * String(the course name)
     */
    List<String> listCourse() throws DAOException;

    /*
     * Returns how many user_course line are registered in database (a line =
     * one course for one user)
     */
    int getUserCourseCount() throws DAOException;

}
