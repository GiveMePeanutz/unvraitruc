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
	
	private static List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December", "");
	private static List<String> days = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thrusday", "Friday", "Saturday", "Sunday", "" );

    private static String SELECT_DISTINCT_YEAR			= "SELECT DISTINCT year FROM TimeDim";
    private static String SELECT_NEW_YEARS				= "SELECT DISTINCT date_format(factDate,'%Y') AS year FROM fact_table WHERE factDate>(SELECT MAX(updateTime) FROM updateTime)";
	private static String SELECT_NEW_GROUPS 			= "SELECT DISTINCT groupName FROM user_group WHERE groupName NOT IN ( SELECT DISTINCT groupName FROM GroupDim)";
    
    private static String INSERT_TIME_TIMEDIM			= "INSERT INTO timeDim (year,monthName,week,dayName,day,hour) values (?,?,?,?,?,?)";
    private static String INSERT_GROUP_GROUPDIM			= "INSERT INTO groupDim (groupName) VALUES (?)";
    private static String INSERT_USER_USERDIM			= "INSERT INTO userDim (sex, groupID) VALUES (?,?)";
    
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
        
        
        try {
            connexion = daoFactory.getConnection();
            
           
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
}
