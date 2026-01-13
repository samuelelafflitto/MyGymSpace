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


//    public void initializeDemoData() {
//        String demoPT = "trainer1";
//        LocalDate today = LocalDate.now();
//
//        DailySchedule dailySchedule = new DailySchedule(today);
//        dailySchedule.setSlotOccupied("09:00");
//
//        Map<LocalDate, DailySchedule> ptSchedules = new HashMap<>();
//        ptSchedules.put(today, dailySchedule);
//        schedules.put(demoPT, ptSchedules);
//
//        System.out.println("[MEM] Schedule demo caricata per '" + demoPT + "' in data " + today);
//    }
//
//    @Override
//    public DailySchedule loadSchedule(Training training, LocalDate date) {
//        String ptUsername = training.getPersonalTrainer().getUsername();
//
//        if(schedules.containsKey(ptUsername)) {
//            return schedules.get(ptUsername).get(date);
//        }
//        return null;
//    }
//
//    @Override
//    public void saveSchedule(Training training, DailySchedule schedule) {
//        String ptUsername = training.getPersonalTrainer().getUsername();
//
//        schedules.putIfAbsent(ptUsername, new HashMap<>());
//        schedules.get(ptUsername).put(schedule.getDate(), schedule);
//        System.out.println("[MEM] Orari salvati per '" + ptUsername + "' in data " + schedule.getDate() + " - Bits: " + schedule.getTimeSlotBits());
//    }
}