package graphicalcontrollers.cli;

import utils.session.SessionManager;

public class AthleteMenuCLI {
    public void showMenu() {
        System.out.println("1) Book a Session");
        System.out.println("2) Upcoming Events");
        System.out.println("3) My Bookings");
        System.out.println("4) Il mio Profilo");
    }

    public void goToHome() {
        if(SessionManager.getInstance().getBookingSession() != null)
            SessionManager.getInstance().freeBookingSession();
        // ALTRE EVENTUALI SESSIONI
        new HomepageCLI().start();
    }

    public void goToBookASession() {
        new TrainingSelectionPageCLI().start();
    }

    public void goToEvents() {

    }

    public void goToMyBookings() {

    }

    public void goToMyProfile() {

    }

    public void logout() {
        if(SessionManager.getInstance().getBookingSession() != null)
            SessionManager.getInstance().freeBookingSession();
        // ALTRE EVENTUALI SESSIONI
        SessionManager.getInstance().freeSession();
        new  HomepageCLI().start();
    }
}
