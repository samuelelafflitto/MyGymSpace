package models.booking.record;

import models.user.Athlete;
import models.user.PersonalTrainer;

public record BookingDataWithUsers(Athlete athlete, PersonalTrainer pt, BasicBookingDataFromDB record) {
}
