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
    /*private LocalTime startTime;*/
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

//    @Override
//    public LocalTime getStartTime() {
//        return startTime;
//    }

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

//    public void setStartTime(LocalTime startTime) {
//        this.startTime = startTime;
//    }

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





    /*private String id;
    private String description;
    private double cost;
    private String athleteUsername;
    private String trainingName;
    private LocalDate date;
    private String startTime;*/



//    //GET
//    @Override
//    public String getId() {
//        return id;
//    }
//
//    @Override
//    public String getDescription() {
//        return description;
//    }
//
//    @Override
//    public double getCost() {
//        return cost;
//    }
//
//    @Override
//    public String getAthlete() {
//        return athleteUsername;
//    }
//
//    @Override
//    public String getTraining() {
//        return trainingName;
//    }
//
//    @Override
//    public LocalDate getDate() {
//        return date;
//    }
//
//    @Override
//    public String getStartTime() {
//        return startTime;
//    }



//    //SET
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setCost(double cost) {
//        this.cost = cost;
//    }
//
//    public void setAthlete(String athleteUsername) {
//        this.athleteUsername = athleteUsername;
//    }
//
//    public void setTrainingName(String trainingName) {
//        this.trainingName = trainingName;
//    }
//
//    public void setDate(LocalDate date) {
//        this.date = date;
//    }
//
//    public void setStartTime(String startTime) {
//        this.startTime = startTime;
//    }
}

