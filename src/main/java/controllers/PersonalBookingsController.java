package controllers;

import beans.BookingRecapBean;
import models.booking.BookingInterface;
import models.booking.record.BookingKey;
import models.user.User;
import utils.session.SessionManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class PersonalBookingsController {

    public PersonalBookingsController() {// The constructor does not need parameters
    }

    BookingController bController =  new BookingController();

    // Obtaining Active Booking
    public List<BookingRecapBean> getActiveBookingsFromMap() {
        return getBookingsInternal(true);
    }

    // Obtaining Past Booking
    public List<BookingRecapBean> getPastBookingsFromMap() {
        return getBookingsInternal(false);
    }

    // Orderly obtaining of the Booking list
    private List<BookingRecapBean> getBookingsInternal(boolean fetchFuture) {
        User user = SessionManager.getInstance().getLoggedUser();
        List<BookingRecapBean> resultBeans = new ArrayList<>();

        Map<BookingKey, BookingInterface> bookingsMap = bController.getBookingsMap(user);

        if(bookingsMap == null) {
            System.out.println("No bookings found");
            bookingsMap = new HashMap<>();
        }

        LocalDate today = LocalDate.now();
        LocalTime now =  LocalTime.now();

        for(BookingInterface b : bookingsMap.values()) {
            LocalDate bDate = b.getDailySchedule().getDate();
            LocalTime bTime = b.getSelectedSlot();

            boolean isPastEvent = bDate.isBefore(today) || (bDate.equals(today) && bTime.isBefore(now));

            if(fetchFuture != isPastEvent) {
                BookingRecapBean bean = new BookingRecapBean();
                bean.setTrainingName(b.getTraining().getName());

                if(b.getTraining().getPersonalTrainer() != null) {
                    bean.setPtTraining(b.getTraining().getPersonalTrainer().getUsername());
                    bean.setPtLastName(b.getTraining().getPersonalTrainer().getLastName());
                }

                if(b.getAthlete() != null) {
                    bean.setAthCompleteName(b.getAthlete().getFirstName() + " " + b.getAthlete().getLastName());
                }

                bean.setAthCompleteName(b.getAthlete().getFirstName() + " " + b.getAthlete().getLastName());
                bean.setDate(bDate);
                bean.setStartTime(bTime);
                bean.setDescription(b.getDescription());
                bean.setPrice(b.getFinalPrice());

                resultBeans.add(bean);
            }
        }

        resultBeans.sort(Comparator.comparing(BookingRecapBean::getDate).thenComparing(BookingRecapBean::getStartTime));

        if(!fetchFuture) {
            Collections.reverse(resultBeans);
        }
        return resultBeans;
    }
}
