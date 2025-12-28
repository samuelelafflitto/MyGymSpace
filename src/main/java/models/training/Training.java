package models.training;

import models.booking.BookingInterface;
import models.dailyschedule.DailySchedule;

import java.time.LocalDate;
import java.util.HashMap;

public class Training {
    private String name;
    private String description;
    private double basePrice;
    private final HashMap<LocalDate, DailySchedule> schedules;
    /*private final HashMap<String, BookingInterface> bookings;*/

    public Training(String name, String description, double basePrice) {
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;

        this.schedules = new HashMap<>();
        /*this.bookings = new HashMap<>();*/
    }

    public DailySchedule getDailySchedule(LocalDate date) {
        if (!this.schedules.containsKey(date)) {
            this.schedules.put(date, new DailySchedule(date));
        }
        return this.schedules.get(date);
    }

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
