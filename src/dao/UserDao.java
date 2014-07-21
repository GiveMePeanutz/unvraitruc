package dao;

import java.util.List;

import beans.User;

public interface UserDao {

    void create( User user ) throws DAOException;

    User find( String username ) throws DAOException;

    List<User> list() throws DAOException;

    List<User> listStudent() throws DAOException;

    List<User> listGroup( String group ) throws DAOException;

    void delete( String username ) throws DAOException;

    String getPassword( String username ) throws DAOException;

    List<String> listAccMenus( String username ) throws DAOException;

    void modify( User user ) throws DAOException;

    void addCourse( String username, String courseName ) throws DAOException;

    void deleteCourse( String username, String courseName ) throws DAOException;

}
