package models.booking.record;

import models.dailyschedule.DailySchedule;
import models.training.Training;
import models.user.Athlete;

import java.math.BigDecimal;
import java.time.LocalTime;

//public record FinalBookingData(Athlete athlete, Training training, DailySchedule dailySchedule, LocalTime selectedSlot, String description, BigDecimal finalPrice) {
//}

public record FinalBookingData(Athlete athlete, Training training, DailySchedule dailySchedule, BasicBookingDataFromDB record) {
}
