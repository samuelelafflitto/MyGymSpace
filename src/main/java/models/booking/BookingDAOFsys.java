package models.booking;

import models.booking.record.BasicBookingDataFromDB;
import models.booking.record.NextSessionRecord;
import models.training.Training;
import models.user.Athlete;
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
    public List<BasicBookingDataFromDB> fetchBasicBookingData(User user) {
        return List.of();
    }

    @Override
    public List<BookingInterface> getBookingByTraining(Training training) {
        return List.of();
    }

//    @Override
//    public int getTotalSessions(String username, boolean isAthlete) {
//        // Non implementato
//        return 0;
//    }
//
//    @Override
//    public int getFutureSessions(String username, boolean isAthlete, LocalDate dateNow, LocalTime timeNow) {
//        // Non implementato
//        return 0;
//    }
//
//    @Override
//    public NextSessionRecord getNextSession(String username, boolean isAthlete, LocalDate dateNow, LocalTime timeNow) {
//        // Non implementato
//        return null;
//    }
}
