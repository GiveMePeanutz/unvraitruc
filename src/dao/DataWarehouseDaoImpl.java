package dao;

import static dao.DAOUtility.fermeturesSilencieuses;
import static dao.DAOUtility.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

public class DataWarehouseDaoImpl implements DataWarehouseDao {

	private DAOFactory          daoFactory;
	
	private static List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December", "All");
	private static List<String> days = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thrusday", "Friday", "Saturday", "Sunday", "All" );

    private static String SELECT_DISTINCT_YEAR			= "SELECT DISTINCT year FROM TimeDim";
    private static String SELECT_NEW_YEARS				= "SELECT DISTINCT date_format(factDate,'%Y') AS year FROM fact_table WHERE factDate>(SELECT MAX(updateTime) FROM updateTime)";
	private static String SELECT_NEW_GROUPS 			= "SELECT DISTINCT groupName FROM user_group WHERE groupName NOT IN ( SELECT DISTINCT groupName FROM GroupDim)";
    
	private static String SELECT_USERDIM_ID				= "SELECT userId FROM userDim ud, groupDim gd, user u, user_group ug WHERE u.username like ? AND u.sex=ud.sex AND ug.username=u.username AND ug.groupName=gd.groupName AND ud.groupId=gd.groupId ";
	private static String SELECT_TIMEDIM_ID				= "SELECT timeId FROM timeDim td WHERE td.hour like ? AND td.day like ? AND td.dayName like ?  AND td.week like ? AND td.monthName like ? AND td.year like ?";
	private static String SELECT_ACTIVITYDIM_ID			= "SELECT activityId FROM activityDim ad WHERE ad.isAction like ?";
	
	private static String SELECT_NEW_LOGS				= "SELECT DISTINCT u.sex, ug.groupName, pageName,  factDate FROM fact_table, user_group ug, user u WHERE fact_table.username=ug.username AND ug.username=u.username AND factDate>(SELECT MAX(updateTime) FROM updateTime)";
	
	private static String COUNT							= "SELECT COUNT(*) as count FROM fact_table WHERE username IN (SELECT u.username FROM user u, user_group ug, userDim ud, groupDim gd WHERE u.sex=ud.sex AND ud.groupID=gd.groupID AND gd.groupName=ug.groupName  AND u.username=ug.username AND ud.userID=?) AND pageName IN (SELECT pageName FROM fact_table ft, activityDim ad WHERE IF(ad.isAction=0, pageName LIKE '/%', NOT pageName LIKE '/%') AND ad.activityID=?) AND factDate IN (SELECT factDate FROM fact_table ft, timeDim td WHERE  date_format(factDate,'%H')=td.hour AND date_format(factDate,'%d')=td.day AND date_format(factDate,'%W')=td.dayName  AND date_format(factDate,'%u')=td.week AND date_format(factDate,'%M')=td.monthName AND date_format(factDate,'%Y')=td.year AND td.timeID=?)";
	
    private static String INSERT_TIME_TIMEDIM			= "INSERT INTO timeDim (year,monthName,week,dayName,day,hour) values (?,?,?,?,?,?)";
    private static String INSERT_GROUP_GROUPDIM			= "INSERT INTO groupDim (groupName) VALUES (?)";
    private static String INSERT_USER_USERDIM			= "INSERT INTO userDim (sex, groupID) VALUES (?,?)";
    
