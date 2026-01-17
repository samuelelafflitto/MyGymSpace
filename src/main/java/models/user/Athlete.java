package models.user;

import models.booking.BookingInterface;
import models.booking.record.BookingKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Athlete extends User {
    private HashMap<BookingKey, BookingInterface> myBookings;

    // Costruttore con password
    public Athlete(String username, String password, String fName, String lName, String type) {
        super(username, password, fName, lName, type);
        myBookings = new HashMap<>();
    }

    // Costruttore senza password
    public Athlete(String username, String fName, String lName, String type) {
        super(username, null,  fName, lName, type);
        myBookings = new HashMap<>();
    }

    public HashMap<BookingKey, BookingInterface> getBookings() {
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
