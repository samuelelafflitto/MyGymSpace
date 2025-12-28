package models.booking;

import java.time.LocalDate;
import java.time.LocalTime;

public interface BookingInterface {
    String getId();
    String getDescription();
    double getCost();
    String getAthlete();
    String getTraining();
    LocalDate getDate();
    LocalTime getStartTime();
}
