package models.booking.record;

import models.dailyschedule.DailySchedule;
import models.training.Training;
import models.user.Athlete;

public record FinalBookingData(Athlete athlete, Training training, DailySchedule dailySchedule, BasicBookingDataFromPersistence previousRecord) {
}
