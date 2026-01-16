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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class BookingDAODB extends BookingDAO {
    private static final String PT_USERNAME = "pt_username";
    private static final String DESCRIPTION = "description";
    public static final String FINAL_PRICE = "final_price";
    public static final String SELECTED_SLOT = "selected_slot";
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
        List<BasicBookingDataFromDB> dbRecordsA = fetchBasicBookingData(user);

        List<BookingDataWithTraining> dbRecordsB = enrichWithTraining(dbRecordsA);
        List<BookingDataWithPT> dbRecordsC = enrichWithPT(dbRecordsB);
        List<FinalBookingData> dbFinalRecords = enrichWithDailySchedules(dbRecordsC);

        return createFinalBookings(dbFinalRecords);
    }

    private List<BasicBookingDataFromDB> fetchBasicBookingData(Athlete athlete) {
        String sql = getQueryOrThrow("SELECT_BOOKINGS_BY_USER");
        List<BasicBookingDataFromDB> records = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, athlete.getUsername());

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    BasicBookingDataFromDB newR = new BasicBookingDataFromDB(athlete,
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

    private List<BookingDataWithTraining> enrichWithTraining(List<BasicBookingDataFromDB> baseRecords) {
        String sql = getQueryOrThrow("SELECT_TRAINING_FOR_BOOKING");
        List<BookingDataWithTraining> enrichedRecords = new ArrayList<>();

        Map<String, Training> trainingCache = new HashMap<>();
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for(BasicBookingDataFromDB element : baseRecords) {
                String ptUsername = element.ptUsername();
                Training training;

                if(trainingCache.containsKey(ptUsername)) {
                    training = trainingCache.get(ptUsername);
                } else {
                    statement.setString(1, ptUsername);

                    try (ResultSet rs = statement.executeQuery()) {
                        if(rs.next()) {
                            training = mapTrainingFromResultSet(rs);
                            trainingCache.put(ptUsername, training);

                        } else {
                            continue;
                        }
                    }
                }
                BookingDataWithTraining newR = new BookingDataWithTraining(element.athlete(),
                        training,
                        element.ptUsername(),
                        element.date(),
                        element.selectedSlot(),
                        element.description(),
                        element.finalPrice());
                enrichedRecords.add(newR);
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero degli allenamenti ", e);
        }
        return enrichedRecords;
    }

    private List<BookingDataWithPT> enrichWithPT(List<BookingDataWithTraining> records) {
        String sql = getQueryOrThrow("SELECT_PT_FOR_TRAINING");
        List<BookingDataWithPT> enrichedRecords = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for(BookingDataWithTraining element : records) {
                statement.setString(1, element.ptUsername());

                try (ResultSet rs = statement.executeQuery()) {
                    if(rs.next()) {
                        PersonalTrainer pt = new PersonalTrainer(rs.getString("username"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                "PT");

                        element.training().setPersonalTrainer(pt);

                        BookingDataWithPT newR = new BookingDataWithPT(element.athlete(),
                                element.training(),
                                element.date(),
                                element.selectedSlot(),
                                element.description(),
                                element.finalPrice());

                        enrichedRecords.add(newR);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero degli allenamenti ", e);
        }
        return enrichedRecords;
    }

    private List<FinalBookingData> enrichWithDailySchedules(List<BookingDataWithPT> records) {
        String sql = getQueryOrThrow("SELECT_DS_FOR_TRAINING");
        List<FinalBookingData> finalRecords = new ArrayList<>();

        Map<String, List<DailySchedule>> scheduleCache = new HashMap<>();
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for(BookingDataWithPT element : records) {
                String ptUsername = element.training().getPersonalTrainer().getUsername();
                List<DailySchedule> schedules;

                if(scheduleCache.containsKey(ptUsername)) {
                    schedules = scheduleCache.get(ptUsername);
                } else {
                    schedules = new ArrayList<>();
                    statement.setString(1, ptUsername);
                    try(ResultSet rs = statement.executeQuery()) {
                        while(rs.next()) {
                            LocalDate date = rs.getDate("selected_date").toLocalDate();
                            StringBuilder slots = new StringBuilder(rs.getString("time_slots"));

                            DailySchedule ds = new DailySchedule(element.training(), date, slots);
                            schedules.add(ds);
                        }
                    }
                    scheduleCache.put(ptUsername, schedules);
                }
                element.training().setSchedules(schedules);

                FinalBookingData finalRecord = new FinalBookingData(element.athlete(),
                        element.training(),
                        element.training().getSchedules().get(element.date()),
                        element.selectedSlot(),
                        element.description(),
                        element.finalPrice());

                finalRecords.add(finalRecord);
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero delle DailySchedule ", e);
        }
        return finalRecords;
    }

    private List<BookingInterface> createFinalBookings(List<FinalBookingData> records) {
        List<BookingInterface> bookings = new ArrayList<>();

        for(FinalBookingData element: records) {
            ConcreteBooking booking = new ConcreteBooking();

            booking.setAthlete(element.athlete());
            booking.setTraining(element.training());
            booking.setDailySchedule(element.dailySchedule());
            booking.setSelectedSlot(element.selectedSlot());
            booking.setDescription(element.description());
            booking.setFinalPrice(element.finalPrice());

            bookings.add(booking);
        }
        return bookings;
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
                    bookings.add(mapResultSetToBooking(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero delle prenotazioni per l'allenamento " + training.getName(), e);
        }
        return bookings;
    }

    @Override
    public int getTotalSessions(String username, boolean isAthlete) throws SQLException {
        String sql;
        if(isAthlete) {
            sql = getQueryOrThrow("COUNT_TOTAL_SESSIONS_ATH");
        } else {
            sql = getQueryOrThrow("COUNT_TOTAL_SESSIONS_PT");
        }

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt("total") : 0;
            }
        }
    }

    @Override
    public int getFutureSessions(String username, boolean isAthlete, LocalDate dateNow, LocalTime timeNow) throws SQLException {
        String sql;
        if(isAthlete) {
            sql = getQueryOrThrow("COUNT_FUTURE_SESSIONS_ATH");
        } else {
            sql = getQueryOrThrow("COUNT_FUTURE_SESSIONS_PT");
        }

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setDate(2, Date.valueOf(dateNow));
            statement.setDate(3, Date.valueOf(dateNow));
            statement.setTime(4, Time.valueOf(timeNow));

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt("total") : 0;
            }
        }
    }


    public NextSessionRecord getNextSession(String username, boolean isAthlete, LocalDate dateNow, LocalTime timeNow) throws SQLException {
        String sql;
        if(isAthlete) {
            sql = getQueryOrThrow("WHEN_NEXT_SESSION_ATH");
        } else {
            sql = getQueryOrThrow("WHEN_NEXT_SESSION_PT");
        }

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setDate(2, Date.valueOf(dateNow));
            statement.setDate(3, Date.valueOf(dateNow));
            statement.setTime(4, Time.valueOf(timeNow));

            try (ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    LocalDate day = rs.getDate("date").toLocalDate();
                    LocalTime hour = rs.getTime(SELECTED_SLOT).toLocalTime();
                    return new NextSessionRecord(day, hour);
                }
            }
        }
        return null;
    }








    private ConcreteBooking mapResultSetToBooking(ResultSet resultSet) throws SQLException {
        ConcreteBooking booking = new ConcreteBooking();

        // Ricavo Athlete e Personal Trainer dagli username nel DB
        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
        Athlete ath = (Athlete)userDAO.getUserByUsername(resultSet.getString("athlete_username"));
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

    private Training mapTrainingFromResultSet(ResultSet resultSet) throws SQLException {
        Training t = new Training();
        t.setName(resultSet.getString("title"));
        t.setDescription(resultSet.getString(DESCRIPTION));
        t.setBasePrice(resultSet.getBigDecimal("base_price"));
        return t;
    }
}










