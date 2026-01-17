package models.booking;

import exceptions.DataLoadException;
import models.booking.record.*;
import models.dailyschedule.DailySchedule;
import models.dailyschedule.DailyScheduleDAO;
import models.dao.factory.FactoryDAO;
import models.training.Training;
import models.training.TrainingDAO;
import models.user.*;
import utils.DBConnection;
import utils.ResourceLoader;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class BookingDAODB extends BookingDAO {
    private static final String ATHLETE_USERNAME = "athlete_username";
    private static final String PT_USERNAME = "pt_username";
    private static final String DESCRIPTION = "description";
    public static final String FINAL_PRICE = "final_price";
    public static final String SELECTED_SLOT = "selected_slot";
    private final Properties queries;
    private static final String ATHLETE_TYPE = "ATH";
//    private static final String PT_TYPE = "PT";

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
            throw new DataLoadException("Errore nel recupero delle prenotazioni ", e);
        }
        return records;
    }

    @Override
    public List<BookingInterface> getBookingByTraining(Training training) {
        String sql = queries.getProperty("SELECT_BOOKINGS_BY_TRAINING");
        if(sql == null)
            throw new DataLoadException("Query SELECT_BOOKING_BY_TRAINING non trovata");

        List<BookingInterface> bookings = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, training.getName());

            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    bookings.add(mapBookingFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero delle prenotazioni per l'allenamento " + training.getName(), e);
        }
        return bookings;
    }

    private ConcreteBooking mapBookingFromResultSet(ResultSet resultSet) throws SQLException {
        ConcreteBooking booking = new ConcreteBooking();

        // Ricavo Athlete e Personal Trainer dagli username nel DB
        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
        Athlete ath = (Athlete)userDAO.getUserByUsername(resultSet.getString(ATHLETE_USERNAME));
        PersonalTrainer pt = (PersonalTrainer)userDAO.getUserByUsername(resultSet.getString(PT_USERNAME));

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
        booking.setSelectedSlot(resultSet.getTime(SELECTED_SLOT).toLocalTime());
        booking.setDescription(resultSet.getString(DESCRIPTION));
        booking.setFinalPrice(resultSet.getBigDecimal(FINAL_PRICE));

        return booking;
    }





    // HELPER
    private String getQueryOrThrow(String query) {
        String sql = queries.getProperty(query);
        if(sql == null)
            throw new DataLoadException("Query " + query + " non trovata");
        return sql;
    }
}










