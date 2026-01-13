package models.dailyschedule;

import exceptions.DataLoadException;
import models.dao.factory.FactoryDAO;
import models.training.Training;
import models.training.TrainingDAO;
import models.user.PersonalTrainer;
import models.user.UserDAO;
import utils.DBConnection;
import utils.ResourceLoader;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;

public class DailyScheduleDAODB extends DailyScheduleDAO {
    private final Properties queries;

    public DailyScheduleDAODB() throws DataLoadException {
        try {
            this.queries = ResourceLoader.loadProperties("/queries/schedule_queries.properties");
        } catch (Exception e) {
            throw new DataLoadException("Impossibile caricare il file schedule_queries.properties", e);
        }
    }

    @Override
    public void loadSchedulesByTraining(Training training) {
        String sql = queries.getProperty("SELECT_TRAINING_SCHEDULES");
        if(sql == null) return;

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, training.getPersonalTrainer().getUsername());
            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    String ds_training = resultSet.getString("ds_training");
                    UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
                    PersonalTrainer pt = (PersonalTrainer) userDAO.getUserByUsername(ds_training);

                    TrainingDAO trainingDAO = FactoryDAO.getInstance().createTrainingDAO();
                    Training t = trainingDAO.getTrainingByPT(pt);

                    LocalDate selected_date = resultSet.getDate("selected_date").toLocalDate();

                    String ts = resultSet.getString("time_slots");
                    StringBuilder timeSlots = new StringBuilder(ts);

                    DailySchedule ds = DailySchedule.fromPersistence(t, selected_date, timeSlots);

                    training.addSchedule(ds);
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero degli orari disponibili per " + training.getName() + ": ", e);
        }
    }

    @Override
    public DailySchedule loadSingleScheduleByTraining(Training training, LocalDate date) {
        String sql = queries.getProperty("SELECT_SINGLE_TRAINING_SCHEDULE");
        if(sql == null)
            throw new DataLoadException("Query SELECT_SINGLE_TRAINING_SCHEDULE non trovata");
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, training.getPersonalTrainer().getUsername());
            statement.setDate(2, Date.valueOf(date));

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {

                    /*LocalTime mStart = resultSet.getObject("morning_start", LocalTime.class);
                    LocalTime mEnd = resultSet.getObject("morning_end", LocalTime.class);
                    LocalTime aStart = resultSet.getObject("afternoon_start", LocalTime.class);
                    LocalTime aEnd = resultSet.getObject("afternoon_end", LocalTime.class);*/

                    String ts = resultSet.getString("time_slots");
                    StringBuilder time_slots = new StringBuilder(ts);

                    return DailySchedule.fromPersistence(training, date, time_slots);
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero degli orari per la data: " + date, e);
        }
        return DailySchedule.createNew(training, date);
    }

//    @Override
//    public DailySchedule loadSchedule(Training training, LocalDate date) {
//        String sql = queries.getProperty("SELECT_SCHEDULE");
//        if (sql == null)
//            throw new DataLoadException("Query SELECT_SCHEDULE non trovata");
//
//        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
//            String ptUsername = training.getPersonalTrainer().getUsername();
//
//            statement.setString(1, ptUsername);
//            statement.setDate(2, Date.valueOf(date));
//
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    String slotBits = resultSet.getString("time_slots");
//
//                    DailySchedule schedule = new DailySchedule(date);
//                    schedule.setSlotBits(slotBits);
//                    return schedule;
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataLoadException("Errore nel caricamento orari per la data " + date, e);
//        }
//        return null;
//    }

    @Override
    public void updateDailySchedule(Training training, DailySchedule schedule) {
        String sql = queries.getProperty("INSERT_UPDATE_SCHEDULE");
        if (sql == null) throw new DataLoadException("Query INSERT_UPDATE_SCHEDULE non trovata");

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            String ptUsername = training.getPersonalTrainer().getUsername();

            statement.setString(1, training.getPersonalTrainer().getUsername());
            statement.setDate(2, Date.valueOf(schedule.getDate()));
            statement.setString(3, schedule.getTimeSlots().toString());
            statement.setString(4, schedule.getTimeSlots().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel salvataggio orari per la data " + schedule.getDate(), e);
        }
    }

}