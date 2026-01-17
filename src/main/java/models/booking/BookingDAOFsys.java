package models.booking;

import models.booking.record.BasicBookingDataFromPersistence;
import models.training.Training;
import models.user.User;

import java.util.List;

public class BookingDAOFsys extends BookingDAO {
    @Override
    public void saveBooking(BookingInterface booking) {
        // Non implementato
    }

    @Override
    public List<BasicBookingDataFromPersistence> fetchBasicBookingData(User user) {
        return List.of();
    }

    @Override
    public List<BookingInterface> getBookingByTraining(Training training) {
        return List.of();
    }
}
