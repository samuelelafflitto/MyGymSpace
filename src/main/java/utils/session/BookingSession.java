package utils.session;

import models.booking.BookingInterface;
import models.dailyschedule.DailySchedule;
import models.training.Training;

import java.time.LocalDate;

public class BookingSession {
    private Training training;
    private DailySchedule dailySchedule;
    private LocalDate selectedDate;
    //private LocalTime selectedTimeSlot;
    private BookingInterface booking;

    public BookingSession(Training training/*, LocalDate selectedDate*/) {
        this.training = training;
//        this.dailySchedule = null;
//        this.selectedDate = null;
//        this.booking = null;
        /*this.selectedDate = selectedDate;*/
    }

    public Training getTraining() {
        return training;
    }

    public DailySchedule getDailySchedule() {
        return dailySchedule;
    }

    public LocalDate getDate() {
        return selectedDate;
    }

    /*public LocalTime getTimeSlot() {
        return selectedTimeSlot;
    }*/

    public BookingInterface getBooking() {
        return booking;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public void setDailySchedule(DailySchedule dailySchedule) {
        this.dailySchedule = dailySchedule;
    }

    public void setDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    /*public void setTimeSlot(LocalTime selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;
    }*/

    public void setBooking(BookingInterface booking) {
        this.booking = booking;
    }

    public boolean clearBookingSession() {
        this.training = null;
        this.dailySchedule = null;
        this.selectedDate = null;
        this.booking = null;

        return true;
    }
}
