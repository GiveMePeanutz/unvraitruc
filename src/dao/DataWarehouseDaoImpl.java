package dao;

import static dao.DAOUtility.fermeturesSilencieuses;
import static dao.DAOUtility.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;

public class DataWarehouseDaoImpl implements DataWarehouseDao {

    private DAOFactory          daoFactory;

    private static List<String> months                 = Arrays.asList( "All", "January", "February", "March", "April",
                                                               "May",
                                                               "June", "July", "August", "September", "October",
                                                               "November", "December" );
    private static List<String> days                   = Arrays.asList( "All", "Monday", "Tuesday", "Wednesday",
                                                               "Thursday",
                                                               "Friday", "Saturday", "Sunday" );

    private static String       SELECT_DISTINCT_YEAR   = "SELECT DISTINCT year FROM timeDim";
    private static String       SELECT_DISTINCT_GROUP  = "SELECT DISTINCT groupName FROM groupDim";
    private static String       SELECT_NEW_YEARS       = "SELECT DISTINCT date_format(factDate,'%Y') AS year FROM transaction_table WHERE factDate>(SELECT MAX(updateTime) FROM updateTime)";
    private static String       SELECT_NEW_GROUPS      = "SELECT DISTINCT groupName FROM user_group WHERE groupName NOT IN ( SELECT DISTINCT groupName FROM GroupDim)";

    private static String       SELECT_USERDIM_ID      = "SELECT DISTINCT userId FROM userDim ud, groupDim gd WHERE ud.sex=? AND gd.groupName=? AND ud.groupId=gd.groupId";
    private static String       SELECT_TIMEDIM_ID_H1   = "SELECT DISTINCT timeId FROM timeDim td WHERE td.hour like ? AND td.day like ?  AND td.monthName like ? AND td.year like ?";
    private static String       SELECT_TIMEDIM_ID_H2   = "SELECT DISTINCT timeId FROM timeDim td WHERE td.dayName like ?  AND td.week like ? AND td.year like ?";
    private static String       SELECT_ACTIVITYDIM_ID  = "SELECT DISTINCT activityId FROM activityDim ad WHERE ad.isAction like ?";

    private static String       SELECT_NEW_LOGS_H1     = "SELECT u.sex AS sex, ug.groupName AS groupName, IF( pageName like '/%', 0,1) AS isAction,  date_format(factDate,'%H') AS hour, date_format(factDate,'%d') AS day, date_format(factDate,'%M') AS monthName, date_format(factDate,'%Y') AS year, count(*) AS count FROM transaction_table, user_group ug, user u WHERE transaction_table.username=ug.username AND ug.username=u.username AND factDate>(SELECT MAX(updateTime) FROM updateTime) GROUP BY sex, groupName, isAction, hour, day, monthName, year";
    private static String       SELECT_NEW_LOGS_H2     = "SELECT u.sex AS sex, ug.groupName AS groupName, IF( pageName like '/%', 0,1) AS isAction,  date_format(factDate,'%W') AS dayName,  date_format(factDate,'%u') AS week, date_format(factDate,'%Y') AS year, count(*) AS count FROM transaction_table, user_group ug, user u WHERE transaction_table.username=ug.username AND ug.username=u.username AND factDate>(SELECT MAX(updateTime) FROM updateTime) GROUP BY sex, groupName, isAction, dayName, week, year;";

    private static String       INSERT_TIME_TIMEDIM    = "INSERT INTO timeDim (year,monthName,week,dayName,day,hour) values (?,?,?,?,?,?)";
    private static String       INSERT_GROUP_GROUPDIM  = "INSERT INTO groupDim (groupName) VALUES (?)";
    private static String       INSERT_USER_USERDIM    = "INSERT INTO userDim (sex, groupID) VALUES (?,?)";

    private static String       INSERT_UPDATE_DATETIME = "INSERT INTO updateTime (updateTime)  VALUES (NOW())";
    private static String       INSERT_DWFACT          = "INSERT INTO dwFactTable (userID, activityId, timeId, count) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE count = count+?";

