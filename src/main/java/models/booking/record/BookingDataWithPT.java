package models.booking.record;

import models.training.Training;
import models.user.Athlete;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record BookingDataWithPT(Athlete athlete, Training training, LocalDate date, LocalTime selectedSlot, String description, BigDecimal finalPrice) {
}
