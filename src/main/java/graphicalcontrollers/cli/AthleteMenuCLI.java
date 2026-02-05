package graphicalcontrollers.cli;

import controllers.BookingController;
import utils.session.SessionManager;


public class AthleteMenuCLI {
    BookingController bController =  new BookingController();

    public void showMenu() {
        System.out.println("1) Book a Session");
        System.out.println("2) Upcoming Events (COOMING SOON)");
        System.out.println("3) My Bookings");
        System.out.println("4) My Profile");
        System.out.println("5) Logout");
    }

    public void goToHome() {
        freeBSessionIfNotNull();
        new HomepageCLI().start();
    }

    public void goToBookASession() {
        if(SessionManager.getInstance().getLoggedUser() != null){
            freeBSessionIfNotNull();
            new TrainingSelectionPageCLI().start();
        } else {
            new LoginCLI().start();
        }
    }

    public void goToEvents() {
        System.out.println("UPCOMING EVENTS - COMING SOON");
    }

    public void goToMyBookings() {
        new MyBookingsPageCLI().start();
    }

    public void goToMyProfile() {
        if(SessionManager.getInstance().getLoggedUser() != null) {
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
