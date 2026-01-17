package models.user;

import models.booking.BookingInterface;
import models.booking.record.BookingKey;
import models.training.Training;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalTrainer extends User {
    private Training managedTraining;
    private final HashMap<BookingKey, BookingInterface> myPrivateSessions;

    // Costruttore con password
    public PersonalTrainer(String username, String password, String fName, String lName, String type){
        super(username, password, fName, lName, type);
        this.managedTraining = null;
        this.myPrivateSessions = new HashMap<>();
    }

    // Costruttore senza password
    public PersonalTrainer(String username, String fName, String lName, String type){
        super(username, null, fName, lName, type);
        this.managedTraining = null;
        this.myPrivateSessions = new HashMap<>();
    }

    public Training getTraining() {
        return managedTraining;
    }

    public Map<BookingKey, BookingInterface> getPrivateSessions() {
        return myPrivateSessions;
    }

    public void setTraining(Training managedTraining) {
        this.managedTraining = managedTraining;
    }

    public void setPrivateSessions(List<BookingInterface> bookingList) {
        this.myPrivateSessions.clear();
        for (BookingInterface b : bookingList) {
            this.myPrivateSessions.put(b.getKey(), b);
        }
    }
}
