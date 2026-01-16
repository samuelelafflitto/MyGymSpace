package graphicalcontrollers.cli;

import utils.session.BookingSession;
import utils.session.SessionManager;

public class AthleteMenuCLI {
    SessionManager sessionManager = SessionManager.getInstance();
    BookingSession bSession = SessionManager.getInstance().getBookingSession();

    public void showMenu() {
        System.out.println("1) Prenota una sessione di allenamento");
        System.out.println("2) Eventi futuri");
        System.out.println("3) My Bookings");
        System.out.println("4) Il mio Profilo");
        System.out.println("5) Logout");
    }

    public void goToHome() {
        if(bSession != null)
            sessionManager.freeBookingSession();
        // ALTRE EVENTUALI SESSIONI
        new HomepageCLI().start();
    }

    public void goToBookASession() {
        if(sessionManager.getLoggedUser() != null) {
            if(bSession != null)
                sessionManager.freeBookingSession();
            new TrainingSelectionPageCLI().start();
        } else {// Non dovrebbe mai accadere a runtime
            new LoginCLI().start();
        }
    }

    public void goToEvents() {// Da implementare

    }

    public void goToMyBookings() {// Da implementare

    }

    public void goToMyProfile() {
        if(sessionManager.getLoggedUser() != null) {
            if(bSession != null)
                sessionManager.freeBookingSession();
            new MyProfilePageCLI().start();
        }
    }

    public void logout() {
        if(bSession != null)
            sessionManager.freeBookingSession();
        // ALTRE EVENTUALI SESSIONI
        sessionManager.freeSession();
        new  HomepageCLI().start();
    }
}
