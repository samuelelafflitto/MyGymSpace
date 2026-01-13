package models.dailyschedule;

import models.training.Training;

import java.time.LocalDate;

public class DailyScheduleDAOFsys extends DailyScheduleDAO {
    public void loadSchedulesByTraining(Training training) {
        // Non implementato
    }
    public DailySchedule loadSingleScheduleByTraining(Training training, LocalDate date) {
        // Non implementato
        return null;
    }

    @Override
    public void updateDailySchedule(Training training, DailySchedule dailySchedule) {
        // Non Implementato
    }
}
