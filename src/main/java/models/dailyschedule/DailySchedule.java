package models.dailyschedule;

import models.booking.BookingInterface;
import utils.ScheduleConfig;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DailySchedule {
    private final LocalDate date;
    private final Map<Integer, Boolean> schedule;

    public DailySchedule(LocalDate date) {
        this.date = date;
        this.schedule = new HashMap<>();
        initializeSchedule();
    }

    private void initializeSchedule() {
        int morningStartHour = ScheduleConfig.getInt("schedule.morning.start", 9);
        int morningEndHour = ScheduleConfig.getInt("schedule.morning.end", 13);

        int afternoonStartHour = ScheduleConfig.getInt("schedule.afternoon.start", 15);
        int afternoonEndHour = ScheduleConfig.getInt("schedule.afternoon.end", 20);

        for (int h = morningStartHour; h < morningEndHour; h++) {
            this.schedule.put(h, true);
        }
        for (int h = afternoonStartHour; h < afternoonEndHour; h++) {
            this.schedule.put(h, true);
        }
    }

    public void syncWithBookings (List<BookingInterface> existingBookings) {
        initializeSchedule();

        if (existingBookings == null) return;

        for (BookingInterface b: existingBookings) {
            if (b.getDate().isEqual(this.date)) {
                int hour = b.getSlotIndex();

                if (schedule.containsKey(hour)) {
                    schedule.put(hour, false);
                }
            }
        }
    }

    public Map<Integer, Boolean> getALLSchedule() {
        return schedule;
    }

    // Restituisce lista ordinata degli orari liberi
    public List<Integer> getAvailableSlots() {
        return schedule.entrySet().stream()
                .filter(Map.Entry::getValue)            // Filtro per valori true
                .map(Map.Entry::getKey)                 // Filtro per chiave (ora)
                .sorted()                               // Ordinato in modo crescente
                .collect(Collectors.toList());          // Sotto forma di lista
    }
}