    private static String       FIND_RESULT            = "SELECT count FROM dwFactTable dwft, userDim ud, groupDim gd, timeDim td, activityDim ad WHERE dwft.userId=ud.userId AND dwft.timeId=td.timeID AND dwft.activityId=ad.activityId AND ud.groupID=gd.groupId AND ud.sex= ? AND gd.groupName = ? AND ad.isAction = ? AND td.hour = ? AND td.day = ? AND td.dayName= ? AND td.week = ? AND td.monthName = ? AND td.year = ?";

    DataWarehouseDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /*
	 *  Method that updates the dataWarehouse
	 *  Uses updateDWDim() and updateDWFactTable()
	 */
    @Override
    public void updateDW() throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement1 = null;

        try {

        	// Retrieves connection from the factory
            connexion = daoFactory.getConnection();

            // DW Dim tables update
            updateDWDim();

            // DW Fact table update
            updateDWFactTable();

            // Once the Datawarehouse is updated we insert a datetime indicating the last time the 
            // Data warehouse was updated.
            preparedStatement1 = connexion.prepareStatement( INSERT_UPDATE_DATETIME );
            preparedStatement1.executeUpdate();

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1, connexion );
        }

    }

    /*
     * Calls the update methods of all dimensions that can evolve
     */
    @Override
    public void updateDWDim() throws DAOException {

        updateTimeDim();

        updateUserGroupDim();

    }

    /*
	 *  Method that updates the dataWarehouse fact table
	 *  Should not be called separately from updateDWDim()
	 *  Used for testing
	 */
    @Override
    public void updateDWFactTable() throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement1Bis = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        PreparedStatement preparedStatement4 = null;
        PreparedStatement preparedStatement5 = null;
        PreparedStatement preparedStatement6 = null;
        PreparedStatement preparedStatement7 = null;
        ResultSet resultSet1 = null;
        ResultSet resultSet1Bis = null;
        ResultSet resultSet2 = null;
        ResultSet resultSet3 = null;
        ResultSet resultSet4 = null;
        ResultSet resultSet5 = null;

        try {
            
        	// Retrieves connection from the factory
        	connexion = daoFactory.getConnection();
            
        	// Prepared statement the query in charge of loading the new logs regrouped according to distinct 
        	// combinations of the dimensions using the hierarchy 1 of the time dimension 
        	preparedStatement1 = connexion.prepareStatement( SELECT_NEW_LOGS_H1 );
            resultSet1 = preparedStatement1.executeQuery();
            
            // Prepared statement the query in charge of loading the new logs regrouped and counted according to distinct 
        	// combinations of the dimensions using the hierarchy 2 of the time dimension 
            preparedStatement1Bis = connexion.prepareStatement( SELECT_NEW_LOGS_H2 );
            resultSet1Bis = preparedStatement1Bis.executeQuery();

            // Update of the fact table using hierarchy 1 of the time dimension
            // We loop on every line and retrieve the different userIDs, timeIDs and activityIDs from the dimensions
            // that correspond to the attributes of the line read
            while ( resultSet1.next() ) {

            	// List of all the userIDs of the user dimension that correspond to new activity logs
                ArrayList<Integer> userIDs = new ArrayList<Integer>();
                // List of all the timeIDs of the time dimension that correspond to new activity logs
                ArrayList<Integer> timeIDs = new ArrayList<Integer>();
                // List of all the activityIDs of the activity dimension that correspond to new activity logs
                ArrayList<Integer> activityIDs = new ArrayList<Integer>();

                // Loop on all the different possible combinations with the attributes of the user of the line 
                // read and the "all" possibilities
                for ( int sex : new int[] { -1, resultSet1.getInt( "sex" ) } ) {
                    for ( String groupName : new String[] { "All", resultSet1.getString( "groupName" ) } ) {
                        preparedStatement2 = initialisationRequetePreparee( connexion, SELECT_USERDIM_ID, false, sex,
                                groupName );
                        resultSet2 = preparedStatement2.executeQuery();
                        userIDs.addAll( mapSingleColumnIntegerQuery( resultSet2, "userId" ) );
                    }
                }

                // Same as with user in the previous loop but with all the combinations of the hierarchy 1 
                for ( int year : new int[] { -1, resultSet1.getInt( "year" ) } ) {

                    for ( String monthName : new String[] { "All", resultSet1.getString( "monthName" ) } ) {

                        for ( int day : new int[] { -1, resultSet1.getInt( "day" ) } ) {

                            for ( int hour : new int[] { -1, resultSet1.getInt( "hour" ) } ) {

                                preparedStatement3 = initialisationRequetePreparee( connexion, SELECT_TIMEDIM_ID_H1,
                                        false,
                                        hour, day, monthName, year );
                                resultSet3 = preparedStatement3.executeQuery();
                                timeIDs.addAll( mapSingleColumnIntegerQuery( resultSet3, "timeId" ) );
                            }

                        }

                    }
                }

                // Same as before with action dim
                for ( int isAction : new int[] { -1, resultSet1.getInt( "isAction" ) } ) {
                    preparedStatement5 = initialisationRequetePreparee( connexion, SELECT_ACTIVITYDIM_ID, false,
                            isAction );
                    resultSet5 = preparedStatement5.executeQuery();
                    activityIDs.addAll( mapSingleColumnIntegerQuery( resultSet5, "activityId" ) );
                }

                //Once we have retrieved all the different instances of each dimension that reflects the data
                // retrieved from the transaction table, we combinate them in every possible way and insert the new
                // line with the count. The query will add the new count if this combination already exists in
                // the database.
                for ( int userId : userIDs ) {
                    for ( int timeId : timeIDs ) {
                        for ( int activityId : activityIDs ) {
                            preparedStatement6 = initialisationRequetePreparee( connexion, INSERT_DWFACT, false,
                                    userId, activityId, timeId, resultSet1.getInt( "count" ),
                                    resultSet1.getInt( "count" ) );
                            int statut = preparedStatement6.executeUpdate();

                        }
                    }
                }
            }

            // Update of the fact table using hierarchy 2 of the time dimension
            // We loop on every line and retrieve the different userIDs, timeIDs and activityIDs from the dimensions
            // that correspond to the attributes of the line read
            while ( resultSet1Bis.next() ) {

                ArrayList<Integer> userIDs = new ArrayList<Integer>();
                ArrayList<Integer> timeIDs = new ArrayList<Integer>();
                ArrayList<Integer> activityIDs = new ArrayList<Integer>();

                // Loop on all the different possible combinations with the attributes of the user of the line 
                // read and the "all" possibilities
                for ( int sex : new int[] { -1, resultSet1Bis.getInt( "sex" ) } ) {
                    for ( String groupName : new String[] { "All", resultSet1Bis.getString( "groupName" ) } ) {
                        preparedStatement2 = initialisationRequetePreparee( connexion, SELECT_USERDIM_ID, false, sex,
                                groupName );
                        resultSet2 = preparedStatement2.executeQuery();
                        userIDs.addAll( mapSingleColumnIntegerQuery( resultSet2, "userId" ) );
                    }
                }

                // Same as with user in the previous loop but with all the combinations of the hierarchy 2
                for ( int year : new int[] { -1, resultSet1Bis.getInt( "year" ) } ) {

                    for ( String dayName : new String[] { "All", resultSet1Bis.getString( "dayName" ) } ) {

                        for ( int week : new int[] { -1, resultSet1Bis.getInt( "week" ) } ) {
                            preparedStatement4 = initialisationRequetePreparee( connexion, SELECT_TIMEDIM_ID_H2, false,
                                    dayName, week, year );
                            resultSet4 = preparedStatement4.executeQuery();
                            timeIDs.addAll( mapSingleColumnIntegerQuery( resultSet4, "timeId" ) );
                        }
                    }
                }

                // Same as before with action dim
                for ( int isAction : new int[] { -1, resultSet1Bis.getInt( "isAction" ) } ) {
                    preparedStatement5 = initialisationRequetePreparee( connexion, SELECT_ACTIVITYDIM_ID, false,
                            isAction );
                    resultSet5 = preparedStatement5.executeQuery();
                    activityIDs.addAll( mapSingleColumnIntegerQuery( resultSet5, "activityId" ) );
                }

                //Once we have retrieved all the different instances of each dimension that reflects the data
                // retrieved from the transaction table, we combinate them in every possible way and insert the new
                // line with the count. The query will add the new count if this combination already exists in
                // the database.
                for ( int userId : userIDs ) {
                    for ( int timeId : timeIDs ) {
                        for ( int activityId : activityIDs ) {
                            preparedStatement6 = initialisationRequetePreparee( connexion, INSERT_DWFACT, false,
                                    userId, activityId, timeId, resultSet1Bis.getInt( "count" ),
                                    resultSet1Bis.getInt( "count" ) );
                            int statut = preparedStatement6.executeUpdate();

                        }
                    }
                }
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1, connexion );
            fermeturesSilencieuses( preparedStatement2, connexion );
            fermeturesSilencieuses( preparedStatement3, connexion );
            fermeturesSilencieuses( preparedStatement4, connexion );
            fermeturesSilencieuses( preparedStatement5, connexion );
            fermeturesSilencieuses( preparedStatement6, connexion );
            fermeturesSilencieuses( preparedStatement7, connexion );
        }

    }

    /*
     * Method in charge of updating the group dimension
     */
    @SuppressWarnings( "resource" )
    public void updateUserGroupDim() throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet1 = null;
        PreparedStatement preparedStatement2 = null;
        ArrayList<String> newGroups = new ArrayList<String>();
        PreparedStatement preparedStatement3 = null;
        ResultSet generatedKey = null;

        try {
            // Retrives the connection from the factory
        	connexion = daoFactory.getConnection();
            
        	// Prepared statement of the query in charge of retrieving groups that arent reflected in the group dimension
        	// of the data warehouse
        	preparedStatement1 = initialisationRequetePreparee( connexion, SELECT_NEW_GROUPS, false );
            resultSet1 = preparedStatement1.executeQuery();
            
            // Here we map all the retrived groupnales in an arrayList
            newGroups = mapSingleColumnStringQuery( resultSet1, "groupName" );

            // Here we loop on every new groupName
            for ( String groupName : newGroups ) {

                // For every groupname we create a new type of group in the group dimension table
            	preparedStatement2 = initialisationRequetePreparee( connexion, INSERT_GROUP_GROUPDIM, true, groupName );
                preparedStatement2.executeUpdate();

                int groupId = 1;
                // generatedKey stores the groupID of the new group
                generatedKey = preparedStatement2.getGeneratedKeys();

                if ( generatedKey.next() ) {
                    groupId = generatedKey.getInt( 1 );
                }

                // The user dimension only has one attribute sex and there are usually users of both sex in a group
                // So here we loop on every possibility of the sex attribute. -1 being "all"
                for ( int sex = -1; sex < 2; sex++ ) {
                    preparedStatement3 = initialisationRequetePreparee( connexion, INSERT_USER_USERDIM, true, sex,
                            groupId );
                    int statut = preparedStatement3.executeUpdate();
                    if ( statut == 0 ) {
                        throw new DAOException( "Failed to create user in userDim. No rows added." );
                    }
                }

            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1, connexion );
            fermeturesSilencieuses( preparedStatement2, connexion );
            fermeturesSilencieuses( preparedStatement3, connexion );
        }

    }

    /*
     * Method in charge of updating the time dimension
     */
    @SuppressWarnings( "resource" )
    public void updateTimeDim() throws DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet1 = null;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet2 = null;
        ArrayList<Integer> dimTableyears = new ArrayList<Integer>();
        ArrayList<Integer> newActivityLogYears = new ArrayList<Integer>();
        PreparedStatement preparedStatement3 = null;

        try {
            // Retrieves a connection from the factory
        	connexion = daoFactory.getConnection();
           
        	// Prepared statement of the query in charge of selecting all the years from the time dimension
        	preparedStatement1 = initialisationRequetePreparee( connexion, SELECT_DISTINCT_YEAR, false );
            resultSet1 = preparedStatement1.executeQuery();
            
            // Method used to extract the new years from the resultSet into a List
            dimTableyears = mapSingleColumnIntegerQuery( resultSet1, "year" );
            
            // If there isn't any year in the time dimension ( time dimension empty ) then we add the "all" value
            if ( dimTableyears.isEmpty() ) {
                dimTableyears.add( -1 );
            }
            
            // Statement in charge of retrieving all years from the transaction table datetimes
            preparedStatement2 = initialisationRequetePreparee( connexion, SELECT_NEW_YEARS, false );
            resultSet2 = preparedStatement2.executeQuery();
            
            // Method used to extract the years from the resultSet into a List
            newActivityLogYears = mapSingleColumnIntegerQuery( resultSet2, "year" );

            
            // Loops to create all new possible combinations for the new years
            // The two inner loops are for both hierarchies
            for ( Integer newYear : newActivityLogYears ) {
                if ( !dimTableyears.contains( newYear ) ) {

                    for ( String monthName : months ) {

                        for ( int day = -1; day < 32; day++ ) {

                            for ( int hour = -1; hour < 25; hour++ ) {
                            	// Prepared statement of the query in charge of creating a new time in the time dimension
                                preparedStatement3 = initialisationRequetePreparee( connexion, INSERT_TIME_TIMEDIM,
                                        true, newYear, monthName, null, null, day, hour );
                                int statut = preparedStatement3.executeUpdate();
                                if ( statut == 0 ) {
                                    throw new DAOException( "Failed to create time int timeDim. No row added" );
                                }
                            }
                        }
                    }

                    for ( int week = -1; week < 53; week++ ) {

                        for ( String dayName : days ) {
                        	// Prepared statement of the query in charge of creating a new time in the time dimension
                            preparedStatement3 = initialisationRequetePreparee( connexion, INSERT_TIME_TIMEDIM, true,
                                    newYear, null, week, dayName, null, null );
                            int statut = preparedStatement3.executeUpdate();
                            if ( statut == 0 ) {
                                throw new DAOException( "Failed to create time int timeDim. No row added" );
                            }

                        }
                    }

                }
            }

            
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1, connexion );
            fermeturesSilencieuses( preparedStatement2, connexion );
            fermeturesSilencieuses( preparedStatement3, connexion );
        }

    }

    // Method that takes a ResultSet and map the String values from one column whose name is passed as parameter to an ArrayList
    public ArrayList<String> mapSingleColumnStringQuery( ResultSet resultSet, String colName ) throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        while ( resultSet.next() ) {
            result.add( resultSet.getString( colName ) );
        }
        return result;
    }

    // Method that takes a ResultSet and map the Integer values from one column whose name is passed as parameter to an ArrayList
    public ArrayList<Integer> mapSingleColumnIntegerQuery( ResultSet resultSet, String colName ) throws SQLException {
        ArrayList<Integer> result = new ArrayList<>();
        while ( resultSet.next() ) {
            result.add( resultSet.getInt( colName ) );
        }
        return result;
    }

    // Method that takes a ResultSet and map the DateTime values from one column whose name is passed as parameter to an ArrayList
    public ArrayList<DateTime> mapSingleColumnDateTimeQuery( ResultSet resultSet, String colName ) throws SQLException {
        ArrayList<DateTime> result = new ArrayList<DateTime>();
        while ( resultSet.next() ) {
            result.add( new DateTime( resultSet.getTimestamp( colName ) ) );
        }
        return result;
    }


}
