package models.booking;

import exceptions.DataLoadException;
import models.booking.record.BasicBookingDataFromPersistence;
import models.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOMem extends BookingDAO {
    private static BookingDAOMem instance;
    private final List<BookingInterface> bookings;

    private static final String ATHLETE_TYPE = "ATH";

    protected BookingDAOMem() {
        this.bookings = new ArrayList<>();
    }

    public static synchronized BookingDAOMem getInstance() {
        if(instance == null) {
            instance = new BookingDAOMem();
        }
        return instance;
    }

    @Override
    public void saveBooking(BookingInterface booking) {
        this.bookings.add(booking);
    }

    @Override
    public void deleteBooking(String athleteUsername, String ptUsername, LocalDate date, LocalTime time) {
        boolean found = false;

        // Deleting all Bookings associated with a User
        if(ptUsername == null && date == null && time == null) {
            deleteATHBookings(athleteUsername);
            found = true;
        } else if (athleteUsername == null && date == null && time == null) {
            deletePTBookings(ptUsername);
            found = true;
        } else {
            // Deleting a single Booking
            for(int i = bookings.size() - 1; i >= 0; i--) {
                BookingInterface b = bookings.get(i);

                if(b.getAthlete().getUsername().equals(athleteUsername) &&
                        b.getTraining().getPersonalTrainer().getUsername().equals(ptUsername) &&
                        b.getDailySchedule().getDate().equals(date) &&
                        b.getSelectedSlot().equals(time)) {

                    bookings.remove(i);
                    found = true;
                    break;
                }
            }
        }

        if(!found) {
            throw new DataLoadException("[MEM] No bookings found");
        }
    }

    private void deleteATHBookings(String athleteUsername) {
        for(int i = bookings.size() - 1; i >= 0; i--) {
            BookingInterface b = bookings.get(i);

            if(b.getAthlete().getUsername().equals(athleteUsername)) {
                bookings.remove(i);
            }
        }
    }

    private void deletePTBookings(String ptUsername) {
        for(int i = bookings.size() - 1; i >= 0; i--) {
            BookingInterface b = bookings.get(i);

            if(b.getTraining().getPersonalTrainer().getUsername().equals(ptUsername)) {
                bookings.remove(i);
            }
        }
    }

    @Override
    public List<BasicBookingDataFromPersistence> fetchBasicBookingData(User user) {
        String username = user.getUsername();
        String type = user.getType();
        List<BasicBookingDataFromPersistence> basicBookingData = new ArrayList<>();

        for(BookingInterface b : bookings) {
            boolean isMatch;

            if(type.equals(ATHLETE_TYPE)) {
                isMatch = b.getAthlete().getUsername().equals(username);
            } else {
                isMatch = b.getTraining().getPersonalTrainer().getUsername().equals(username);
            }

            if(isMatch) {
                basicBookingData.add(new BasicBookingDataFromPersistence(
                        b.getAthlete().getUsername(),
                        b.getTraining().getPersonalTrainer().getUsername(),
                        b.getDailySchedule().getDate(),
                        b.getSelectedSlot(),
                        b.getDescription(),
                        b.getFinalPrice()
                ));
            }
        }

        return basicBookingData;
    }
}
