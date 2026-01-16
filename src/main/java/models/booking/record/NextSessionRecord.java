package models.booking.record;

import java.time.LocalDate;
import java.time.LocalTime;

public record NextSessionRecord(LocalDate date, LocalTime time) {
}
