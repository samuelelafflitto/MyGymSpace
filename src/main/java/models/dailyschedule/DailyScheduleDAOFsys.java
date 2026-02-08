package models.dailyschedule;

import models.training.Training;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DailyScheduleDAOFsys extends DailyScheduleDAO {

    @Override
    public List<DailySchedule> getSchedulesByTraining(Training training) {
        // Not implemented
        return List.of();
    }

    @Override
    public DailySchedule loadSingleScheduleByTraining(Training training, LocalDate date) {
        // Not implemented
        return null;
    }

    @Override
    public void updateDailySchedule(Training training, DailySchedule dailySchedule) {
        // Not implemented
    }

    @Override
    public void resetSlotInSchedule(Training training, LocalDate date, LocalTime time) {
        // Not implemented
    }

    @Override
    public void deleteDemoDailySchedule(Training t) {
        // Used only in Demo mode
    }
}
