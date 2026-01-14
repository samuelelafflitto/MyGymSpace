package models.booking;

import models.dailyschedule.DailySchedule;
import models.training.Training;
import models.user.Athlete;

import java.math.BigDecimal;
import java.time.LocalTime;

public abstract class BookingDecorator implements BookingInterface {
    protected BookingInterface booking;
    protected BookingDecorator(BookingInterface booking) {
        this.booking = booking;
    }

    @Override
    public Athlete getAthlete() {
        return booking.getAthlete();
    }

    @Override
    public Training getTraining() {
        return booking.getTraining();
    }

    @Override
    public DailySchedule getDailySchedule() {
        return booking.getDailySchedule();
    }

    @Override
    public LocalTime getSelectedSlot() {
        return booking.getSelectedSlot();
    }

//    @Override
//    public LocalTime getStartTime() {
//        return booking.getStartTime();
//    }

    @Override
    public String getDescription() {
        return booking.getDescription();
    }

    @Override
    public BigDecimal getFinalPrice() {
        return booking.getFinalPrice();
    }

    @Override
    public BookingKey getKey() {
        return booking.getKey();
    }
}