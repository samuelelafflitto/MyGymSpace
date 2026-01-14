package models.user;

import models.booking.BookingInterface;
import models.booking.BookingKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Athlete extends User {
    private HashMap<BookingKey, BookingInterface> myBookings;

    public Athlete(String username, String password, String fName, String lName, String type) {
        super(username, password, fName, lName, type);
        myBookings = new HashMap<>();
    }

    public Map<BookingKey, BookingInterface> getBookings() {
        return myBookings;
    }

    public void setBookings(List<BookingInterface> bookingList) {
        this.myBookings.clear();
        for (BookingInterface b : bookingList) {
            this.myBookings.put(b.getKey(), b);
        }
    }

    public void addBooking(BookingInterface booking) {
        BookingKey key = new BookingKey(booking.getTraining().getPersonalTrainer().getUsername(), booking.getDailySchedule().getDate(), booking.getSelectedSlot());
        this.myBookings.put(key, booking);
    }
}
