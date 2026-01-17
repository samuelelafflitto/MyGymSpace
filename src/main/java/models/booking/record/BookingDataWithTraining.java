package models.booking.record;

import models.training.Training;
import models.user.Athlete;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

//public record BookingDataWithTraining(Athlete athlete, Training training, String ptUsername, LocalDate date, LocalTime selectedSlot, String description, BigDecimal finalPrice) {
//}

public record BookingDataWithTraining(Athlete athlete, Training training, BasicBookingDataFromDB record) {
}