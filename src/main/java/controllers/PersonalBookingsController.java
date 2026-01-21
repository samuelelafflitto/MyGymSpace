package controllers;

import beans.BookingRecapBean;
import models.booking.BookingInterface;
import models.booking.record.BookingKey;
import models.user.Athlete;
import models.user.PersonalTrainer;
import models.user.User;
import utils.session.SessionManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class PersonalBookingsController {
    private static final String ATHLETE_TYPE = "ATH";
    private static final String PT_TYPE = "PT";

    public List<BookingRecapBean> getFutureBookingsFromMap() {
        User user = SessionManager.getInstance().getLoggedUser();
        List<BookingRecapBean> resultBeans = new ArrayList<>();

        Map<BookingKey, BookingInterface> bookingsMap = new HashMap<>();

        if(user.getType().equals(ATHLETE_TYPE)) {
            bookingsMap = ((Athlete)user).getBookings();
        } else if(user.getType().equals(PT_TYPE)) {
            bookingsMap = ((PersonalTrainer)user).getPrivateSessions();
        }

        if(bookingsMap == null) {
            System.out.println("No bookings found");
        }

        LocalDate today = LocalDate.now();
        LocalTime now =  LocalTime.now();

        assert bookingsMap != null;
        for(BookingInterface b : bookingsMap.values()) {
            LocalDate bookingDate = b.getDailySchedule().getDate();
            LocalTime bookingTime = b.getSelectedSlot();

            if(bookingDate.isBefore(today)) {
                continue;
            }
            if(bookingDate.equals(today) && bookingTime.isBefore(now)) {
                continue;
            }

            BookingRecapBean bean = new BookingRecapBean();
            bean.setTrainingName(b.getTraining().getName());
            bean.setPtTraining(b.getTraining().getPersonalTrainer().getLastName());
            bean.setDate(b.getDailySchedule().getDate());
            bean.setStartTime(b.getSelectedSlot());
            bean.setPrice(b.getFinalPrice());

            resultBeans.add(bean);
        }

        resultBeans.sort((b1, b2) -> {
            LocalDate d1 = b1.getDate();
            LocalDate d2 = b2.getDate();

            int dateCompare = d1.compareTo(d2);

            if(dateCompare != 0) {
                return dateCompare;
            }

            return b1.getStartTime().compareTo(b2.getStartTime());
        });

        return resultBeans;
    }
}
