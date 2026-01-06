package utils.session;

import models.booking.BookingInterface;
import models.training.Training;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingSession {
    private Training training;
    private LocalDate selectedDate;
    //private LocalTime selectedTimeSlot;
    private BookingInterface booking;

    public BookingSession(Training training, LocalDate selectedDate) {
        this.training = training;
        this.selectedDate = selectedDate;
    }

    public Training getTraining() {
        return training;
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

    public void setDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    /*public void setTimeSlot(LocalTime selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;
    }*/

    public void setBooking(BookingInterface booking) {
        this.booking = booking;
    }
}
