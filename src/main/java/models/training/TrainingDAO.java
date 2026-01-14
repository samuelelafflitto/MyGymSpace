package models.training;

import models.dailyschedule.DailySchedule;
import models.user.PersonalTrainer;

import java.util.List;

public abstract class TrainingDAO {
    public abstract List<Training> getAvailableTrainings();
    public abstract Training getTrainingByPT(PersonalTrainer pt);
    public abstract void initializeDemoData(PersonalTrainer pt1, PersonalTrainer pt2);


    //public abstract void updateDailySchedule(PersonalTrainer pt, DailySchedule ds);

    /*public abstract void insertTraining(Training training);
    public abstract Training getTraining(String name);
    public abstract List<Training> getAvailableTrainings();
    public abstract Training getTrainingByPT(PersonalTrainer pt);*/
}
