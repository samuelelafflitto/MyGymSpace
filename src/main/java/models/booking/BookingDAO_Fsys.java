package models.booking;

import models.training.Training;
import models.user.Athlete;

import java.util.List;

public class BookingDAO_Fsys extends BookingDAO {
    @Override
    public void saveBooking(BookingInterface booking) {
        // Non implementato
    }

    @Override
    public List<BookingInterface> getBookingByUser(Athlete user) {
        return List.of();
    }

    @Override
    public List<BookingInterface> getBookingByTraining(Training training) {
        return List.of();
    }
}
