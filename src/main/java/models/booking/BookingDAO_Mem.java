package models.booking;

import models.training.Training;
import models.user.Athlete;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingDAO_Mem extends BookingDAO {
    private static BookingDAO_Mem instance;
    private final List<BookingInterface> bookings;

    protected BookingDAO_Mem() {
        this.bookings = new ArrayList<>();
        initializeDemoData();
    }

    public static synchronized BookingDAO_Mem getInstance() {
        if(instance == null) {
            instance = new BookingDAO_Mem();
        }
        return instance;
    }

    public void initializeDemoData() {
        ConcreteBooking demoBooking = new ConcreteBooking();
        demoBooking.setAthlete("athlete1");
        demoBooking.setTrainingName("Box");
        demoBooking.setCost(20.00);
        demoBooking.setDate(LocalDate.now());
        demoBooking.setStartTime(LocalTime.of(10,0));
        demoBooking.setDescription("Prenotazione Demo");

        bookings.add(demoBooking);

        System.out.println("[MEM] Booking demo caricata per l'utente 'athlete1'");
    }

    @Override
    public void saveBooking(BookingInterface booking) {
        bookings.add(booking);
        System.out.println("[MEM] Prenotazione di prova salvata per '" + booking.getAthlete() + "' - Costo: " + booking.getCost());
    }

    @Override
    public List<BookingInterface> getBookingByUser(Athlete user) {
        return bookings.stream()
                .filter(b -> b.getAthlete().equals(user.getUsername()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingInterface> getBookingByTraining(Training training) {
        return bookings.stream()
                .filter(b -> b.getTraining().equals(training.getName()))
                .collect(Collectors.toList());
    }
}
