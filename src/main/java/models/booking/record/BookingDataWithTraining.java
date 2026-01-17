package models.booking.record;

import models.training.Training;
import models.user.Athlete;

public record BookingDataWithTraining(Athlete athlete, Training training, BasicBookingDataFromPersistence record) {
}