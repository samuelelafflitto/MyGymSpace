package models.booking;

import models.training.Training;
import models.user.Athlete;

import java.util.List;

public abstract class BookingDAO {
    public abstract List<BookingInterface> getBookingByUser(Athlete user);
    public abstract List<BookingInterface> getBookingByTraining(Training training);
    public abstract void saveBooking(BookingInterface booking);
}
