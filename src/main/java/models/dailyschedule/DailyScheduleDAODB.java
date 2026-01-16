package models.dailyschedule;

import exceptions.DataLoadException;
import models.training.Training;
import utils.DBConnection;
import utils.ResourceLoader;

import java.sql.*;
import java.time.LocalDate;
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
                    LocalDate selectedDate = resultSet.getDate("selected_date").toLocalDate();
                    String ts = resultSet.getString("time_slots");

                    StringBuilder timeslots = new StringBuilder(ts);
                    DailySchedule ds = DailySchedule.fromPersistence(training, selectedDate, timeslots);

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

                    String ts = resultSet.getString("time_slots");
                    StringBuilder timeSlots = new StringBuilder(ts);

                    return DailySchedule.fromPersistence(training, date, timeSlots);
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero degli orari per la data: " + date, e);
        }
        return DailySchedule.createNew(training, date);
    }

    @Override
    public void updateDailySchedule(Training training, DailySchedule schedule) {
        String sql = queries.getProperty("INSERT_UPDATE_SCHEDULE");
        if (sql == null) throw new DataLoadException("Query INSERT_UPDATE_SCHEDULE non trovata");

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

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