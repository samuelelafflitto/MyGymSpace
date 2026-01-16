package models.training;

import models.booking.BookingInterface;
import models.booking.record.BookingKey;
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
    private final HashMap<BookingKey, BookingInterface> bookings;

    public Training() {
        this.schedules = new HashMap<>();
        this.bookings = new HashMap<>();
    }

    // GET
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

//    public Map<BookingKey, BookingInterface> getBookings() {
//        return bookings;
//    }

    // SET
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

//    public void setBookings(List<BookingInterface> bookingList) {
//        this.bookings.clear();
//        for (BookingInterface b : bookingList) {
//            this.bookings.put(b.getKey(), b);
//        }
//    }

    public void addSchedule(DailySchedule dailySchedule) {
        this.schedules.put(dailySchedule.getDate(), dailySchedule);
    }
}
