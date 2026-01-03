package models.dailyschedule;

import models.training.Training;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public abstract class DailyScheduleDAO {
    public abstract DailySchedule loadSchedule(Training training, LocalDate date);
    public abstract void saveSchedule(Training training, DailySchedule schedule);

    /*public abstract List<LocalTime> getDailySchedule(Training t, LocalDate d);
    public abstract void updateDailySchedule (DailySchedule ds, LocalTime hour);*/
}
