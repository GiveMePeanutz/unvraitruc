package dao;

import java.util.List;

public interface NaiveBayesDao {

    int getUserCount() throws DAOException;

    List<String> listClassName() throws DAOException;

    List<String> listCourse() throws DAOException;

    int getUserCourseCount() throws DAOException;

}
