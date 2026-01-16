package models.training;

import exceptions.DataLoadException;
import models.user.PersonalTrainer;
import utils.DBConnection;
import utils.ResourceLoader;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TrainingDAODB extends TrainingDAO {
    private final Properties queries;

    public TrainingDAODB() throws DataLoadException {
        try {
            this.queries = ResourceLoader.loadProperties("/queries/training_queries.properties");
        } catch (Exception e) {
            throw new DataLoadException("Impossibile caricare il file training_queries.properties", e);
        }
    }

    @Override
    public List<Training> getAvailableTrainings() {
        String sql = getQueryOrThrow("SELECT_ALL_TRAININGS");
        List<Training> list = new ArrayList<>();

        Map<String, PersonalTrainer> ptCache = new HashMap<>();

        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            while(rs.next()) {
                Training t = mapTrainingFromResultSet(rs, ptCache);
                list.add(t);
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero degli allenamenti ", e);
        }
//        enrichTrainingWithSchedules(list);

        return list;
    }

    @Override
    public Training getTrainingByPT(PersonalTrainer pt) {
        String sql = getQueryOrThrow("SELECT_TRAINING_BY_PT");

        Training training = null;
        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pt.getUsername());

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    training = mapResultSetUsingExistingPT(resultSet, pt);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            //throw new DataLoadException("Errore nel recupero dei dati del Database", e);
        }

        if(training != null) {
            return training;
        }
        return null;
    }

    @Override
    public void initializeDemoData(PersonalTrainer pt1, PersonalTrainer pt2) {
        // Usato solo in modalità demo
    }

    private Training mapTrainingFromResultSet(ResultSet rs, Map<String, PersonalTrainer> ptCache) throws SQLException {
        String ptUsername = rs.getString("training_pt");
        PersonalTrainer pt;

        if(ptCache.containsKey(ptUsername)) {
            pt = ptCache.get(ptUsername);
        } else {
            pt = new PersonalTrainer(ptUsername, rs.getString("first_name"), rs.getString("last_name"), "PT");
            ptCache.put(ptUsername, pt);
        }

        Training t = new Training();
        t.setPersonalTrainer(pt);
        t.setName(rs.getString("title"));
        t.setDescription(rs.getString("description"));
        t.setBasePrice(rs.getBigDecimal("base_price"));

        return t;
    }

    private Training mapResultSetUsingExistingPT(ResultSet resultSet, PersonalTrainer pt) throws SQLException {
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        BigDecimal basePrice = resultSet.getBigDecimal("base_price");

        Training training = new Training();
        training.setPersonalTrainer(pt);
        training.setName(title);
        training.setDescription(description);
        training.setBasePrice(basePrice);

        return training;
        /*return new Training(title, description, pt, price);*/
    }

//    private void enrichTrainingWithSchedules(List<Training> list) {
//        if(list == null || list.isEmpty())
//            return;
//
//        String sql = getQueryOrThrow("SELECT_ALL_SCHEDULES");
//        Map<String, Training> trainingMap = new HashMap<>();
//
//        for(Training t : list) {
//            String ptUsername = t.getPersonalTrainer().getUsername();
//            trainingMap.put(ptUsername, t);
//        }
//
//        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
//            while(rs.next()) {
//                String ptUsernameFromDB = rs.getString("ds_training");
//
//                if(trainingMap.containsKey(ptUsernameFromDB)) {
//                    Training matchingTraining = trainingMap.get(ptUsernameFromDB);
//                    LocalDate date = rs.getDate("selected_date").toLocalDate();
//                    StringBuilder slots = new  StringBuilder(rs.getString("time_slots"));
//
//                    DailySchedule newDS = new DailySchedule(matchingTraining, date, slots);
//
//                    List<DailySchedule> currentSchedules = matchingTraining.getSchedules();
//
//                    if(currentSchedules == null) {
//                        currentSchedules = new ArrayList<>();
//                        matchingTraining.setSchedules(currentSchedules);
//                    }
//                    currentSchedules.add(newDS);
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataLoadException("Errore nel caricamento delle schedule", e);
//        }
//    }


    // HELPER
    private String getQueryOrThrow(String query) {
        String sql = queries.getProperty(query);
        if(sql == null)
            throw new DataLoadException("Query " + query + " non trovata");
        return sql;
    }
}
