package dao;

import static dao.DAOUtility.fermeturesSilencieuses;
import static dao.DAOUtility.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import staticData.Menu;
import beans.User;

public class UserDaoImpl implements UserDao {

    private DAOFactory          daoFactory;

    private static final String SQL_SELECT                = "SELECT username, address, birthDate, email, firstName, lastName, password, phone, photoURL, className, regDate, sex FROM User ORDER BY username";
    private static final String SQL_SELECT_TEACHERS       = "SELECT username, address, birthDate, email, firstName, lastName, password, phone, photoURL, className, regDate, sex FROM User WHERE username IN (SELECT username FROM user_group WHERE groupname='teacher') ORDER BY username";
    private static final String SQL_SELECT_STUDENTS       = "SELECT username, address, birthDate, email, firstName, lastName, password, phone, photoURL, className, regDate, sex FROM User WHERE username IN (SELECT username FROM user_group WHERE groupname='student') ORDER BY username";
    private static final String SQL_SELECT_BY_USERNAME    = "SELECT username, address, birthDate, email, firstName, lastName, password, phone, photoURL, className, regDate, sex FROM User WHERE username = ?";
    private static final String SQL_SELECT_USER_COURSES   = "SELECT courseName FROM course WHERE courseName IN (SELECT courseName FROM user_course WHERE username = ?) ORDER BY courseName";
    private static final String SQL_SELECT_USER_GROUPS    = "SELECT groupName FROM web_app_db.group WHERE groupName IN (SELECT groupName FROM user_group WHERE username = ?) ORDER BY groupName";
    private static final String SQL_SELECT_USER_ACC_MENUS = "SELECT DISTINCT menuPath from priv_menu where privname IN (SELECT privName FROM group_priv WHERE groupname IN ( SELECT groupName FROM user_group WHERE username = ? ))";

    private static final String SQL_MODIFY_USER           = "UPDATE user SET password=?, firstName=?, lastName=?, email=?, phone=?, photoURL=?, address=?, sex=?, birthDate=?, className=? WHERE username=?";

    private static final String SQL_INSERT                = "INSERT INTO User (username, address, birthDate, email, firstName, lastName, password, phone, photoURL, className, sex, regDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_USER_COURSE    = "INSERT INTO user_course (username , courseName) VALUES (? , ?)";
    private static final String SQL_INSERT_USER_GROUP     = "INSERT INTO user_group (username , groupName) VALUES (? , ?)";

    private static final String SQL_DELETE_BY_USERNAME    = "DELETE FROM User WHERE username = ?";
    private static final String SQL_DELETE_USER_GROUPS    = "DELETE FROM user_group WHERE username = ?";
    private static final String SQL_DELETE_USER_COURSE    = "DELETE FROM user_course WHERE username = ? AND courseName=?";

    UserDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /*
	 * Creates a user in the database using as parameter a user bean 
	 */
    @Override
    public void create( User user ) throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement3 = null;

