package models.dailyschedule;

import exceptions.DataLoadException;
import models.training.Training;
import utils.DBConnection;
import utils.ResourceLoader;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    public List<DailySchedule> getSchedulesByTraining(Training training) {
        String sql = getQueryOrThrow("SELECT_DS_FOR_TRAINING");
        List<DailySchedule> schedules = new ArrayList<>();
        String ptUsername = training.getPersonalTrainer().getUsername();

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ptUsername);

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    schedules.add(mapDailyScheduleFromResultSet(rs, training));
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero delle dailyschedule ", e);
        }
        return schedules;
    }

    @Override
    public DailySchedule loadSingleScheduleByTraining(Training training, LocalDate date) {
        String sql = getQueryOrThrow("SELECT_SINGLE_TRAINING_SCHEDULE");

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
        String sql = getQueryOrThrow("INSERT_UPDATE_SCHEDULE");

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


    // HELPER
    private String getQueryOrThrow(String query) {
        String sql = queries.getProperty(query);
        if(sql == null)
            throw new DataLoadException("Query " + query + " non trovata");
        return sql;
    }

    private DailySchedule mapDailyScheduleFromResultSet(ResultSet rs, Training training) throws SQLException {
        LocalDate date = rs.getDate("selected_date").toLocalDate();
        StringBuilder slots = new StringBuilder(rs.getString("time_slots"));
        return new DailySchedule(training, date, slots);
    }

}