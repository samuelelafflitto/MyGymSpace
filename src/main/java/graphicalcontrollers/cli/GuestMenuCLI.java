package graphicalcontrollers.cli;

import utils.session.BookingSession;
import utils.session.SessionManager;

public class GuestMenuCLI {
    SessionManager sessionManager = SessionManager.getInstance();
    BookingSession bSession = sessionManager.getBookingSession();

    public void showMenu() {
        System.out.println("1) Upcoming Events");
        System.out.println("2) Login");
    }

    public void goToHome() {
        if(bSession != null)
            sessionManager.freeBookingSession();
        // ALTRE EVENTUALI SESSIONI
        new HomepageCLI().start();
    }

    public void goToEvents() {// Da implementare

    }

    public void goToLogin() {
        new LoginCLI().start();
    }
}
