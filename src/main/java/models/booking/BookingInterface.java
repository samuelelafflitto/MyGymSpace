package models.booking;

import java.time.LocalDate;

public interface BookingInterface {
    String getId();
    String getDescription();
    double getCost();
    String getAthlete();
    String getTraining();
    LocalDate getDate();
    int getSlotIndex();
}
