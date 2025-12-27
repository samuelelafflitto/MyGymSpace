package models.booking;

import java.util.List;

public abstract class BookingDAO {
    public abstract List<BookingInterface> getBookingByUser(String usr);
    public abstract List<BookingInterface> getBookingByTraining(String trainingName);
    public abstract void addBooking(BookingInterface booking);
}
