package models.user;

import exceptions.DataLoadException;
import models.booking.BookingDAO;
import models.booking.BookingInterface;
import models.dao.factory.FactoryDAO;
import utils.DBConnection;
import utils.ResourceLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDAODB extends UserDAO {
    private final Properties queries;

    private static final String PT_TYPE = "PT";
    private static final String ATHLETE_TYPE = "ATHLETE";

    public UserDAODB() {
        try {
            this.queries = ResourceLoader.loadProperties("/queries/user_queries.properties");
        } catch (Exception e) {
            throw new DataLoadException("Impossibile caricare il file user_queries.properties", e);
        }
    }

    @Override
    public User getUser(String username, String password) {
        String sql = queries.getProperty("SELECT_USER");
        if(sql == null)
            throw new DataLoadException("Query SELECT_USER non trovata");

        User user = null;
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = mapUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero dei dati dal Database", e);
        }
        return populateUser(user);
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = queries.getProperty("SELECT_USER_BY_USERNAME");
        if (sql == null)
            throw new DataLoadException("Query SELECT_USER_BY_USERNAME non trovata");

        User user = null;
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = mapUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero dei dati dal Database", e);
        }
        return (user != null) ? populateUser(user) : null;
    }

    public void addUser(String username, User user) {
        String sql = queries.getProperty("INSERT_USER");
        if (sql == null)
            throw new DataLoadException("Query INSERT_USER non trovata");

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getType());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataLoadException("Errore durante l'inserimento dell'utente: " + username, e);
        }
    }

    /*@Override
        public void addUser(String firstName, String lastName, String username, String password) {
            String sql = queries.getProperty("INSERT_USER");
            if (sql == null)
                throw new DataLoadException("Query INSERT_USER non trovata");

            try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, username);
                statement.setString(4, password);
                statement.setString(5, ATHLETE_TYPE);

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DataLoadException("Errore durante l'inserimento dell'utente: " + username, e);
            }
        }*/

    private User mapUserFromResultSet(ResultSet resultSet) throws SQLException {
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String type = resultSet.getString("type");

        if (PT_TYPE.equals(type)) {
            return new PersonalTrainer(firstName, lastName, username, password, type);
        } else {
            return new Athlete(firstName, lastName, username, password, type);
        }
    }

    private User populateUser(User user) {
        if (user.getType().equals(ATHLETE_TYPE)) {
            BookingDAO bookingDAO = FactoryDAO.getInstance().createBookingDAO();
            List<BookingInterface> bookings = new ArrayList<>();

            try {
                bookings = bookingDAO.getBookingByUser((Athlete) user);
            } catch (Exception e) {
                throw new DataLoadException(e.getMessage());
            }
            ( (Athlete) user).setBookings(bookings);
        }
        return user;
    }
}
