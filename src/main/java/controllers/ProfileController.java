package controllers;

import beans.ProfileStatsBean;
import exceptions.DataLoadException;
import models.booking.BookingDAO;
import models.booking.record.NextSessionRecord;
import models.dao.factory.FactoryDAO;
import models.user.User;
import utils.session.SessionManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ProfileController {
    private static final String ATHLETE_TYPE = "ATH";

    public ProfileStatsBean getProfileStats(){
        User user = SessionManager.getInstance().getLoggedUser();
        String type = user.getType();

        ProfileStatsBean bean = new ProfileStatsBean();

        String username = user.getUsername();
        boolean isAthlete = type.equals(ATHLETE_TYPE);
        LocalDate today = LocalDate.now();
        LocalTime now =  LocalTime.now();

        try {
            BookingDAO bookingDAO = FactoryDAO.getInstance().createBookingDAO();
            bean.setStats1(bookingDAO.getTotalSessions(username, isAthlete));
            bean.setStats2(bookingDAO.getFutureSessions(username, isAthlete, today, now));
            NextSessionRecord element = bookingDAO.getNextSession(username, isAthlete, today, now);

            if (element != null){
                bean.setNextDate(element.date());
                bean.setNextTime(element.time());
            } else {
                bean.setNextDate(null);
                bean.setNextTime(null);
            }

        } catch (SQLException | DataLoadException e) {
            System.err.println(e.getMessage());
        }

        return bean;
    }
}
