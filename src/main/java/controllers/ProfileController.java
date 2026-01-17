package controllers;

import beans.ProfileStatsBean;
import exceptions.DataLoadException;
import models.booking.BookingInterface;
import models.booking.record.BookingKey;
import models.booking.record.NextSessionRecord;
import models.user.Athlete;
import models.user.PersonalTrainer;
import models.user.User;
import utils.session.SessionManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileController {
    private static final String ATHLETE_TYPE = "ATH";
    private static final String PT_TYPE = "PT";

    public ProfileController() {// Il costruttore non ha bisogno di parametri
    }

    public ProfileStatsBean getProfileStats() {
        User user = SessionManager.getInstance().getLoggedUser();
        String type = user.getType();

        HashMap<BookingKey, BookingInterface> bookings = null;
        ProfileStatsBean bean = new ProfileStatsBean();


        if (type.equals(ATHLETE_TYPE)) {
            Athlete ath = (Athlete) user;
            bookings = ath.getBookings();
        } else if (type.equals(PT_TYPE)) {
            PersonalTrainer pt = (PersonalTrainer) user;
            bookings = pt.getPrivateSessions();

        }

        List<BookingInterface> bookingList = (bookings != null)
                ? new ArrayList<>(bookings.values())
                : new ArrayList<>();

//        String username = user.getUsername();
//        boolean isAthlete = type.equals(ATHLETE_TYPE);
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        bean.setStats1(getTotalSessions(bookingList));
        bean.setStats2(getFutureSessions(bookingList, today, now));

        BookingInterface nextSession = getNextSession(bookingList, today, now);

        if (nextSession != null) {
            bean.setNextDate(nextSession.getDailySchedule().getDate());
            bean.setNextTime(nextSession.getSelectedSlot());
        } else {
            bean.setNextDate(null);
            bean.setNextTime(null);
        }

        return bean;
    }


    private int getTotalSessions(List<BookingInterface> bookings) {
        if (bookings == null)
            return 0;
        return bookings.size();
    }

    private int getFutureSessions(List<BookingInterface> bookings, LocalDate date, LocalTime time) {
        int count = 0;
        for (BookingInterface b : bookings) {
            if (b.getDailySchedule().getDate().isAfter(date) || (b.getDailySchedule().getDate().isEqual(date) && b.getSelectedSlot().isAfter(time))) {
                count++;
            }
        }
        return count;
    }



    // HELPER
    private boolean isFutureSession(BookingInterface booking, LocalDate date, LocalTime time) {
        LocalDate sessionDate = booking.getDailySchedule().getDate();
        LocalTime sessionTime = booking.getSelectedSlot();

        return sessionDate.isAfter(date) || (sessionDate.isEqual(date) && sessionTime.isAfter(time));
    }

    private boolean isBeforeOf(BookingInterface booking1, BookingInterface booking2) {
        LocalDate date1 = booking1.getDailySchedule().getDate();
        LocalTime time1 = booking1.getSelectedSlot();
        LocalDate date2 = booking2.getDailySchedule().getDate();
        LocalTime time2 = booking2.getSelectedSlot();

        if (date1.isBefore(date2))
            return true;
        if (date1.isEqual(date2)) {
            return time1.isBefore(time2);
        }
        return false;
    }

    private BookingInterface getNextSession(List<BookingInterface> bookings, LocalDate date, LocalTime time) {
        if (bookings == null || bookings.isEmpty())
            return null;
        BookingInterface nextSession = null;

        for (BookingInterface b : bookings) {
            if(isFutureSession(b, date, time)) {
                if(nextSession == null || isBeforeOf(b, nextSession)) {
                    nextSession = b;
                }
            }
        }
        return nextSession;
    }
}
