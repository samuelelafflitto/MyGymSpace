package models.training;

import models.dailyschedule.DailySchedule;
import models.user.PersonalTrainer;

import java.util.List;

public class TrainingDAOFsys extends TrainingDAO {
    @Override
    public List<Training> getAvailableTrainings() {
        // Non implementato
        return List.of();
    }

    @Override
    public Training getTrainingByPT(PersonalTrainer personalTrainer) {
        // Non implementato
        return null;
    }

    @Override
    public void initializeDemoData(PersonalTrainer pt1, PersonalTrainer pt2) {
        // Usato solo in modalità demo
    }
}