        try {
            // Retrieves a connection from the factory 
        	connection = daoFactory.getConnection();
            
        	// prepared statement of the insert query. This query creates a new user in the table with the
        	// information from the user bean
        	preparedStatement1 = initialisationRequetePreparee( connection,
                    SQL_INSERT, true, user.getUsername(), user.getAddress(), new Timestamp( user.getBirthDate()
                            .getMillis() ), user.getEmail(), user.getFirstName(), user.getLastName(),
                    user.getPassword(), user.getPhone(), user.getPhotoURL(), user.getClassName(), user.getSex(),
                    new Timestamp( user.getRegDate().getMillis() ) );
            int statut1 = preparedStatement1.executeUpdate();
            
            // If the query failed
            if ( statut1 == 0 ) {
                throw new DAOException(
                        "Failed to create client. No row added" );
            }
            
            // Here we insert in the database all the user groups that are present in the bean
            for ( String groupName : user.getGroupNames() ) {
                preparedStatement3 = initialisationRequetePreparee( connection,
                        SQL_INSERT_USER_GROUP, true, user.getUsername(), groupName );
                int statut3 = preparedStatement3.executeUpdate();
                if ( statut3 == 0 ) {
                    throw new DAOException(
                            "Failed to create user-group association. No row added" );
                }
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1,
                    connection );
            fermeturesSilencieuses( preparedStatement3,
                    connection );
        }
    }

    /*
	 * Returns a course bean containing all the information of the course whose courseName is passed as parameter
     */
    @Override
    public User find( String username ) throws DAOException {
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet resultSet3 = null;

        User user = null;
        String sql = SQL_SELECT_BY_USERNAME;
        String sql2 = SQL_SELECT_USER_COURSES;
        String sql3 = SQL_SELECT_USER_GROUPS;

        try {
            /* Retrieves a connection from the factory */
            connection = daoFactory.getConnection();
            
            /* prepared statements of the queries that :
             * - 1 : retrieves user information from user table
             * - 2 : retrieves user courses from user_course table
             * - 3 : retrieves user groups from the user_group table
             */
            preparedStatement = initialisationRequetePreparee( connection, sql, false, username );
            resultSet = preparedStatement.executeQuery();
            preparedStatement2 = initialisationRequetePreparee( connection, sql2, false, username );
            resultSet2 = preparedStatement2.executeQuery();
            preparedStatement3 = initialisationRequetePreparee( connection, sql3, false, username );
            resultSet3 = preparedStatement3.executeQuery();
            
            // if a user is retrieved from the database, the resultSets are custom mapped into a group bean
            if ( resultSet.next() ) {
                user = map( resultSet, resultSet2, resultSet3 );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
            fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
            fermeturesSilencieuses( resultSet3, preparedStatement3, connection );
        }

        // returns the user bean filled with the queried information
        return user;
    }

    /*
	 * Returns all the users of the database in the form of a list of user beans
     */
    @Override
    public List<User> list() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        ResultSet resultSet3 = null;
        List<User> users = new ArrayList<User>();

        try {
            
            /* Retrieves a connection from the factory */
        	connection = daoFactory.getConnection();
            
        	// prepared statement of the query that retrieves all the users from the database
        	preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();

            /* For every user retrieved with the first query and stored in ResutSet :
             * - we query the database to retrieve the associated courses and groups
             * - we store the retrieved information in a bean thanks to the custom map method
             * - add this bean to the list
             */
            while ( resultSet.next() ) {
                preparedStatement2 = initialisationRequetePreparee( connection, SQL_SELECT_USER_COURSES, false,
                        resultSet.getString( "username" ) );
                resultSet2 = preparedStatement2.executeQuery();
                preparedStatement3 = initialisationRequetePreparee( connection, SQL_SELECT_USER_GROUPS, false,
                        resultSet.getString( "username" ) );
                resultSet3 = preparedStatement3.executeQuery();
                users.add( map( resultSet, resultSet2, resultSet3 ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
            fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
            fermeturesSilencieuses( resultSet3, preparedStatement3, connection );
        }

        //returns the list of user beans 
        return users;
    }

    /*
	 * Returns all the users of the database in the form of a list of user beans
	 * whose group corresponds to the String passed as parameter.
	 * Very similar to the previous List method
     */
    @Override
    public List<User> listGroup( String group ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        ResultSet resultSet3 = null;
        List<User> users = new ArrayList<User>();

        try {
            /* Retrieves a connection from the factory */
        	connection = daoFactory.getConnection();
            
        	// Here we query the database depending on the wanted group
        	if ( group.equalsIgnoreCase( "teacher" ) ) {
                preparedStatement = connection.prepareStatement( SQL_SELECT_TEACHERS );
            }
            else if ( group.equalsIgnoreCase( "student" ) ) {
                preparedStatement = connection.prepareStatement( SQL_SELECT_STUDENTS );
            }
            else {
                preparedStatement = connection.prepareStatement( SQL_SELECT );
            }
            resultSet = preparedStatement.executeQuery();

            /* For every user retrieved with the first query and stored in ResutSet :
             * - we query the database to retrieve the associated courses and groups
             * - we store the retrieved information in a bean thanks to the custom map method
             * - add this bean to the list
             */
            while ( resultSet.next() ) {
                preparedStatement2 = initialisationRequetePreparee( connection, SQL_SELECT_USER_COURSES, false,
                        resultSet.getString( "username" ) );
                resultSet2 = preparedStatement2.executeQuery();
                preparedStatement3 = initialisationRequetePreparee( connection, SQL_SELECT_USER_GROUPS, false,
                        resultSet.getString( "username" ) );
                resultSet3 = preparedStatement3.executeQuery();
                users.add( map( resultSet, resultSet2, resultSet3 ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
            fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
            fermeturesSilencieuses( resultSet3, preparedStatement3, connection );
        }

        //returns the list of group beans 
        return users;
    }

    /*
     * This method should not exist anymore
     */
    public List<User> listStudent() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        ResultSet resultSet3 = null;
        List<User> students = new ArrayList<User>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT_STUDENTS );
            resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ) {
                preparedStatement2 = initialisationRequetePreparee( connection, SQL_SELECT_USER_COURSES, false,
                        resultSet.getString( "username" ) );
                resultSet2 = preparedStatement2.executeQuery();
                preparedStatement3 = initialisationRequetePreparee( connection, SQL_SELECT_USER_GROUPS, false,
                        resultSet.getString( "username" ) );
                resultSet3 = preparedStatement3.executeQuery();
                students.add( map( resultSet, resultSet2, resultSet3 ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
            fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
            fermeturesSilencieuses( resultSet3, preparedStatement3, connection );
        }

        return students;
    }

    /*
	 * Deletes from the database the user whose username is passed as parameter
     */
    @Override
    public void delete( String username ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        try {
            /* Retrieves a connection from the factory */
        	connexion = daoFactory.getConnection();
            
            // prepared statement of the deletion query
        	preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_BY_USERNAME, true, username );
            int statut = preparedStatement.executeUpdate();
            
            // If the deletion failed
            if ( statut == 0 ) {
                throw new DAOException( "Failed to delete user, no row deleted." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }

    }
    

    /*
     *  Retrieves from the database the password of the user whose username is passed as parameter
     */
    @Override
    public String getPassword( String username ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = SQL_SELECT_BY_USERNAME;
        ResultSet resultSet = null;
        String pw = null;

        try {
            /* Retrieves a connection from the factory */
            connection = daoFactory.getConnection();

            // Prepared statement of the query in charge of retrieving the password of 
            // the user whose username is passed as parameter
            preparedStatement = initialisationRequetePreparee( connection, sql, false, username );
            resultSet = preparedStatement.executeQuery();

            // Retrieve the password from the resultSet
            if ( resultSet.next() ) {
                pw = resultSet.getString( "password" );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        // Returns retrieved password
        return pw;
    }

    /*
     *  Retrieves as a List of Strings the names of the menues the user whose username is passed as 
     *  parameter is authorized to access
     */
    @Override
    public List<String> listAccMenus( String username ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<String> menus = new ArrayList<String>();

        try {
            
            /* Retrieves a connection from the factory */
        	connection = daoFactory.getConnection();
            
        	// Prepared statement of the query in charge of retrieving all the menu Paths the user whose username is passed
        	// as parameter can access
        	preparedStatement = initialisationRequetePreparee( connection, SQL_SELECT_USER_ACC_MENUS, false, username );
            resultSet = preparedStatement.executeQuery();
            
            // All the retrieved menu paths are added to the list
            while ( resultSet.next() ) {
                menus.add( Menu.getMenuName( Integer.parseInt( resultSet.getString( "menuPath" ) ) ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        // Returns the list of all menu Paths the user has access to
        return menus;
    }

    /*
	 * Modifies the user in the database with the corresponding userName extracted from the bean passed as argument
     */
    @Override
    public void modify( User user ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;

        try {
            /* Retrieves a connection from the factory */
        	connection = daoFactory.getConnection();
        	
        	// prepared statement of the modify user query. This query modifies the user whose username has been
         	// extracted from the bean passed as parameter. It modifies the remaining info from the bean.
            preparedStatement1 = initialisationRequetePreparee( connection,
                    SQL_MODIFY_USER, true, user.getPassword(), user.getFirstName(), user.getLastName(),
                    user.getEmail(), user.getPhone(), user.getPhotoURL(), user.getAddress(), user.getSex(),
                    new Timestamp( user.getBirthDate().getMillis() ), user.getClassName(), user.getUsername() );
            int statut1 = preparedStatement1.executeUpdate();
            if ( statut1 == 0 ) {
                throw new DAOException(
                        "Failed to modify client. No row modified" );
            }

            // Here we delete all the user/group associations
            preparedStatement2 = initialisationRequetePreparee( connection, SQL_DELETE_USER_GROUPS, true,
                    user.getUsername() );
            int statut2 = preparedStatement2.executeUpdate();
            if ( statut2 == 0 ) {
                throw new DAOException(
                        "Failed to delete client groups. No rows deleted" );
            }

            // Here we create all the user/group associations
            for ( String groupName : user.getGroupNames() ) {
                preparedStatement3 = initialisationRequetePreparee( connection,
                        SQL_INSERT_USER_GROUP, true, user.getUsername(), groupName );
                int statut3 = preparedStatement3.executeUpdate();
                if ( statut3 == 0 ) {
                    throw new DAOException(
                            "Failed to create user-group association. No row added" );
                }
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1,
                    connection );
            fermeturesSilencieuses( preparedStatement2,
                    connection );
            fermeturesSilencieuses( preparedStatement3,
                    connection );
        }
    }

    /*
     * Add the course whose courseName is passed as parameter to the user whose username is
     * also passed as parameter
     */
    @Override
    public void addCourse( String username, String courseName )
            throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement1 = null;

        try {
            /* Retrieves a connection from the factory */
            connection = daoFactory.getConnection();
            
            // Prepared statement of the query in charge of creating an association between
            // the user and the course whose username and courseName are passed as parameters
            preparedStatement1 = initialisationRequetePreparee( connection,
                    SQL_INSERT_USER_COURSE, true, username, courseName );
            int statut1 = preparedStatement1.executeUpdate();
            
            // If the insert has failed
            if ( statut1 == 0 ) {
                throw new DAOException(
                        "Failed to create user-course association. No row added" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1,
                    connection );
        }
    }

    /*
     * Deletes from the database the user/course association between the course and user whose username and courseNames 
     * are passed as parameters
     */
    @Override
    public void deleteCourse( String username, String courseName )
            throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        try {
            /* Retrieves a connection from the factory */
        	connexion = daoFactory.getConnection();
        	
        	// Prepared statement of the query in charge of deleting the user whose username is passed
        	// as parameter
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_USER_COURSE, true, username,
                    courseName );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Failed to delete user_course association, no row deleted." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }

    }

    /*
	 * Static method taking as parameter 3 resultSets :
	 * - 1 : ResultSet of the select user query
	 * - 2 : ResultSet of the select user courses query
	 * - 3 : ResultSet of the select user groups query
	 */
    private static User map( ResultSet resultSet1, ResultSet resultSet2, ResultSet resultSet3 ) throws SQLException {

        User user = new User();
        user.setUsername( resultSet1.getString( "username" ) );
        user.setAddress( resultSet1.getString( "address" ) );
        user.setBirthDate( new DateTime( resultSet1.getTimestamp( "birthDate" ) ) );
        user.setEmail( resultSet1.getString( "email" ) );
        user.setFirstName( resultSet1.getString( "firstName" ) );
        user.setLastName( resultSet1.getString( "lastName" ) );
        user.setPassword( resultSet1.getString( "password" ) );
        user.setPhone( resultSet1.getString( "phone" ) );
        user.setPhotoURL( resultSet1.getString( "photoURL" ) );
        user.setClassName( resultSet1.getString( "className" ) );
        user.setRegDate( new DateTime( resultSet1.getTimestamp( "regDate" ) ) );
        user.setSex( resultSet1.getInt( "sex" ) );

        ArrayList<String> courseNames = new ArrayList<String>();
        resultSet2.beforeFirst();
        while ( resultSet2.next() ) {
            courseNames.add( resultSet2.getString( "courseName" ) );
        }
        user.setCourseNames( courseNames );

        ArrayList<String> groupNames = new ArrayList<String>();
        resultSet3.beforeFirst();
        while ( resultSet3.next() ) {
            groupNames.add( resultSet3.getString( "groupName" ) );
        }
        user.setGroupNames( groupNames );

        return user;
    }

}
