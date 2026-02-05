package models.booking;

import models.booking.record.BasicBookingDataFromPersistence;
import models.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BookingDAOFsys extends BookingDAO {
    @Override
    public void saveBooking(BookingInterface booking) {
        // Not implemented
    }

    @Override
    public void deleteBooking(String athleteUsername, String ptUsername, LocalDate date, LocalTime time) {
        // Not implemented
    }

    @Override
    public List<BasicBookingDataFromPersistence> fetchBasicBookingData(User user) {
        // Not implemented
        return List.of();
    }
}
