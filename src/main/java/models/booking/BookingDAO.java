package models.booking;

import models.booking.record.BasicBookingDataFromPersistence;
import models.training.Training;
import models.user.User;

import java.util.List;

public abstract class BookingDAO {
//    public abstract List<BookingInterface> getBookingByUser(Athlete user);
//    public abstract List<BookingInterface> getBookingByUser(User user);
    public abstract List<BookingInterface> getBookingByTraining(Training training);
    public abstract void saveBooking(BookingInterface booking);
    //TODO
    public abstract List<BasicBookingDataFromPersistence> fetchBasicBookingData(User user);
}