    private static String INSERT_UPDATE_DATETIME		= "INSERT INTO updateTime (updateTime)  VALUES (NOW())";
    private static String INSERT_DWFACT					= "INSERT INTO dwFactTable (userID, activityId, timeId, count) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE count = VALUES(count)";
    
    
	DataWarehouseDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }
	
	


	

	


	@Override
	public void updateDW() throws DAOException {
		
		Connection connexion = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        
        
        try {
            connexion = daoFactory.getConnection();
            // Dimension updates
            updateTimeDim();
            updateUserGroupDim();
            
            // DW Fact table update
            updateDWFactTable();
            
           
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1, connexion );
            fermeturesSilencieuses( preparedStatement2, connexion );
            fermeturesSilencieuses( preparedStatement3, connexion );
        }
		
	}

	
	public void updateDWFactTable () throws DAOException {
		
		Connection connexion = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        PreparedStatement preparedStatement4 = null;
        PreparedStatement preparedStatement5 = null;
        PreparedStatement preparedStatement6 = null;
        PreparedStatement preparedStatement7 = null;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;
        ResultSet resultSet3 = null;
        ResultSet resultSet4 = null;
        ResultSet resultSet5 = null;
        ArrayList<Integer> userIDs = new ArrayList<Integer>();
        ArrayList<Integer> timeIDs = new ArrayList<Integer>();
        ArrayList<Integer> activityIDs = new ArrayList<Integer>();
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement1 = connexion.prepareStatement(SELECT_NEW_LOGS);
            resultSet1 = preparedStatement1.executeQuery();
            while( resultSet1.next() ){
            	
            	preparedStatement2 = initialisationRequetePreparee(connexion, SELECT_USERDIM_ID, false, resultSet1.getString("username"));
                resultSet2 = preparedStatement2.executeQuery();
            	userIDs = mapSingleColumnIntegerQuery(resultSet2, "userId");
            	
            	preparedStatement3 = initialisationRequetePreparee(connexion, SELECT_TIMEDIM_ID, false, new DateTime(resultSet1.getTimestamp("factDate")));
            	resultSet3 = preparedStatement3.executeQuery();
            	timeIDs = mapSingleColumnIntegerQuery(resultSet3, "timeId");
            	
            	boolean isAction=true;
            	if(resultSet1.getString("pageName").startsWith("/")){
            		isAction=false;
            	}
            	preparedStatement4 = initialisationRequetePreparee(connexion, SELECT_ACTIVITYDIM_ID, false, isAction);
            	resultSet4 = preparedStatement4.executeQuery();
            	activityIDs = mapSingleColumnIntegerQuery(resultSet4, "activityId");
            	
            	
            	
            	for(int userId : userIDs){
            		for(int timeId : timeIDs){
            			for(int activityId : activityIDs){
            				int count=0;
            				preparedStatement5 = initialisationRequetePreparee(connexion, COUNT, false, userId, activityId, timeId);
            				resultSet5 = preparedStatement5.executeQuery();
            				if(resultSet5.next()){
            					count=resultSet5.getInt("count");
            				}
            				
            				preparedStatement6 = initialisationRequetePreparee(connexion, INSERT_DWFACT, false, userId,activityId,timeId,count);
            				int statut = preparedStatement6.executeUpdate();
            				
            				
            			}
            		}
            	}
            	
            	
            	preparedStatement7 = connexion.prepareStatement(INSERT_UPDATE_DATETIME);
            	preparedStatement7.executeUpdate();
            	
            	
            }
           
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1, connexion );
            fermeturesSilencieuses( preparedStatement2, connexion );
            fermeturesSilencieuses( preparedStatement3, connexion );
        }	
		
	}
	
	@SuppressWarnings("resource")
	public void updateUserGroupDim() throws DAOException {
		
		Connection connexion = null;
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet1 = null;
        PreparedStatement preparedStatement2 = null;
        ArrayList<String> newGroups = new ArrayList<String>();
        PreparedStatement preparedStatement3 = null;
        
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement1 = initialisationRequetePreparee( connexion, SELECT_NEW_GROUPS , false  );
            resultSet1 = preparedStatement1.executeQuery();
            newGroups = mapSingleColumnStringQuery(resultSet1, "groupName");
            
            for( String groupName : newGroups ){
            	
            	preparedStatement2 = initialisationRequetePreparee(connexion, INSERT_GROUP_GROUPDIM, true, groupName);
            	int generatedKey = preparedStatement2.executeUpdate();
            	
            	for(int sex=-1; sex<2; sex++){
            		preparedStatement3 = initialisationRequetePreparee(connexion, INSERT_USER_USERDIM, true, sex, generatedKey);
            		int statut = preparedStatement3.executeUpdate();
            		if( statut == 0 ){
            			throw new DAOException ("Failed to create user in userDim. No rows added." );
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
	
	@SuppressWarnings("resource")
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
            connexion = daoFactory.getConnection();
            preparedStatement1 = initialisationRequetePreparee( connexion, SELECT_DISTINCT_YEAR , false  );
            resultSet1 = preparedStatement1.executeQuery();
            dimTableyears = mapSingleColumnIntegerQuery(resultSet1, "year");
            if(dimTableyears.isEmpty()){
            	dimTableyears.add(-1);
            }
            preparedStatement2 = initialisationRequetePreparee( connexion, SELECT_NEW_YEARS , false  );
            resultSet2 = preparedStatement2.executeQuery();
            newActivityLogYears = mapSingleColumnIntegerQuery(resultSet2, "year");
            
            for(Integer newYear : newActivityLogYears){
            	if(!dimTableyears.contains(newYear)){
            		
            		for( String monthName : months ){
            			
            			for ( int week=-1; week<53; week++){
            				
            				for ( String dayName : days ){
            					
	            				for ( int day=-1; day<32; day++){
	            					
	            					for ( int hour=-1; hour<25; hour++){
	            						
	            						preparedStatement3 = initialisationRequetePreparee( connexion, INSERT_TIME_TIMEDIM , true, newYear, monthName, week, dayName, day, hour );
	            						int statut = preparedStatement3.executeUpdate();
	            						if ( statut == 0 ) {
	            			                throw new DAOException(
	            			                        "Failed to create time int timeDim. No row added" );
	            			            }
	            					}
	            				}
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
	
	
	
	public ArrayList<String> mapSingleColumnStringQuery(ResultSet resultSet, String colName) throws SQLException{
		ArrayList<String> result = new ArrayList<>();
		while(resultSet.next()){
			result.add(resultSet.getString(colName));
		}
		return result;
	}
	
	public ArrayList<Integer> mapSingleColumnIntegerQuery(ResultSet resultSet, String colName) throws SQLException{
		ArrayList<Integer> result = new ArrayList<>();
		while(resultSet.next()){
			result.add(resultSet.getInt(colName));
		}
		return result;
	}
	
	public ArrayList<DateTime> mapSingleColumnDateTimeQuery(ResultSet resultSet, String colName) throws SQLException{
		ArrayList<DateTime> result = new ArrayList<DateTime>();
		while(resultSet.next()){
			result.add(new DateTime(resultSet.getTimestamp(colName)));
		}
		return result;
	}
}
