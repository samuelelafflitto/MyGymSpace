package models.dailyschedule;

import models.training.Training;
//import utils.ScheduleConfig;
//import utils.session.BookingSession;
//import utils.session.SessionManager;

import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.List;

public class DailySchedule {
    private final Training training;
    private final LocalDate date;

//    private final LocalTime morningStart;
//    private final LocalTime morningEnd;
//    private final LocalTime afternoonStart;
//    private final LocalTime afternoonEnd;

//    private final int morningSlots;
//    private final int afternoonSlots;
//    private final int totalSlots;

    private StringBuilder timeSlots;


    public DailySchedule(Training training, LocalDate date, /*LocalTime mStart, LocalTime mEnd, LocalTime aStart, LocalTime aEnd,*/ String persistenceBits) {
        this.training = training;
        this.date = date;

//        this.morningStart = mStart;
//        this.morningEnd = mEnd;
//        this.afternoonStart = aStart;
//        this.afternoonEnd = aEnd;

        /*this.morningStart = ScheduleConfig.getTime("schedule.morning.start", "09:00");
        this.morningEnd = ScheduleConfig.getTime("schedule.morning.end", "13:00");

        this.afternoonStart = ScheduleConfig.getTime("schedule.afternoon.start", "15:00");
        this.afternoonEnd = ScheduleConfig.getTime("schedule.afternoon.end", "20:00");

        int mSlot = (int) ChronoUnit.HOURS.between(morningStart, morningEnd);
        int aSlot = (int) ChronoUnit.HOURS.between(afternoonStart, afternoonEnd);*/

//        this.morningSlots = (int) ChronoUnit.HOURS.between(morningStart, morningEnd);
//        this.afternoonSlots = (int) ChronoUnit.HOURS.between(afternoonStart, afternoonEnd);

        /*if(morningSlots < 0 || afternoonSlots < 0) {
            throw new IllegalStateException("Orari configurati non validi.");
        }*/

        /*if(mSlot < 0) {
            this.morningSlots = 0;
        } else {
            this.morningSlots = mSlot;
        }

        if(aSlot < 0) {
            this.afternoonSlots = 0;
        } else {
            this.afternoonSlots = aSlot;
        }*/

//        this.totalSlots = morningSlots + afternoonSlots;

        if(persistenceBits == null || persistenceBits.isEmpty()) {
            this.setTimeSlots(new StringBuilder());
            this.initializeSlotsToZero();
        } else {
            this.setTimeSlots(new StringBuilder(persistenceBits));
            this.timeSlots = new StringBuilder(persistenceBits);
        }

        /*this.timeSlots = new StringBuilder();
        for(int i = 0; i < this.totalSlots; i++) {
            timeSlots.append("0");
        }*/
    }

    // CREAZIONE DA CONFIGURAZIONE ATTUALE
    public static DailySchedule createNew(Training training, LocalDate date) {
//        LocalTime mStart = ScheduleConfig.getTime("schedule.morning.start", "09:00");
//        LocalTime mEnd = ScheduleConfig.getTime("schedule.morning.end", "13:00");
//
//        LocalTime aStart = ScheduleConfig.getTime("schedule.afternoon.start", "15:00");
//        LocalTime aEnd = ScheduleConfig.getTime("schedule.afternoon.end", "20:00");

        return new DailySchedule(training, date, /*mStart, mEnd, aStart, aEnd,*/ null);
    }

    // CREAZIONE DA PERSISTENZA
    public static DailySchedule fromPersistence(Training training, LocalDate date, /*LocalTime mStart, LocalTime mEnd, LocalTime aStart, LocalTime aEnd,*/ String persistenceBits) {
        return new DailySchedule(training, date, /*mStart, mEnd, aStart, aEnd,*/ persistenceBits);
    }

    // GET
    public Training getTraining() {
        return training;
    }

    public LocalDate getDate() {
        return date;
    }

//    public LocalTime getMorningStart() {
//        return morningStart;
//    }
//
//    public LocalTime getMorningEnd() {
//        return morningEnd;
//    }
//
//    public LocalTime getAfternoonStart() {
//        return afternoonStart;
//    }
//
//    public LocalTime getAfternoonEnd() {
//        return afternoonEnd;
//    }
//
//    public int getMorningSlots() {
//        return morningSlots;
//    }
//
//    public int getAfternoonSlots() {
//        return afternoonSlots;
//    }
//
//    public int getTotalSlots() {
//        return totalSlots;
//    }

    public StringBuilder getTimeSlots() {
        return timeSlots;
    }


    // SET
    public void setTimeSlots(StringBuilder timeSlots) {
        this.timeSlots = timeSlots;
    }



    public void setSlotOccupied(int index) {
        if(isValidIndex(index)) {
            timeSlots.setCharAt(index, '1');
        }
    }

    private void initializeSlotsToZero() {
        for(int i = 0; i < 24; i++) {
            this.timeSlots.append("0");
        }
    }


    /*public List<String> getAvailableSlots() {
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
    }*/


//    // Conversione indice -> orario
//    private String indexToTime(int index) {
//        LocalTime time;
//
//        if(index < morningSlots) {
//            time = morningStartHour.plusHours(index);
//        } else {
//            int afternoonIndex = index - morningSlots;
//            time = afternoonStartHour.plusHours(afternoonIndex);
//        }
//        // Restituisce il LocalTime sotto forma di String
//        return time.toString();
//    }
//
//    // Conversione orario -> indice
//    private int timeToIndex(String time) {
//        try {
//            LocalTime selectedTime = LocalTime.parse(time);
//
//            // Orario cade nella mattina
//            long morningHours = ChronoUnit.HOURS.between(morningStartHour, selectedTime);
//            if(morningHours >= 0 && morningHours < morningSlots) {
//                return (int) morningHours;
//            }
//
//            // Orario cade nel pomeriggio
//            long afternoonHours = ChronoUnit.HOURS.between(afternoonStartHour, selectedTime);
//            if(afternoonHours >= 0 && afternoonHours < afternoonSlots) {
//                return morningSlots + (int) afternoonHours;
//            }
//        } catch (Exception _) {
//            return -1;
//        }
//        // Ritorna -1 se orario non valido
//        return -1;
//    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < 24;
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
