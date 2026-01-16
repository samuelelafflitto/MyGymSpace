package models.training;

import exceptions.DataLoadException;
import models.dailyschedule.DailySchedule;
import models.dailyschedule.DailyScheduleDAO;
import models.dao.factory.FactoryDAO;
import models.user.PersonalTrainer;
import utils.DBConnection;
import utils.ResourceLoader;

import javax.print.attribute.HashDocAttributeSet;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

    // DA IMPLEMENTARE CON NUOVA SESSIONE CHE 'TIENE TRACCIA'
//    @Override
//    public Training getTrainingByPT(PersonalTrainer pt) {
//        String sql = queries.getProperty("SELECT_TRAINING_BY_PT");
//
//        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, pt.getUsername());
//
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if(resultSet.next()) {
//                    Training training = mapResultSetUsingExistingPT(resultSet, pt);
//                    loadSchedulesForTraining(training);
//                    return training;
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataLoadException("Errore nel recupero dei dati del Database", e);
//        }
//        return null;
//    }

    // DA SPOSTARE IN DAILYSCHEDULE (V fatto)
    /*@Override
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
    }*/


    // DA SPOSTARE IN DAILYSCHEDULE (V fatto)
//    private void loadSchedulesForTraining(Training training) {
//        String sql = queries.getProperty("SELECT_TRAINING_SCHEDULES");
//        if(sql == null) return;
//
//        try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, training.getPersonalTrainer().getUsername());
//            try (ResultSet resultSet = statement.executeQuery()) {
//                while(resultSet.next()) {
//                    String ds_training = resultSet.getString("ds_training");
//                    LocalDate selected_date = resultSet.getDate("selected_date").toLocalDate();
//                    LocalTime morning_start = resultSet.getObject("morning_start", LocalTime.class);
//                    LocalTime morning_end = resultSet.getObject("morning_end", LocalTime.class);
//                    LocalTime afternoon_start = resultSet.getObject("afternoon_start", LocalTime.class);
//                    LocalTime afternoon_end = resultSet.getObject("afternoon_end", LocalTime.class);
//                    StringBuilder time_slots = resultSet.getObject("time_slots", StringBuilder.class);
//
//                    DailySchedule ds = DailySchedule.fromPersistence(training, selected_date, morning_start, morning_end, afternoon_start, afternoon_end, time_slots);
//
//                    training.addSchedule(ds);
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataLoadException("Errore nel recupero degli orari disponibili per " + training.getName() + ": ", e);
//        }
//    }

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

//    private Training mapResultSetCreatingPT(ResultSet resultSet) throws SQLException {
//        String title = resultSet.getString("title");
//        String description = resultSet.getString("description");
//        BigDecimal basePrice = resultSet.getBigDecimal("base_price");
//
//        String ptUsername = resultSet.getString("training_pt");
//        String ptPassword = resultSet.getString("password");
//        String ptFirstName = resultSet.getString("first_name");
//        String ptLastName = resultSet.getString("last_name");
//
//        PersonalTrainer personalTrainer = new PersonalTrainer(ptUsername, ptPassword, ptFirstName, ptLastName, "PT");
//        Training training = new Training();
//        training.setPersonalTrainer(personalTrainer);
//        training.setName(title);
//        training.setDescription(description);
//        training.setBasePrice(basePrice);
//
//        return training;
//        //return new Training(personalTrainer, title, description, basePrice);
//    }

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
