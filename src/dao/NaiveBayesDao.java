package dao;

import java.util.List;

public interface NaiveBayesDao {

    int getUserCount() throws DAOException;

    List<String> listPromotion() throws DAOException;

    List<String> listCourse() throws DAOException;

    int getUserCourseCount() throws DAOException;

}
