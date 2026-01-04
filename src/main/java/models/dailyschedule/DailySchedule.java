package models.dailyschedule;

import utils.ScheduleConfig;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DailySchedule {
    private final LocalDate date;

    private StringBuilder timeSlots;

    private LocalTime morningStartHour;
    private int morningSlots;

    private LocalTime afternoonStartHour;
    private int afternoonSlots;

    private int totalSlots;

    public DailySchedule(LocalDate date) {
        this.date = date;

        this.morningStartHour = ScheduleConfig.getTime("schedule.morning.start", "09:00");
        LocalTime morningEndHour = ScheduleConfig.getTime("schedule.morning.end", "13:00");

        this.afternoonStartHour = ScheduleConfig.getTime("schedule.afternoon.start", "15:00");
        LocalTime afternoonEndHour = ScheduleConfig.getTime("schedule.afternoon.end", "20:00");

        this.morningSlots = (int) ChronoUnit.HOURS.between(morningStartHour, morningEndHour);
        this.afternoonSlots = (int) ChronoUnit.HOURS.between(afternoonStartHour, afternoonEndHour);

        if(morningSlots < 0) morningSlots = 0;
        if(afternoonSlots < 0) afternoonSlots = 0;
        this.totalSlots = morningSlots + afternoonSlots;

        this.timeSlots = new StringBuilder();
        for (int i = 0; i < this.totalSlots; i++) {
            this.timeSlots.append("0");
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTimeSlotBits() {
        return timeSlots.toString();
    }

    public void setSlotBits(String bits) {
        if(bits != null) {
            this.timeSlots = new StringBuilder(bits);
            while(this.timeSlots.length() < totalSlots) this.timeSlots.append("0");
            if(this.timeSlots.length() > totalSlots) this.timeSlots.setLength(totalSlots);
        }
    }

    public List<String> getAvailableSlots() {
        List<String> availableSlots = new ArrayList<>();
        for (int i = 0; i < totalSlots; i++) {
            if(timeSlots.charAt(i) == '0') {
                availableSlots.add(indexToTime(i));
            }
        }
        return availableSlots;
    }

    public void setSlotOccupied(String slotString) {
        int index = timeToIndex(slotString);
        if(isValidIndex(index)) {
            timeSlots.setCharAt(index, '1');
        }
    }

    public void setSlotFree(String slotString) {
        int index = timeToIndex(slotString);
        if(isValidIndex(index)) {
            timeSlots.setCharAt(index, '0');
        }
    }

    public boolean isSlotBooked(String slotString) {
        int index = timeToIndex(slotString);
        if(!isValidIndex(index)) return false;
        return timeSlots.charAt(index) == '1';
    }


    // Conversione indice -> orario
    private String indexToTime(int index) {
        LocalTime time;

        if(index < morningSlots) {
            time = morningStartHour.plusHours(index);
        } else {
            int afternoonIndex = index - afternoonSlots;
            time = afternoonStartHour.plusHours(afternoonIndex);
        }
        // Restituisce il LocalTime sotto forma di String
        return time.toString();
    }

    // Conversione orario -> indice
    private int timeToIndex(String time) {
        try {
            LocalTime selectedTime = LocalTime.parse(time);

            // Orario cade nella mattina
            long morningHours = ChronoUnit.HOURS.between(morningStartHour, selectedTime);
            if(morningHours >= 0 && morningHours < morningSlots) {
                return (int) morningHours;
            }

            // Orario cade nel pomeriggio
            long afternoonHours = ChronoUnit.HOURS.between(afternoonStartHour, selectedTime);
            if(afternoonHours >= 0 && afternoonHours < afternoonSlots) {
                return morningSlots + (int) afternoonHours;
            }
        } catch (Exception _) {
            return -1;
        }
        // Ritorna -1 se orario non valido
        return -1;
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < totalSlots;
    }



    /*private final Map<LocalTime, Boolean> schedule;

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
    }*/
}
