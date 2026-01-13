package models.booking;

import exceptions.DataLoadException;
import models.dailyschedule.DailySchedule;
import models.dailyschedule.DailyScheduleDAO;
import models.dao.factory.FactoryDAO;
import models.training.Training;
import models.training.TrainingDAO;
import models.user.*;
import utils.DBConnection;
import utils.ResourceLoader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BookingDAODB extends BookingDAO {
    private final Properties queries;

    public BookingDAODB() {
        try {
            this.queries = ResourceLoader.loadProperties("/queries/booking_queries.properties");
        } catch (Exception e) {
            throw new DataLoadException("Impossibile caricare il file booking_queries.properties", e);
        }
    }

    @Override
    public void saveBooking(BookingInterface booking) {
        String sql = queries.getProperty("INSERT_BOOKING");
        if(sql == null)
            throw new DataLoadException("Query INSERT_BOOKING non trovata");

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, booking.getAthlete().getUsername());
            statement.setString(2, booking.getTraining().getPersonalTrainer().getUsername());
            statement.setDate(3, Date.valueOf(booking.getDailySchedule().getDate()));
            statement.setTime(4, Time.valueOf(booking.getSelectedSlot()));
            statement.setString(5, booking.getDescription());
            statement.setBigDecimal(6, booking.getFinalPrice());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel salvataggio della prenotazione", e);
        }
    }

    @Override
    public List<BookingInterface> getBookingByUser(Athlete user) {
        String sql = queries.getProperty("SELECT_BOOKING_BY_USER");
        if(sql == null)
            throw new DataLoadException("Query SELECT_BOOKING_BY_USER non trovata");

        List<BookingInterface> bookings = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());

            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    bookings.add(mapResultSetToBooking(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero delle prenotazioni per l'utente " + user.getUsername(), e);
        }
        return bookings;
    }

    @Override
    public List<BookingInterface> getBookingByTraining(Training training) {
        String sql = queries.getProperty("SELECT_BOOKING_BY_TRAINING");
        if(sql == null)
            throw new DataLoadException("Query SELECT_BOOKING_BY_TRAINING non trovata");

        List<BookingInterface> bookings = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, training.getName());

            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    bookings.add(mapResultSetToBooking(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero delle prenotazioni per l'allenamento " + training.getName(), e);
        }
        return bookings;
    }

    private ConcreteBooking mapResultSetToBooking(ResultSet resultSet) throws SQLException {
        ConcreteBooking booking = new ConcreteBooking();

        // Ricavo Athlete e Personal Trainer dagli username nel DB
        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
        Athlete ath = (Athlete)userDAO.getUserByUsername(resultSet.getString("athlete_username"));
        PersonalTrainer pt = (PersonalTrainer)userDAO.getUserByUsername(resultSet.getString("pt_username"));

        // Ricavo Training dal Personal Trainer
        TrainingDAO trainingDAO = FactoryDAO.getInstance().createTrainingDAO();
        Training t = trainingDAO.getTrainingByPT(pt);

        // Ricavo DailySchedule da Training e la date nel DB
        DailyScheduleDAO dsDAO = FactoryDAO.getInstance().createDailyScheduleDAO();
        DailySchedule ds = dsDAO.loadSingleScheduleByTraining(t, resultSet.getDate("date").toLocalDate());

        // Ricreo la ConcreteBooking vera e propria
        booking.setAthlete(ath);
        booking.setTraining(t);
        booking.setDailySchedule(ds);
        booking.setSelectedSlot(resultSet.getTime("selected_slot").toLocalTime());
        booking.setDescription(resultSet.getString("description"));
        booking.setFinalPrice(resultSet.getBigDecimal("final_price"));

        return booking;
    }
}










