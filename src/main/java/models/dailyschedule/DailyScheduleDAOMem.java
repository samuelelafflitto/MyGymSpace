package models.dailyschedule;

import models.training.Training;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public List<DailySchedule> getSchedulesByTraining(Training training) {
        List<DailySchedule> result = new ArrayList<>();

        for(DailySchedule ds : schedules.values()) {
            if(ds.getTraining().equals(training)) {
                result.add(ds);
            }
        }
        return result;
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

    @Override
    public void resetSlotInSchedule(Training training, LocalDate date, LocalTime time) {
        DailySchedule schedule = loadSingleScheduleByTraining(training, date);
        int index = time.getHour();

        StringBuilder slots = schedule.getTimeSlots();
        if(index >= 0 && index < 24) {
            slots.setCharAt(index, '0');
        }
    }


    private String generateKey(Training training, LocalDate date) {
        return training.getPersonalTrainer().getUsername() + "_" + date.toString();
    }
}