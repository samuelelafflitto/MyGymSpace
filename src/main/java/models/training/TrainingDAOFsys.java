package models.training;

import models.user.PersonalTrainer;

import java.util.List;

public class TrainingDAOFsys extends TrainingDAO {
    @Override
    public List<Training> getAvailableTrainings() {
        // Not implemented
        return List.of();
    }

    @Override
    public Training getTrainingByPT(PersonalTrainer personalTrainer) {
        // Not implemented
        return null;
    }

    @Override
    public void initializeDemoData(PersonalTrainer pt1, PersonalTrainer pt2) {
        // Used only in Demo mode
    }

    @Override
    public void updateTrainingDetails(Training t) {
        // Not implemented
    }
}