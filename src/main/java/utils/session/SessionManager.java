package utils.session;

import beans.BookingRecapBean;
import models.user.User;

import java.util.HashMap;

public class SessionManager {
    private static SessionManager instance;
    private User loggedUser;

    private BookingRecapBean selectedBookingToDelete;

    private final HashMap<String, BookingSession> bookingSessionHashMap;

    protected SessionManager() {
        bookingSessionHashMap = new HashMap<>();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public BookingRecapBean getSelectedBookingToDelete() {
        return selectedBookingToDelete;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setSelectedBookingToDelete(BookingRecapBean booking) {
        this.selectedBookingToDelete = booking;
    }

    public void freeSession() {
        this.loggedUser = null;
    }


    // BOOKING SESSION
    public void createBookingSession(BookingSession bookingSession) {
        bookingSessionHashMap.put(this.loggedUser.getUsername(), bookingSession);
    }

    public BookingSession getBookingSession() {
        return bookingSessionHashMap.get(this.loggedUser.getUsername());
    }

    public void freeBookingSession() {
        bookingSessionHashMap.remove(loggedUser.getUsername());
    }
}
