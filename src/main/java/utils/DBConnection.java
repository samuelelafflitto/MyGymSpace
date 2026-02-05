package utils;

import exceptions.DataLoadException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        try {
            Properties prop = ResourceLoader.loadProperties("/config/dbconfig.properties");

            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.user");
            PASSWORD = prop.getProperty("db.password");

            if (URL == null || USER == null || PASSWORD == null) {
                throw new DataLoadException("Missing DB credentials in config.properties file");
            }
        } catch (Exception e) {
            throw new DataLoadException("Error uploading the configuration from config.properties file ", e);
        }
    }

    protected DBConnection(){
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DataLoadException("Unable to connect to the Database", e);
        }
    }

    public static DBConnection getInstance(){
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {

        if(connection == null || connection.isClosed()) {
            try {
                this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                throw new SQLException("Error reconnecting to the Database",e);
            }
        }
        return connection;
    }
}
