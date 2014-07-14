package dao;

import static dao.DAOUtility.fermeturesSilencieuses;
import static dao.DAOUtility.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Date;

public class DateDaoImpl implements DateDao {

    private DAOFactory          daoFactory;

    private static final String SQL_INSERT = "INSERT INTO Date (minute, hour, day, dayName, week, monthName, quarter, year) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

    DateDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    public Date create() throws DAOException
    {
        Connection connexion = null;
        PreparedStatement preparedStatement1 = null;
        ResultSet valeursAutoGenerees = null;
        Date date = new Date();

        try {
            connexion = daoFactory.getConnection();
            preparedStatement1 = initialisationRequetePreparee( connexion,
                    SQL_INSERT, true, date.getMinute(), date.getHour(), date.getDay(),
                    date.getDayName(), date.getWeek(), date.getMonthName(), date.getQuarter(),
                    date.getYear() );
            int statut1 = preparedStatement1.executeUpdate();
            if ( statut1 == 0 ) {
                throw new DAOException(
                        "Failed to create date. No row added" );
            }
            valeursAutoGenerees = preparedStatement1.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                date.setDateID( valeursAutoGenerees.getInt( 1 ) );
            } else {
                throw new DAOException( "Failed to create date. No auto increment ID generation " );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1,
                    connexion );

        }

        return date;

    }

}
