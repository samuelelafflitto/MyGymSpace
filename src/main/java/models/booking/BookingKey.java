package models.booking;

import java.time.LocalDate;
import java.time.LocalTime;

public record BookingKey(String pt_username, LocalDate date, LocalTime time) {
}
