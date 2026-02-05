package graphicalcontrollers.cli;

import controllers.BookingController;
import utils.session.SessionManager;

public class GuestMenuCLI {
    BookingController bController = new BookingController();

    public void showMenu() {
        System.out.println("1) Upcoming Events (COMING SOON)");
        System.out.println("2) Login");
    }

    public void goToHome() {
        if(bController.isBookingSessionOpen())
            SessionManager.getInstance().freeBookingSession();
        new HomepageCLI().start();
    }

    public void goToEvents() {
        System.out.println("UPCOMING EVENTS - COMING SOON");
    }

    public void goToLogin() {
        new LoginCLI().start();
    }
}
