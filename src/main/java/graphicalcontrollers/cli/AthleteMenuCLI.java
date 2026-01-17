package graphicalcontrollers.cli;

import controllers.BookingController;
import utils.session.SessionManager;


public class AthleteMenuCLI {
    BookingController bController =  new BookingController();

    public void showMenu() {
        System.out.println("1) Prenota una sessione di allenamento");
        System.out.println("2) Eventi futuri");
        System.out.println("3) My Bookings");
        System.out.println("4) Il mio Profilo");
        System.out.println("5) Logout");
    }

    public void goToHome() {
        freeBSessionIfNotNull();
        // ALTRE EVENTUALI SESSIONI
        new HomepageCLI().start();
    }

    public void goToBookASession() {
        if(bController.isBookingSessionOpen()) {
            freeBSessionIfNotNull();
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
        if(bController.isBookingSessionOpen()) {
            freeBSessionIfNotNull();
            new MyProfilePageCLI().start();
        }
    }

    public void logout() {
        freeAll();
        new  HomepageCLI().start();
    }

    private void freeBSessionIfNotNull() {
        if(bController.isBookingSessionOpen())
            SessionManager.getInstance().freeBookingSession();
    }

    private void freeAll() {
        freeBSessionIfNotNull();
        SessionManager.getInstance().freeSession();
    }
}
