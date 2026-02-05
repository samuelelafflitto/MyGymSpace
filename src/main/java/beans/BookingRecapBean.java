package beans;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingRecapBean {
    String trainingName;
    String ptTraining;
    String ptLastName;
    String athCompleteName;
    LocalDate date;
    LocalTime startTime;
    String description;
    BigDecimal price;

    public BookingRecapBean() {// The constructor does not need parameters
    }

    // GETTER
    public String getTrainingName() {
        return trainingName;
    }

    public String getPtTraining() {
        return ptTraining;
    }

    public String getPtLastName() {
        return ptLastName;
    }

    public String getAthCompleteName() {
        return athCompleteName;
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

    // SETTER
    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public void setPtTraining(String ptTraining) {
        this.ptTraining = ptTraining;
    }

    public void setPtLastName(String ptLastName) {
        this.ptLastName = ptLastName;
    }

    public void setAthCompleteName(String athCompleteName) {
        this.athCompleteName = athCompleteName;
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
