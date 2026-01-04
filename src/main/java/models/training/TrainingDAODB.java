package models.training;

import exceptions.DataLoadException;
import models.dailyschedule.DailySchedule;
import models.user.PersonalTrainer;
import utils.DBConnection;
import utils.ResourceLoader;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class TrainingDAODB extends TrainingDAO {
    private final Properties queries;

    public TrainingDAODB() {
        try {
            this.queries = ResourceLoader.loadProperties("/queries/training_queries.properties");
        } catch (Exception e) {
            throw new DataLoadException("Impossibile caricare il file training_queries.properties", e);
        }
    }

    @Override
    public List<Training> getAvailableTrainings() {
        String sql = queries.getProperty("SELECT_ALL_TRAININGS");
        List<Training> list = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Training t = mapResultSetCreatingPT(resultSet);
                loadSchedulesForTraining(t, t.getPersonalTrainer().getUsername());
                list.add(t);
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero dei dati del Database", e);
        }
        return list;
    }

    @Override
    public Training getTrainingByPT(PersonalTrainer pt) {
        String sql = queries.getProperty("SELECT_TRAINING_BY_PT");

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pt.getUsername());

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    Training training = mapResultSetUsingExistingPT(resultSet, pt);
                    loadSchedulesForTraining(training, pt.getUsername());
                    return training;
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero dei dati del Database", e);
        }
        return null;
    }

    @Override
    public void updateDailySchedule(PersonalTrainer pt, DailySchedule ds) {
        String sql = queries.getProperty("INSERT_UPDATE_SCHEDULE");

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pt.getUsername());
            statement.setDate(2, Date.valueOf(ds.getDate()));
            statement.setString(3, ds.getTimeSlotBits());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel salvataggio dei dati nel Database", e);
        }
    }



    private void loadSchedulesForTraining(Training training, String ptUsername) {
        String sql = queries.getProperty("SELECT_SCHEDULES_BY_PT");
        if(sql == null) return;

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ptUsername);
            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    LocalDate date = resultSet.getDate("date").toLocalDate();
                    String bits = resultSet.getString("slots_bits");

                    DailySchedule ds = new DailySchedule(date);
                    ds.setSlotBits(bits);

                    training.addDailySchedule(ds);
                }
            }
        } catch (SQLException e) {
            System.err.println("[WARNING] Impossibile caricare gli orari per " + ptUsername + ": " + e.getMessage());
        }
    }

    private Training mapResultSetCreatingPT(ResultSet resultSet) throws SQLException {
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        double price = resultSet.getDouble("price");

        String ptUsername = resultSet.getString("username");
        String ptFirstName = resultSet.getString("first_name");
        String ptLastName = resultSet.getString("last_name");

        PersonalTrainer personalTrainer = new PersonalTrainer(ptFirstName, ptLastName, ptUsername, null, "PersonalTrainer");
        return new Training(title, description, personalTrainer, price);
    }

    private Training mapResultSetUsingExistingPT(ResultSet resultSet, PersonalTrainer pt) throws SQLException {
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        double price = resultSet.getDouble("price");
        return new Training(title, description, pt, price);
    }


    /*@Override
    public Training getTraining(String name) {
        String sql = queries.getProperty("SELECT_TRAINING_BY_NAME");
        if (sql == null) throw new DataLoadException("Query SELECT_TRAINING_BY_NAME non trovata");

        Training training = null;
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    training = resultSetToTraining(resultSet);
                }
            }
        }catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero dei dati dal Database", e);
        }
        return training;
    }



    private Training resultSetToTraining(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        double basePrice = resultSet.getDouble("base_price");
        PersonalTrainer personalTrainer = resultSet.getObject("personal_trainer", PersonalTrainer.class);
        Map<LocalDate, DailySchedule> schedules = resultSet.getObject("schedules", Map.class);

        Training training = new Training();


    }


    @Override
    public List<Training> getAvailableTrainings() {
        String sql = queries.getProperty("SELECT_ALL_TRAININGS");
        if (sql == null) throw new DataLoadException("Query SELECT_ALL_TRAININGS non trovata");

        List<Training> trainings = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                trainings.add(mapResultSetCreatingPT(resultSet));
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero dei dati da Database", e);
        }
        return trainings;
    }

    @Override
    public Training getTrainingByPT(PersonalTrainer personalTrainer) {
        String sql = queries.getProperty("SELECT_TRAINING_BY_PT");
        if (sql == null) throw new DataLoadException("Query SELECT_TRAINING_BY_PT non trovata");

        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, personalTrainer.getUsername());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetUsingExistingPT(resultSet, personalTrainer);
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero dei dati da Database", e);
        }
        return null;
    }



    private Training mapResultSetCreatingPT(ResultSet resultSet) throws SQLException {
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        double price = resultSet.getDouble("price");

        String fName = resultSet.getString("first_name");
        String lName = resultSet.getString("last_name");
        String username = resultSet.getString("username");

        PersonalTrainer personalTrainer = new PersonalTrainer(fName, lName, username, null, "PersonalTrainer");
        return new Training(title, description, personalTrainer, price);

    }


    private Training mapResultSetUsingExistingPT(ResultSet resultSet, PersonalTrainer personalTrainer) throws SQLException {
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        double price = resultSet.getDouble("price");

        return new Training(title, description, personalTrainer, price);
    }*/
}
