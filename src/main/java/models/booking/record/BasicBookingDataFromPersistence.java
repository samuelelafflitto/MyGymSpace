package models.booking.record;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record BasicBookingDataFromPersistence(String athleteUsername, String ptUsername, LocalDate date, LocalTime selectedSlot, String description, BigDecimal finalPrice) {
}
