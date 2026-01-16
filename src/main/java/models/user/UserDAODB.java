package models.user;

import exceptions.DataLoadException;
import exceptions.UserSearchFailedException;
import models.booking.BookingDAO;
import models.booking.BookingInterface;
import models.dao.factory.FactoryDAO;
import models.training.Training;
import models.training.TrainingDAO;
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

        User user = null;
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                user = mapUserFromResultSet(resultSet);
//                if (resultSet.next()) {
//                    String firstName = resultSet.getString("first_name");
//                    String lastName = resultSet.getString("last_name");
//                    String username = resultSet.getString("username");
//                    String password = resultSet.getString("password");
//                    String type = resultSet.getString("type");
//
//                    if (PT_TYPE.equals(type)) {
//                        user = new PersonalTrainer(username, password, firstName, lastName, type);
//                    } else if (ATHLETE_TYPE.equals(type)) {
//                        user = new Athlete(username, password, firstName, lastName, type);
//                    }
//                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            //throw new DataLoadException("Errore nel recupero dei dati dal Database", e);
        }
        if (user != null) {
            return populateUser(user);
        }
        return null;
    }

    @Override
    public User getUserByUsername(String usr) {
        String sql = getQueryOrThrow("SELECT_USER_BY_USERNAME");

        User user = null;
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usr);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
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
            System.out.println(e.getMessage());
            //throw new DataLoadException("Errore nel recupero dei dati dal Database", e);
        }
        if (user != null) {
            return populateUser(user);
        }
        return null;
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
    public void initializeDemoData() {
        // Usato solo in modalità demo
    }

    private User mapUserFromResultSet(ResultSet resultSet) throws SQLException {
        if(resultSet.next()) {
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String type = resultSet.getString("type");

            if (type.equals(PT_TYPE)) {
                return new PersonalTrainer(username, password, firstName, lastName, type);
            } else if (type.equals(ATHLETE_TYPE)) {
                return new Athlete(username, password, firstName, lastName, type);
            } else throw new UserSearchFailedException();
        } else throw new UserSearchFailedException();
    }



    private User populateUser(User user) {
        // Se user è ATHLETE
        if (user.getType().equals(ATHLETE_TYPE)) {
            BookingDAO bookingDAO = FactoryDAO.getInstance().createBookingDAO();
            List<BookingInterface> bookings = new ArrayList<>();

            try {
                bookings = bookingDAO.getBookingByUser((Athlete) user);
            } catch (DataLoadException e) {
                System.out.println(e.getMessage());
            }
            ( (Athlete) user).setBookings(bookings);
        } else if (user.getType().equals(PT_TYPE)) {
            TrainingDAO trainingDAO = FactoryDAO.getInstance().createTrainingDAO();
            Training training = null;

            try {
                training = trainingDAO.getTrainingByPT((PersonalTrainer)user);
                training.setPersonalTrainer((PersonalTrainer)user);
            } catch (DataLoadException e) {
                System.out.println(e.getMessage());
            }
            ( (PersonalTrainer) user).setTraining(training);
        }
        return user;
    }



    // HELPER
    private String getQueryOrThrow(String query) {
        String sql = queries.getProperty(query);
        if(sql == null)
            throw new DataLoadException("Query " + query + " non trovata");
        return sql;
    }
}
