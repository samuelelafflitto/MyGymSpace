package utils;

import exceptions.DataLoadException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    private static final String url;
    private static final String user;
    private static final String password;

    static {
        try {
            Properties prop = ResourceLoader.loadProperties("/config/config.properties");

            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");

            if (url == null || user == null || password == null) {
                throw new DataLoadException("Credenziali DB mancanti nel file config.properties");
            }
        } catch (Exception e) {
            throw new DataLoadException("Errore nel caricamento della configurazione da config.properties", e);
        }
    }

    protected DBConnection() {
        try {
            this.connection = DriverManager.getConnection(url, user, password);
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
                this.connection = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore di riconnessione al Database", e);
        }
        return connection;
    }
}
