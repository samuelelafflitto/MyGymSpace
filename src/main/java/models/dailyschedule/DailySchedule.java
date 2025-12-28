package models.dailyschedule;

import models.booking.BookingInterface;
import utils.ScheduleConfig;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DailySchedule {
    private final LocalDate date;
    private final Map<LocalTime, Boolean> schedule;

    public DailySchedule(LocalDate date) {
        this.date = date;
        this.schedule = new HashMap<>();
        initializeSchedule();
    }

    private void initializeSchedule() {
        LocalTime morningStartHour = ScheduleConfig.getTime("schedule.morning.start", "09:00");
        LocalTime morningEndHour = ScheduleConfig.getTime("schedule.morning.end", "13:00");

        LocalTime afternoonStartHour = ScheduleConfig.getTime("schedule.afternoon.start", "15:00");
        LocalTime afternoonEndHour = ScheduleConfig.getTime("schedule.afternoon.end", "20:00");

        fillTimeSlots(morningStartHour, morningEndHour);
        fillTimeSlots(afternoonStartHour, afternoonEndHour);
    }

    private void fillTimeSlots(LocalTime start, LocalTime end){
        LocalTime currentTime = start;
        while(currentTime.isBefore(end)) {
            this.schedule.put(currentTime, true);
            currentTime = currentTime.plusHours(1);
        }
    }

    public void syncWithBookings (List<BookingInterface> existingBookings) {
        initializeSchedule();

        if (existingBookings == null) return;

        for (BookingInterface b: existingBookings) {
            if (b.getDate().isEqual(this.date)) {
                LocalTime hour = b.getStartTime();

                if (schedule.containsKey(hour)) {
                    schedule.put(hour, false);
                }
            }
        }
    }

    public void setSlotOccupied(LocalTime hour) {
        if (this.schedule.containsKey(hour)) {
            this.schedule.put(hour, false);
        } else {
            System.err.println("[WARNING] Tentativo di occupare uno slot fuori orario e non valido: " + hour);
        }
    }

    public Map<LocalTime, Boolean> getALLSchedule() {
        return schedule;
    }

    // Restituisce lista ordinata degli orari liberi
    public List<LocalTime> getAvailableSlots() {
        return schedule.entrySet().stream()
                .filter(Map.Entry::getValue)            // Filtro per valori true
                .map(Map.Entry::getKey)                 // Filtro per chiave (ora)
                .sorted()                               // Ordinato in modo crescente
                .collect(Collectors.toList());          // Sotto forma di lista
    }
}
