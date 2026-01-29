package models.booking;

import models.booking.record.BasicBookingDataFromPersistence;
import models.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BookingDAOFsys extends BookingDAO {
    @Override
    public void saveBooking(BookingInterface booking) {
        // Non implementato
    }

    @Override
    public void deleteBooking(String athleteUsername, String ptUsername, LocalDate date, LocalTime time) {
        // Non implementato
    }

    @Override
    public List<BasicBookingDataFromPersistence> fetchBasicBookingData(User user) {
        // Non implementato
        return List.of();
    }
}
