package models.dailyschedule;

import models.training.Training;

import java.time.LocalDate;
import java.util.List;

public class DailyScheduleDAOFsys extends DailyScheduleDAO {

    @Override
    public List<DailySchedule> getSchedulesByTraining(Training training) {
        // Non implementato
        return List.of();
    }

    @Override
    public DailySchedule loadSingleScheduleByTraining(Training training, LocalDate date) {
        // Non implementato
        return null;
    }

    @Override
    public void updateDailySchedule(Training training, DailySchedule dailySchedule) {
        // Non Implementato
    }
}
