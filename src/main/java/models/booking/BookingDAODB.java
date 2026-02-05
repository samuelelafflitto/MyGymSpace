package models.booking;

import exceptions.DataLoadException;
import exceptions.FailedBookingCancellationException;
import models.booking.record.*;
import models.dailyschedule.DailyScheduleDAO;
import models.dao.factory.FactoryDAO;
import models.training.Training;
import models.training.TrainingDAO;
import models.user.*;
import utils.DBConnection;
import utils.ResourceLoader;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class BookingDAODB extends BookingDAO {
    private static final String ATHLETE_USERNAME = "athlete_username";
    private static final String PT_USERNAME = "pt_username";
    private static final String DESCRIPTION = "description";
    public static final String FINAL_PRICE = "final_price";
    public static final String SELECTED_SLOT = "selected_slot";
    private final Properties queries;
    private static final String ATHLETE_TYPE = "ATH";

    public BookingDAODB() {
        try {
            this.queries = ResourceLoader.loadProperties("/queries/booking_queries.properties");
        } catch (Exception e) {
            throw new DataLoadException("Unable to upload booking_queries.properties file ", e);
        }
    }

    @Override
    public void saveBooking(BookingInterface booking) {
        String sql = getQueryOrThrow("INSERT_BOOKING");

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, booking.getAthlete().getUsername());
            statement.setString(2, booking.getTraining().getPersonalTrainer().getUsername());
            statement.setDate(3, Date.valueOf(booking.getDailySchedule().getDate()));
            statement.setTime(4, Time.valueOf(booking.getSelectedSlot()));
            statement.setString(5, booking.getDescription());
            statement.setBigDecimal(6, booking.getFinalPrice());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataLoadException("Error saving booking ", e);
        }
    }

    @Override
    public void deleteBooking(String athleteUsername, String ptUsername, LocalDate date, LocalTime time) {
        String sql = getQueryOrThrow("DELETE_BOOKING");

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, athleteUsername);
            statement.setString(2, ptUsername);
            statement.setDate(3, Date.valueOf(date));
            statement.setTime(4, Time.valueOf(time));

            int affectedRows = statement.executeUpdate();
            if(affectedRows > 0) {
                TrainingDAO trainingDAO = FactoryDAO.getInstance().createTrainingDAO();
                UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
                PersonalTrainer pt = (PersonalTrainer) userDAO.getUserByUsername(ptUsername);
                Training training = trainingDAO.getTrainingByPT(pt);

                DailyScheduleDAO dailyScheduleDAO = FactoryDAO.getInstance().createDailyScheduleDAO();
                dailyScheduleDAO.resetSlotInSchedule(training, date, time);

            }else if(affectedRows == 0) {
                throw new FailedBookingCancellationException();
            }
        } catch (SQLException e) {
            throw new DataLoadException("Error deleting booking ", e);
        }
    }

    @Override
    public List<BasicBookingDataFromPersistence> fetchBasicBookingData(User user) {
        String queryKey = (user.getType().equals(ATHLETE_TYPE)) ? "SELECT_BOOKINGS_BY_ATHLETE" : "SELECT_BOOKINGS_BY_PT";
        String sql = getQueryOrThrow(queryKey);
        List<BasicBookingDataFromPersistence> records = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    BasicBookingDataFromPersistence newR = new BasicBookingDataFromPersistence(rs.getString(ATHLETE_USERNAME),
                            rs.getString(PT_USERNAME),
                            rs.getDate("date").toLocalDate(),
                            rs.getTime(SELECTED_SLOT).toLocalTime(),
                            rs.getString(DESCRIPTION),
                            rs.getBigDecimal(FINAL_PRICE));

                    records.add(newR);
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Error retrieving booking ", e);
        }
        return records;
    }

    // HELPER
    private String getQueryOrThrow(String query) {
        String sql = queries.getProperty(query);
        if(sql == null)
            throw new DataLoadException(query + " query not found");
        return sql;
    }
}










