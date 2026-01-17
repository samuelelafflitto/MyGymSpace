package graphicalcontrollers.cli;

import controllers.BookingController;
import utils.session.SessionManager;

public class GuestMenuCLI {
    BookingController bController = new BookingController();

    public void showMenu() {
        System.out.println("1) Upcoming Events");
        System.out.println("2) Login");
    }

    public void goToHome() {
        if(bController.isBookingSessionOpen())
            SessionManager.getInstance().freeBookingSession();
        // ALTRE EVENTUALI SESSIONI
        new HomepageCLI().start();
    }

    public void goToEvents() {// Da implementare

    }

    public void goToLogin() {
        new LoginCLI().start();
    }
}
