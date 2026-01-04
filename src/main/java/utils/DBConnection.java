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
            Properties prop = ResourceLoader.loadProperties("/config/config.properties");

            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.user");
            PASSWORD = prop.getProperty("db.password");

            if (URL == null || USER == null || PASSWORD == null) {
                throw new DataLoadException("Credenziali DB mancanti nel file config.properties");
            }
        } catch (Exception e) {
            throw new DataLoadException("Errore nel caricamento della configurazione da config.properties", e);
        }
    }

    protected DBConnection() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DataLoadException("Impossibile connettersi al Database", e);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore di riconnessione al Database", e);
        }
        return connection;
    }
}
