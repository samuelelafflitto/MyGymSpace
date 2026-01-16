package models.training;

import models.user.PersonalTrainer;

import java.util.List;

public abstract class TrainingDAO {
    public abstract List<Training> getAvailableTrainings();
    public abstract Training getTrainingByPT(PersonalTrainer pt);
    public abstract void initializeDemoData(PersonalTrainer pt1, PersonalTrainer pt2);
}
