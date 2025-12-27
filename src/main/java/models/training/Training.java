package models.training;

import java.time.LocalDate;
import java.util.HashMap;

public class Training {
    private String name;
    private String description;
    private double basePrice;
    private final HashMap<LocalDate, DailySchedule> schedules;
    private final HashMap<String, BookingComponent> bookings;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
}
