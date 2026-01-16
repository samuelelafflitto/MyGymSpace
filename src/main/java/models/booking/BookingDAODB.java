package models.booking;

import exceptions.DataLoadException;
import models.booking.record.BasicBookingDataFromDB;
import models.booking.record.BookingDataWithPT;
import models.booking.record.BookingDataWithTraining;
import models.booking.record.FinalBookingData;
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
import java.util.*;

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
            System.out.println(e.getMessage());
            //throw new DataLoadException("Errore nel salvataggio della prenotazione", e);
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
                            rs.getString("pt_username"),
                            rs.getDate("date").toLocalDate(),
                            rs.getTime("selected_slot").toLocalTime(),
                            rs.getString("description"),
                            rs.getBigDecimal("final_price"));

                    records.add(newR);
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero delle prenotazioni.");
        }
        return records;
    }

    private List<BookingDataWithTraining> enrichWithTraining(List<BasicBookingDataFromDB> baseRecords) {
        String sql = getQueryOrThrow("SELECT_TRAINING_FOR_BOOKING");
        List<BookingDataWithTraining> enrichedRecords = new ArrayList<>();

        Map<String, Training> trainingCache = new HashMap<>();
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for(BasicBookingDataFromDB record : baseRecords) {
                String ptUsername = record.ptUsername();
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
                BookingDataWithTraining newR = new BookingDataWithTraining(record.athlete(),
                        training,
                        record.ptUsername(),
                        record.date(),
                        record.selectedSlot(),
                        record.description(),
                        record.finalPrice());
                enrichedRecords.add(newR);
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel reupero degli allenamenti.");
        }
        return enrichedRecords;
    }

    private List<BookingDataWithPT> enrichWithPT(List<BookingDataWithTraining> records) {
        String sql = getQueryOrThrow("SELECT_PT_FOR_TRAINING");
        List<BookingDataWithPT> enrichedRecords = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for(BookingDataWithTraining record : records) {
                statement.setString(1, record.ptUsername());
                statement.setString(2, "PT");

                try (ResultSet rs = statement.executeQuery()) {
                    if(rs.next()) {
                        PersonalTrainer pt = new PersonalTrainer(rs.getString("username"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                "PT");

                        record.training().setPersonalTrainer(pt);

                        BookingDataWithPT newR = new BookingDataWithPT(record.athlete(),
                                record.training(),
                                record.date(),
                                record.selectedSlot(),
                                record.description(),
                                record.finalPrice());

                        enrichedRecords.add(newR);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel reupero degli allenamenti.");
        }
        return enrichedRecords;
    }

    private List<FinalBookingData> enrichWithDailySchedules(List<BookingDataWithPT> records) {
        String sql = getQueryOrThrow("SELECT_DS_FOR_TRAINING");
        List<FinalBookingData> finalRecords = new ArrayList<>();

        Map<String, List<DailySchedule>> scheduleCache = new HashMap<>();
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for(BookingDataWithPT record : records) {
                String ptUsername = record.training().getPersonalTrainer().getUsername();
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

                            DailySchedule ds = new DailySchedule(record.training(), date, slots);
                            schedules.add(ds);
                        }
                    }
                    scheduleCache.put(ptUsername, schedules);
                }
                record.training().setSchedules(schedules);

                FinalBookingData finalRecord = new FinalBookingData(record.athlete(),
                        record.training(),
                        record.training().getSchedules().get(record.date()),
                        record.selectedSlot(),
                        record.description(),
                        record.finalPrice());

                finalRecords.add(finalRecord);
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel reupero delle DailySchedule.");
        }
        return finalRecords;
    }

    private List<BookingInterface> createFinalBookings(List<FinalBookingData> records) {
        List<BookingInterface> bookings = new ArrayList<>();

        for(FinalBookingData record: records) {
            ConcreteBooking booking = new ConcreteBooking();

            booking.setAthlete(record.athlete());
            booking.setTraining(record.training());
            booking.setDailySchedule(record.dailySchedule());
            booking.setSelectedSlot(record.selectedSlot());
            booking.setDescription(record.description());
            booking.setFinalPrice(record.finalPrice());

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

    private ConcreteBooking mapBookingsFromResultSet(ResultSet resultSet, Athlete user) throws SQLException {
        ConcreteBooking booking = new ConcreteBooking();

        booking.setAthlete(user);

        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
        PersonalTrainer pt = (PersonalTrainer) userDAO.getUserByUsername(resultSet.getString("pt_username"));

        TrainingDAO trainingDAO = FactoryDAO.getInstance().createTrainingDAO();
        Training t = trainingDAO.getTrainingByPT(pt);

        DailyScheduleDAO dailyScheduleDAO = FactoryDAO.getInstance().createDailyScheduleDAO();
        DailySchedule ds = dailyScheduleDAO.loadSingleScheduleByTraining(t, resultSet.getDate("date").toLocalDate());

        booking.setTraining(t);
        booking.setDailySchedule(ds);
        booking.setSelectedSlot(resultSet.getTime("selected_date").toLocalTime());
        booking.setDescription(resultSet.getString("description"));
        booking.setFinalPrice(resultSet.getBigDecimal("final_price"));

        return booking;
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
        t.setDescription(resultSet.getString("description"));
        t.setBasePrice(resultSet.getBigDecimal("base_price"));
        return t;
    }
}










