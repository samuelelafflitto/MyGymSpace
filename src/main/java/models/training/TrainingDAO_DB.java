package models.training;

import exceptions.DataLoadException;
import models.dailyschedule.DailySchedule;
import models.user.PersonalTrainer;
import utils.DBConnection;
import utils.ResourceLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class TrainingDAO_DB extends TrainingDAO {
    private final Properties queries;

    public TrainingDAO_DB() {
        try {
            this.queries = ResourceLoader.loadProperties("/queries/training_queries.properties");
        } catch (Exception e) {
            throw new DataLoadException("Impossibile caricare il file training_queries.properties", e);
        }
    }

    @Override
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
    }
}
