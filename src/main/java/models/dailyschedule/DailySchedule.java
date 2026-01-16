package models.dailyschedule;

import exceptions.InvalidTimeSlotException;
import models.training.Training;

import java.time.LocalDate;

public class DailySchedule {
    private final Training training;
    private final LocalDate date;
    private StringBuilder timeSlots;


    public DailySchedule(Training training, LocalDate date, StringBuilder persistenceBits) {
        this.training = training;
        this.date = date;

        if(persistenceBits == null || persistenceBits.isEmpty()) {
            this.setTimeSlots(new StringBuilder());
            initializeSlotsToZero();
        } else {
            this.setTimeSlots(new StringBuilder(persistenceBits));
        }
    }

    // CREAZIONE DA CONFIGURAZIONE ATTUALE
    public static DailySchedule createNew(Training training, LocalDate date) {
        return new DailySchedule(training, date, null);
    }

    // CREAZIONE DA PERSISTENZA
    public static DailySchedule fromPersistence(Training training, LocalDate date, StringBuilder persistenceBits) {
        return new DailySchedule(training, date, persistenceBits);
    }

    // GET
    public Training getTraining() {
        return training;
    }

    public LocalDate getDate() {
        return date;
    }

    public StringBuilder getTimeSlots() {
        return timeSlots;
    }


    // SET
    public void setTimeSlots(StringBuilder timeSlots) {
        this.timeSlots = timeSlots;
    }



    public void setSlotOccupied(int index) {
        if(isValidIndex(index) && isSlotEmpty(index)) {
            timeSlots.setCharAt(index, '1');
        } else {
            throw new InvalidTimeSlotException();
        }
    }

    private void initializeSlotsToZero() {
        for(int i = 0; i < 24; i++) {
            this.timeSlots.append("0");
        }
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < 24;
    }

    private boolean isSlotEmpty(int index) {
        return this.timeSlots.charAt(index) == '0';
    }
}
