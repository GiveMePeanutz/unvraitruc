package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public interface DataWarehouseDao {

    void updateDW() throws DAOException;

    public ArrayList<DateTime> mapSingleColumnDateTimeQuery( ResultSet resultSet, String colName ) throws SQLException;

    public ArrayList<Integer> mapSingleColumnIntegerQuery( ResultSet resultSet, String colName ) throws SQLException;

    public ArrayList<String> mapSingleColumnStringQuery( ResultSet resultSet, String colName ) throws SQLException;

    public List<String> getDays();

    public List<String> getMonths();

    public List<String> listYear() throws DAOException;

    public List<String> listGroup() throws DAOException;

}
