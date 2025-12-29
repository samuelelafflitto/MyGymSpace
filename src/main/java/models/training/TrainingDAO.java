package models.training;

import java.util.List;

public abstract class TrainingDAO {
    public abstract List<Training> getAllTrainings();
    public abstract Training getTrainingByID(String id);
    public abstract Training getTrainingByUser(String usr);
    public abstract void addTraining(Training training);

}
