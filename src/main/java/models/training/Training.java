package models.training;

import models.dailyschedule.DailySchedule;
import models.user.PersonalTrainer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Training {
    private PersonalTrainer personalTrainer;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private final HashMap<LocalDate, DailySchedule> schedules;

    public Training() {
        this.schedules = new HashMap<>();
    }

    // GETTER
    public PersonalTrainer getPersonalTrainer() {
        return personalTrainer;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public Map<LocalDate, DailySchedule> getSchedules() {
        return schedules;
    }

    // SETTER
    public void setPersonalTrainer(PersonalTrainer personalTrainer) {
        this.personalTrainer = personalTrainer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public void setSchedules(List<DailySchedule> scheduleList) {
        this.schedules.clear();
        for (DailySchedule ds : scheduleList) {
            this.schedules.put(ds.getDate(), ds);
        }
    }


    public void addSchedule(DailySchedule dailySchedule) {
        this.schedules.put(dailySchedule.getDate(), dailySchedule);
    }
}
