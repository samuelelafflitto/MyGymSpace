package models.booking;

import models.booking.record.BasicBookingDataFromPersistence;
import models.training.Training;
import models.user.User;

import java.util.ArrayList;
import java.util.List;

public class BookingDAOMem extends BookingDAO {
    private static BookingDAOMem instance;
    private final List<BookingInterface> bookings;

    protected BookingDAOMem() {
        this.bookings = new ArrayList<>();
    }

    public static synchronized BookingDAOMem getInstance() {
        if(instance == null) {
            instance = new BookingDAOMem();
        }
        return instance;
    }

    @Override
    public void saveBooking(BookingInterface booking) {
        this.bookings.add(booking);
    }

    @Override
    public List<BasicBookingDataFromPersistence> fetchBasicBookingData(User user) {
        //TODO
        return List.of();
    }

    @Override
    public List<BookingInterface> getBookingByTraining(Training training) {
        return bookings.stream()
                .filter(b -> b.getTraining().getPersonalTrainer().getUsername().equals(training.getPersonalTrainer().getUsername()))
                .toList();
    }
}
