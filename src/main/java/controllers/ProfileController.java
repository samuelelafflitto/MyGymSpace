package controllers;

import beans.ProfileDataBean;
import beans.ProfileStatsBean;
import beans.TrainingToEditBean;
import exceptions.InvalidPasswordConfirmationException;
import models.booking.BookingInterface;
import models.booking.record.BookingKey;
import models.dao.factory.FactoryDAO;
import models.training.Training;
import models.training.TrainingDAO;
import models.user.Athlete;
import models.user.PersonalTrainer;
import models.user.User;
import models.user.UserDAO;
import utils.session.SessionManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileController {
    private static final String ATHLETE_TYPE = "ATH";
    private static final String PT_TYPE = "PT";

    User user = SessionManager.getInstance().getLoggedUser();

    public ProfileController() {// The constructor does not need parameters
    }

    public ProfileStatsBean getProfileStats() {
        String type = user.getType();

        HashMap<BookingKey, BookingInterface> bookings = null;
        ProfileStatsBean bean = new ProfileStatsBean();


        if (type.equals(ATHLETE_TYPE)) {
            Athlete ath = (Athlete) user;
            bookings = (HashMap<BookingKey, BookingInterface>) ath.getBookings();
        } else if (type.equals(PT_TYPE)) {
            PersonalTrainer pt = (PersonalTrainer) user;
            bookings = (HashMap<BookingKey, BookingInterface>) pt.getPrivateSessions();

        }

        List<BookingInterface> bookingList = (bookings != null)
                ? new ArrayList<>(bookings.values())
                : new ArrayList<>();

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

    public boolean changePassword(ProfileDataBean bean) {
        String newPsw = bean.getNewPassword();
        String currentPsw = bean.getCurrentPassword();

        if(currentPsw.equals(user.getPassword())) {
            UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
            userDAO.updatePassword(user.getUsername(), newPsw);

            user.setPassword(newPsw);
            return true;
        } else {
            throw new InvalidPasswordConfirmationException();
        }
    }

    public boolean changeName(ProfileDataBean bean) {
        String newFirstName = bean.getNewFirstName();
        String newLastName = bean.getNewLastName();
        String currentPsw = bean.getCurrentPassword();

        if(currentPsw.equals(user.getPassword())) {
            UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
            userDAO.updateName(user.getUsername(), newFirstName, newLastName);

            user.setFirstName(newFirstName);
            user.setLastName(newLastName);
            return true;
        } else {
            throw new InvalidPasswordConfirmationException();
        }
    }

    public boolean updateTraining(TrainingToEditBean tBean) {
        Training t = new Training();
        t.setPersonalTrainer(((PersonalTrainer) SessionManager.getInstance().getLoggedUser()));
        t.setName(tBean.getName());
        t.setDescription(tBean.getDescription());
        t.setBasePrice(tBean.getBasePrice());

        TrainingDAO tDAO = FactoryDAO.getInstance().createTrainingDAO();
        try {
            tDAO.updateTrainingDetails(t);
            return true;
        } catch (Exception _) {
            return false;
        }
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
            if(isFutureSession(b, date, time) && (nextSession == null || isBeforeOf(b, nextSession))) {
                nextSession = b;
            }
        }
        return nextSession;
    }
}
