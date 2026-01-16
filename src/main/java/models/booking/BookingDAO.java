package models.booking;

import models.booking.record.NextSessionRecord;
import models.training.Training;
import models.user.Athlete;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public abstract class BookingDAO {
    public abstract List<BookingInterface> getBookingByUser(Athlete user);
    public abstract List<BookingInterface> getBookingByTraining(Training training);
    public abstract void saveBooking(BookingInterface booking);

    public abstract int getTotalSessions(String username, boolean isAthlete) throws SQLException;
    public abstract int getFutureSessions(String username, boolean isAthlete, LocalDate dateNow, LocalTime timeNow) throws SQLException;
    public abstract NextSessionRecord getNextSession(String username, boolean isAthlete, LocalDate dateNow, LocalTime timeNow) throws SQLException;
}
