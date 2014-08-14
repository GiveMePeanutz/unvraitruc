package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DAOFactory {

    private static final String FICHIER_PROPERTIES       = "/dao/dao.properties";
    private static final String PROPERTY_URL             = "url";
    private static final String PROPERTY_DRIVER          = "driver";
    private static final String PROPERTY_NOM_UTILISATEUR = "nomutilisateur";
    private static final String PROPERTY_MOT_DE_PASSE    = "motdepasse";

    /* package */BoneCP         connectionPool           = null;

    /* package */DAOFactory( BoneCP connectionPool ) {
        this.connectionPool = connectionPool;
    }

    /*
     * Method in charge of retrieving the informationof the connection to the 
     * database, loading the driver JDBC and return an instance of the factory
     */
    public static DAOFactory getInstance() throws DAOConfigurationException {
        Properties properties = new Properties();
        String url;
        String driver;
        String nomUtilisateur;
        String motDePasse;
        BoneCP connectionPool = null;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new DAOConfigurationException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            properties.load( fichierProperties );
            url = properties.getProperty( PROPERTY_URL );
            driver = properties.getProperty( PROPERTY_DRIVER );
            nomUtilisateur = properties.getProperty( PROPERTY_NOM_UTILISATEUR );
            motDePasse = properties.getProperty( PROPERTY_MOT_DE_PASSE );
        } catch ( FileNotFoundException e ) {
            throw new DAOConfigurationException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable.", e );
        } catch ( IOException e ) {
            throw new DAOConfigurationException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }

        try {
            Class.forName( driver );
        } catch ( ClassNotFoundException e ) {
            throw new DAOConfigurationException( "Le driver est introuvable dans le classpath.", e );
        }

        try {
            /*
             * Creation of a configuration of a pool of connection via
             * BoneCPConfig object and the different associated setters
             */
            BoneCPConfig config = new BoneCPConfig();
            /* Setting of the URL, name and password */
            config.setJdbcUrl( url );
            config.setUsername( nomUtilisateur );
            config.setPassword( motDePasse );
            /* Setting of the pool size  */
            config.setMinConnectionsPerPartition( 5 );
            config.setMaxConnectionsPerPartition( 30 );
            config.setPartitionCount( 2 );
            /* Creation of the pool from the configuration via the BoneCP object */
            connectionPool = new BoneCP( config );
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOConfigurationException( "Erreur de configuration du pool de connexions.", e );
        }
        /*
         * Saves the created pool in a instance variable via a call
         * to the DAOFactry constructor
         */
        DAOFactory instance = new DAOFactory( connectionPool );
        return instance;
    }

    /* Method in charge of giving a connection to the database */
    /* package */Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    /*
     * Methods used to retrieve the implementation of the various DAOs
     */
    public CourseDao getCourseDao() {
        return new CourseDaoImpl( this );
    }

    public GroupDao getGroupDao() {
        return new GroupDaoImpl( this );
    }

    public PrivDao getPrivDao() {
        return new PrivDaoImpl( this );
    }

    public UserDao getUserDao() {
        return new UserDaoImpl( this );
    }

    public TransactionTableDao getTransactionTableDao() {
        return new TransactionTableDaoImpl( this );
    }

    public KmeansDataDao getKmeansDataDao() {
        return new KmeansDataDaoImpl( this );
    }

    public NaiveBayesDao getNaiveBayesDao() {
        return new NaiveBayesDaoImpl( this );
    }

    public DataWarehouseDao getDataWarehouseDao() {
        return new DataWarehouseDaoImpl( this );
    }

    public ExtractDataWarehouseDao getExtractDataWarehouseDao() {
        return new ExtractDataWarehouseDaoImpl( this );
    }

}