package models.booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class ConcreteBooking implements BookingInterface {
    private String id;
    private String description;
    private double cost;
    private String athleteUsername;
    private String trainingName;
    private LocalDate date;
    private String startTime;

    public ConcreteBooking () {
        this.id = UUID.randomUUID().toString();
        this.description = "";
    }

    //GET
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public String getAthlete() {
        return athleteUsername;
    }

    @Override
    public String getTraining() {
        return trainingName;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    //SET
    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setAthlete(String athleteUsername) {
        this.athleteUsername = athleteUsername;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}

