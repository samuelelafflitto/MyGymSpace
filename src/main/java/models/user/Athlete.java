package models.user;

import models.booking.BookingInterface;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Athlete extends User {
    private HashMap<String, BookingInterface> bookings;

    public Athlete(String fName, String lName, String username, String password, String type) {
        super(fName, lName, username, password, type);
        bookings = new HashMap<>();
    }

    public void addBooking(BookingInterface booking) {
        bookings.put(booking.getId(), booking);
    }

    public List<BookingInterface> getBookings() {
        return new ArrayList<>(bookings.values());
    }

    public void setBookings(List<BookingInterface> bookings) {
        for (BookingInterface booking : bookings) {
            addBooking(booking);
        }
    }
}
