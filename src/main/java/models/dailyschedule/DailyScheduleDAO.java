package models.dailyschedule;

import models.training.Training;

import java.time.LocalDate;
import java.util.List;

public abstract class DailyScheduleDAO {
//    public abstract void loadSchedulesByTraining(Training training);
    public abstract List<DailySchedule> getSchedulesByTraining(Training training);
    public abstract DailySchedule loadSingleScheduleByTraining(Training training, LocalDate date);
    public abstract void updateDailySchedule(Training training, DailySchedule dailySchedule);
}
