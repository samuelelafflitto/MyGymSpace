package models.booking;

import models.booking.record.NextSessionRecord;
import models.training.Training;
import models.user.Athlete;

import java.time.LocalDate;
import java.time.LocalTime;
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
    public List<BookingInterface> getBookingByUser(Athlete user) {
        return bookings.stream()
                .filter(b -> b.getAthlete().getUsername().equals(user.getUsername()))
                .toList();
    }

    @Override
    public List<BookingInterface> getBookingByTraining(Training training) {
        return bookings.stream()
                .filter(b -> b.getTraining().getPersonalTrainer().getUsername().equals(training.getPersonalTrainer().getUsername()))
                .toList();
    }

    @Override
    public int getTotalSessions(String username, boolean isAthlete) {
        // Non implementato
        return 0;
    }

    @Override
    public int getFutureSessions(String username, boolean isAthlete, LocalDate dateNow, LocalTime timeNow) {
        // Non implementato
        return 0;
    }

    @Override
    public NextSessionRecord getNextSession(String username, boolean isAthlete, LocalDate dateNow, LocalTime timeNow) {
        // Non implementato
        return null;
    }
}
