package models.booking;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class BookingDecorator implements BookingInterface {
    protected BookingInterface booking;
    protected BookingDecorator(BookingInterface booking) {
        this.booking = booking;
    }

    @Override
    public String getId() {
        return booking.getId();
    }

    @Override
    public String getDescription() {
        return booking.getDescription();
    }

    @Override
    public double getCost() {
        return booking.getCost();
    }

    @Override
    public String getAthlete() {
        return booking.getAthlete();
    }

    @Override
    public String getTraining () {
        return booking.getTraining();
    }

    @Override
    public LocalDate getDate() {
        return booking.getDate();
    }

    @Override
    public LocalTime getStartTime() {
        return booking.getStartTime();
    }
}