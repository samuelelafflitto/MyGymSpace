package models.booking;

import exceptions.DataLoadException;
import models.training.Training;
import models.user.Athlete;
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
            statement.setString(1, booking.getId());
            statement.setString(2, booking.getAthlete());
            statement.setString(3, booking.getTraining());
            statement.setDouble(4, booking.getCost());
            statement.setDate(5, Date.valueOf(booking.getDate()));
            statement.setTime(6, Time.valueOf(booking.getStartTime()));
            statement.setString(7, booking.getDescription());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel salvataggio della prenotazione " + booking.getId(), e);
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

        booking.setId(resultSet.getString("id"));
        booking.setAthlete(resultSet.getString("athlete"));
        booking.setTrainingName(resultSet.getString("training"));
        booking.setCost(resultSet.getDouble("cost"));

        Date sqlDate = resultSet.getDate("date");
        if(sqlDate != null)
            booking.setDate(sqlDate.toLocalDate());

        Time sqlTime = resultSet.getTime("time");
        if(sqlTime != null)
            booking.setStartTime(sqlTime.toLocalTime());

        booking.setDescription(resultSet.getString("description"));
        return booking;
    }
}










