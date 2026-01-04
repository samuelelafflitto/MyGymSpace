package models.dailyschedule;

import models.training.Training;

import java.time.LocalDate;

public class DailyScheduleDAOFsys extends DailyScheduleDAO {
    @Override
    public DailySchedule loadSchedule(Training training, LocalDate date) {
        // Non implementato
        return null;
    }

    @Override
    public void saveSchedule(Training training, DailySchedule schedule) {
        // Non implementato
    }
}
