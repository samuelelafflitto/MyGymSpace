package models.booking;

import models.booking.record.BasicBookingDataFromPersistence;
import models.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public abstract class BookingDAO {
    public abstract void saveBooking(BookingInterface booking);
    public abstract void deleteBooking(String athleteUsername, String ptUsername, LocalDate date, LocalTime time);
    public abstract List<BasicBookingDataFromPersistence> fetchBasicBookingData(User user);
}
