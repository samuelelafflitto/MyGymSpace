package models.training;

/*import models.booking.BookingInterface;*/
import models.dailyschedule.DailySchedule;
import models.user.PersonalTrainer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Training {
    private String name;
    private String description;
    private double basePrice;
    private PersonalTrainer personalTrainer;
    private Map<LocalDate, DailySchedule> schedules;
    /*private final HashMap<String, BookingInterface> bookings;*/

    public Training(String name, String description, PersonalTrainer personalTrainer, double basePrice) {
        this.name = name;
        this.description = description;
        this.personalTrainer = personalTrainer;
        this.basePrice = basePrice;

        this.schedules = new HashMap<>();
        /*this.bookings = new HashMap<>();*/
    }

    // Se non esiste gia una DailySchedule associata alla LocalDate la crea
    public DailySchedule getDailySchedule(LocalDate date) {
        if(!this.schedules.containsKey(date)) {
            this.schedules.put(date, new DailySchedule(date));
        }
        return this.schedules.get(date);
    }

    // Restituisce gli slot disponibili tramite la DailySchedule associata alla LocalDate
    public List<String> getFreeSlots(LocalDate date) {
        return getDailySchedule(date).getAvailableSlots();
    }

    public void addDailySchedule(DailySchedule ds) {
        if(ds != null && ds.getDate() != null) {
            this.schedules.put(ds.getDate(), ds);
        }
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

    public PersonalTrainer getPersonalTrainer() {
        return personalTrainer;
    }

    public Map<LocalDate, DailySchedule> getSchedules() {
        return this.schedules;
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

    public void setPersonalTrainer(PersonalTrainer personalTrainer) {
        this.personalTrainer = personalTrainer;
    }

    public void setSchedules(Map<LocalDate, DailySchedule> schedules) {
        if(schedules != null) {
            this.schedules = schedules;
        } else {
            this.schedules = new HashMap<>();
        }
    }

}
