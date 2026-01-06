package beans;

import models.booking.BookingInterface;
import models.booking.ConcreteBooking;
import models.training.Training;

import java.time.LocalDate;

public class BookingRecapBean {
    String bookingId;
    String selectedTraining;
    LocalDate selectedDate;
    String selectedSlot;
    String bookingDescription;
    double bookingCost;

    public BookingRecapBean(BookingInterface booking) {
        selectedTraining = booking.getTraining();
        selectedDate = booking.getDate();
        selectedSlot = booking.getStartTime().toString();
        bookingDescription = booking.getDescription();
        bookingCost = booking.getCost();
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getSelectedTraining() {
        return selectedTraining;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public String getSelectedSlot() {
        return selectedSlot;
    }

    public String getBookingDescription() {
        return bookingDescription;
    }

    public double getBookingCost() {
        return bookingCost;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setSelectedTraining(String selectedTraining) {
        this.selectedTraining = selectedTraining;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void setSelectedSlot(String selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public void setBookingDescription(String bookingDescription) {
        this.bookingDescription = bookingDescription;
    }

    public void setBookingPrice(double bookingPrice) {
        this.bookingCost = bookingPrice;
    }
}
