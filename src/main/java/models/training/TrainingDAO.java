package models.training;

import models.user.PersonalTrainer;

import java.util.List;

public abstract class TrainingDAO {
    public abstract void insertTraining(Training training);
    public abstract Training getTraining(String username);
    public abstract List<Training> getAvailableTrainings();
    public abstract Training getTrainingByPT(PersonalTrainer pt);
}
