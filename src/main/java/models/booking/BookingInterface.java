package models.booking;

import models.dailyschedule.DailySchedule;
import models.user.Athlete;
import models.training.Training;

import java.math.BigDecimal;
import java.time.LocalTime;

public interface BookingInterface {
    Athlete getAthlete();
    Training getTraining();
    DailySchedule getDailySchedule();
    LocalTime getSelectedSlot();
    //LocalTime getStartTime();
    String getDescription();
    BigDecimal getFinalPrice();

    BookingKey getKey();
}
