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
}