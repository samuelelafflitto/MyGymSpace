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

    public List<BookingRecapBean> getActiveBookingsFromMap() {
        return getBookingsInternal(true);
    }

    public List<BookingRecapBean> getPastBookingsFromMap() {
        return getBookingsInternal(false);
    }

    private List<BookingRecapBean> getBookingsInternal(boolean fetchFuture) {
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
            LocalDate bDate = b.getDailySchedule().getDate();
            LocalTime bTime = b.getSelectedSlot();

            boolean isPastEvent = bDate.isBefore(today) || (bDate.equals(today) && bTime.isBefore(now));

            if(fetchFuture && isPastEvent) {
                continue;
            }
            if(!fetchFuture && !isPastEvent) {
                continue;
            }

            BookingRecapBean bean = new BookingRecapBean();
            bean.setTrainingName(b.getTraining().getName());

            if(b.getTraining().getPersonalTrainer() != null) {
                bean.setPtTraining(b.getTraining().getPersonalTrainer().getLastName());
            }

            bean.setAthCompleteName(b.getAthlete().getFirstName() + " " + b.getAthlete().getLastName());
            bean.setDate(bDate);
            bean.setStartTime(bTime);
            bean.setDescription(b.getDescription());
            bean.setPrice(b.getFinalPrice());

            resultBeans.add(bean);
        }

        resultBeans.sort(Comparator.comparing(BookingRecapBean::getDate).thenComparing(BookingRecapBean::getStartTime));

        if(!fetchFuture) {
            Collections.reverse(resultBeans);
        }
        return resultBeans;
    }
}
