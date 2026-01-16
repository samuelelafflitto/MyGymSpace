package models.booking;

import models.booking.record.BookingKey;
import models.dailyschedule.DailySchedule;
import models.training.Training;
import models.user.Athlete;

import java.math.BigDecimal;
import java.time.LocalTime;

public class ConcreteBooking implements BookingInterface {
    private Athlete athlete;
    private Training training;
    private DailySchedule dailySchedule;

    private LocalTime selectedSlot;
    private String description;
    private BigDecimal finalPrice;

    public ConcreteBooking () {
        this.description = "";
    }

    // GET
    @Override
    public Athlete getAthlete() {
        return athlete;
    }

    @Override
    public Training getTraining() {
        return training;
    }

    @Override
    public DailySchedule getDailySchedule() {
        return dailySchedule;
    }

    @Override
    public LocalTime getSelectedSlot() {
        return selectedSlot;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    // SET
    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public void setDailySchedule(DailySchedule dailySchedule) {
        this.dailySchedule = dailySchedule;
    }

    public void setSelectedSlot(LocalTime selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }


    @Override
    public BookingKey getKey() {
        return new BookingKey(this.getTraining().getPersonalTrainer().getUsername(), this.getDailySchedule().getDate(), this.getSelectedSlot());
    }
}

