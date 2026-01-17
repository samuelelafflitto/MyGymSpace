package models.training;

import exceptions.DataLoadException;
import models.user.PersonalTrainer;
import utils.DBConnection;
import utils.ResourceLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TrainingDAODB extends TrainingDAO {
    private static final String ATHLETE_TYPE = "ATH";
    private static final String PT_TYPE = "PT";

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
                String ptUsername = rs.getString("training_pt");
                PersonalTrainer pt = ptCache.get(ptUsername);

                if(pt == null) {
                    pt = new PersonalTrainer(ptUsername, rs.getString("first_name"), rs.getString("last_name"), PT_TYPE);
                    ptCache.put(ptUsername, pt);
                }

                Training t = mapTrainingFromResultSet(rs, pt);
                list.add(t);
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero degli allenamenti ", e);
        }
        return list;
    }

    @Override
    public Training getTrainingByPT(PersonalTrainer pt) {
        String sql = getQueryOrThrow("SELECT_TRAINING_BY_PT");

        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pt.getUsername());

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapTrainingFromResultSet(resultSet, pt);
                }
            }
        } catch (SQLException e) {
            throw new DataLoadException("Errore nel recupero degli allenamenti ", e);
        }
        return null;
    }

    @Override
    public void initializeDemoData(PersonalTrainer pt1, PersonalTrainer pt2) {
        // Usato solo in modalità demo
    }



    // HELPER
    private String getQueryOrThrow(String query) {
        String sql = queries.getProperty(query);
        if(sql == null)
            throw new DataLoadException("Query " + query + " non trovata");
        return sql;
    }

    private Training mapTrainingFromResultSet(ResultSet rs, PersonalTrainer pt) throws SQLException {
        Training t = new Training();
        t.setPersonalTrainer(pt);
        t.setName(rs.getString("title"));
        t.setDescription(rs.getString("description"));
        t.setBasePrice(rs.getBigDecimal("base_price"));
        return t;
    }
}
