package models.user;

import java.util.HashMap;

public class Athlete extends User {
    private HashMap<String, BookingComponent> bookings;

    public Athlete(String fname, String lname, String username, String password, String type) {
        super(fname, lname, username, password, type);
        bookings = new HashMap<>();
    }
}
