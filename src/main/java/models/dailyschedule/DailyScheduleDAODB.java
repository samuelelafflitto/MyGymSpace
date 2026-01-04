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

    public DailyScheduleDAODB() {
        try {
            this.queries = ResourceLoader.loadProperties("/queries/schedule_queries.properties");
        } catch (Exception e) {
            throw new DataLoadException("Impossibile caricare il file schedule_queries.properties", e);
        }
    }

    @Override
    public DailySchedule loadSchedule(Training training, LocalDate date) {
        String sql = queries.getProperty("SELECT_SCHEDULE");
        if (sql == null)
            throw new DataLoadException("Query SELECT_SCHEDULE non trovata");

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            String ptUsername = training.getPersonalTrainer().getUsername();

            statement.setString(1, ptUsername);
            statement.setDate(2, Date.valueOf(date));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String slotBits = resultSet.getString("time_slots");

                    DailySchedule schedule = new DailySchedule(date);
                    schedule.setSlotBits(slotBits);
                    return schedule;
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel caricamento orari per la data " + date, e);
        }
        return null;
    }

    @Override
    public void saveSchedule(Training training, DailySchedule schedule) {
        String sql = queries.getProperty("INSERT_UPDATE_SCHEDULE");
        if (sql == null) throw new DataLoadException("Query INSERT_UPDATE_SCHEDULE non trovata");

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            String ptUsername = training.getPersonalTrainer().getUsername();

            statement.setString(1, ptUsername);
            statement.setDate(2, Date.valueOf(schedule.getDate()));
            statement.setString(3, schedule.getTimeSlotBits());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel salvataggio orari per la data " + schedule.getDate(), e);
        }
    }

}