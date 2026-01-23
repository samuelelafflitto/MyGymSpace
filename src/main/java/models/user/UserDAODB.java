package models.user;

import exceptions.DataLoadException;
import exceptions.UserSearchFailedException;
import utils.DBConnection;
import utils.ResourceLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDAODB extends UserDAO {
    private final Properties queries;
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";

    private static final String PT_TYPE = "PT";
    private static final String ATHLETE_TYPE = "ATH";

    public UserDAODB() {
        try {
            this.queries = ResourceLoader.loadProperties("/queries/user_queries.properties");
        } catch (Exception e) {
            throw new DataLoadException("Impossibile caricare il file user_queries.properties", e);
        }
    }

    @Override
    public User getUser(String username, String password) {
        String sql = getQueryOrThrow("SELECT_USER");

        User user;
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                user = mapUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            throw new DataLoadException("Errore nel recupero dei dati dal Database", e);
        }
        return user;
    }

    @Override
    public User getUserByUsername(String usr) {
        String sql = getQueryOrThrow("SELECT_USER_BY_USERNAME");

        User user = null;
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usr);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString(FIRST_NAME);
                    String lastName = resultSet.getString(LAST_NAME);
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String type = resultSet.getString("type");

                    if (PT_TYPE.equals(type)) {
                        user = new PersonalTrainer(username, password, firstName, lastName, type);
                    } else if (ATHLETE_TYPE.equals(type)) {
                        user = new Athlete(username, password, firstName, lastName, type);
                    }
                }
            }
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            throw new DataLoadException("Errore nel recupero dei dati dal Database", e);
        }
        return user;
    }

    @Override
    public void addUser(String username, User user) {
        String sql = getQueryOrThrow("INSERT_USER");

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getType());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataLoadException("Errore durante l'inserimento dell'utente: " + username, e);
        }
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        String sql = getQueryOrThrow("UPDATE_PASSWORD");

        try(Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, newPassword);
            statement.setString(2, username);

            int modifiedRows = statement.executeUpdate();
            if (modifiedRows == 0) {
                throw new UserSearchFailedException();
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore durante l'aggiornamento della password", e);
        }
    }

    @Override
    public void updateName(String username, String newFirstName, String newLastName) {
        String sql = getQueryOrThrow("UPDATE_NAME");

        try(Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, newFirstName);
            statement.setString(2, newLastName);
            statement.setString(3, username);

            int modifiedRows = statement.executeUpdate();
            if (modifiedRows == 0) {
                throw new UserSearchFailedException();
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore durante l'aggiornamento di Nome e Cognome", e);
        }
    }

    @Override
    public User fetchUserFromPersistence(String username, String type, Map<String, User> userCache) {
        if(userCache.containsKey(username)) {
            return userCache.get(username);
        }

        String sql = getQueryOrThrow("SELECT_USER_BY_USERNAME_LIGHT");

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    User user;
                    String firstName = rs.getString(FIRST_NAME);
                    String lastName = rs.getString(LAST_NAME);

                    if(type.equals(ATHLETE_TYPE)) {
                        user = new Athlete(username, firstName, lastName, ATHLETE_TYPE);
                    } else {
                        user = new PersonalTrainer(username, firstName, lastName, PT_TYPE);
                    }

                    userCache.put(username, user);
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero dell'utente " + username, e);
        }
        return null;
    }

    private User mapUserFromResultSet(ResultSet resultSet) throws SQLException {
        if(resultSet.next()) {
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String firstName = resultSet.getString(FIRST_NAME);
            String lastName = resultSet.getString(LAST_NAME);
            String type = resultSet.getString("type");

            if (type.equals(PT_TYPE)) {
                return new PersonalTrainer(username, password, firstName, lastName, type);
            } else if (type.equals(ATHLETE_TYPE)) {
                return new Athlete(username, password, firstName, lastName, type);
            } else throw new UserSearchFailedException();
        } else throw new UserSearchFailedException();
    }



    // HELPER
    private String getQueryOrThrow(String query) {
        String sql = queries.getProperty(query);
        if(sql == null)
            throw new DataLoadException("Query " + query + " non trovata");
        return sql;
    }
}
