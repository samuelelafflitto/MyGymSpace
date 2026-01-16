package models.dailyschedule;

import models.training.Training;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DailyScheduleDAOMem extends DailyScheduleDAO {

    private final Map<String, DailySchedule> schedules;
    private static DailyScheduleDAOMem instance;

    protected DailyScheduleDAOMem() {
        this.schedules = new HashMap<>();
    }

    public static synchronized DailyScheduleDAOMem getInstance() {
        if(instance == null) {
            instance = new DailyScheduleDAOMem();
        }
        return instance;
    }

    @Override
    public void loadSchedulesByTraining(Training training) {
        String ptUsername = training.getPersonalTrainer().getUsername();

        for(Map.Entry<String, DailySchedule> entry : schedules.entrySet()) {
            if(entry.getKey().startsWith(ptUsername + "_")) {
                training.addSchedule(entry.getValue());
            }
        }
    }

    @Override
    public DailySchedule loadSingleScheduleByTraining(Training training, LocalDate date) {
        String key = generateKey(training, date);

        if(schedules.containsKey(key)) {
            return schedules.get(key);
        }

        return DailySchedule.createNew(training, date);
    }

    @Override
    public void updateDailySchedule(Training training, DailySchedule dailySchedule) {
        String key = generateKey(training, dailySchedule.getDate());

        schedules.put(key, dailySchedule);

        training.addSchedule(dailySchedule);
    }


    private String generateKey(Training training, LocalDate date) {
        return training.getPersonalTrainer().getUsername() + "_" + date.toString();
    }
}