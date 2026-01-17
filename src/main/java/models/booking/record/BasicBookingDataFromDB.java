package models.booking.record;

import models.user.Athlete;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

//public record BasicBookingDataFromDB(Athlete athlete, String ptUsername, LocalDate date, LocalTime selectedSlot, String description, BigDecimal finalPrice) {
//}

public record BasicBookingDataFromDB(String athleteUsername, String ptUsername, LocalDate date, LocalTime selectedSlot, String description, BigDecimal finalPrice) {
}
