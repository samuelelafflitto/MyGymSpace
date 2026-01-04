package models.dailyschedule;

import models.training.Training;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DailyScheduleDAOMem extends DailyScheduleDAO {
    private static DailyScheduleDAOMem instance;
    private final Map<String, Map<LocalDate, DailySchedule>> schedules;

    protected DailyScheduleDAOMem() {
        this.schedules = new HashMap<>();
        initializeDemoData();
    }

    public static synchronized DailyScheduleDAOMem getInstance() {
        if(instance == null) {
            instance = new DailyScheduleDAOMem();
        }
        return instance;
    }

    public void initializeDemoData() {
        String demoPT = "trainer1";
        LocalDate today = LocalDate.now();

        DailySchedule dailySchedule = new DailySchedule(today);
        dailySchedule.setSlotOccupied("09:00");

        Map<LocalDate, DailySchedule> ptSchedules = new HashMap<>();
        ptSchedules.put(today, dailySchedule);
        schedules.put(demoPT, ptSchedules);

        System.out.println("[MEM] Schedule demo caricata per '" + demoPT + "' in data " + today);
    }

    @Override
    public DailySchedule loadSchedule(Training training, LocalDate date) {
        String ptUsername = training.getPersonalTrainer().getUsername();

        if(schedules.containsKey(ptUsername)) {
            return schedules.get(ptUsername).get(date);
        }
        return null;
    }

    @Override
    public void saveSchedule(Training training, DailySchedule schedule) {
        String ptUsername = training.getPersonalTrainer().getUsername();

        schedules.putIfAbsent(ptUsername, new HashMap<>());
        schedules.get(ptUsername).put(schedule.getDate(), schedule);
        System.out.println("[MEM] Orari salvati per '" + ptUsername + "' in data " + schedule.getDate() + " - Bits: " + schedule.getTimeSlotBits());
    }
}