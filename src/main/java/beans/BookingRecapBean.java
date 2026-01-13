package beans;

import models.booking.BookingInterface;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingRecapBean {
    String trainingName;
    String ptTraining;
    LocalDate date;
    LocalTime startTime;
    String description;
    BigDecimal price;

    public BookingRecapBean(BookingInterface booking) {
        trainingName = booking.getTraining().getName();
        ptTraining = booking.getTraining().getPersonalTrainer().getLastName();
        date = booking.getDailySchedule().getDate();
        startTime = booking.getSelectedSlot();
        description = booking.getDescription();
        price = booking.getFinalPrice();
    }

    // GET
    public String getTrainingName() {
        return trainingName;
    }

    public String getPtTraining() {
        return ptTraining;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    // SET
    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public void setPtTraining(String ptTraining) {
        this.ptTraining = ptTraining;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
