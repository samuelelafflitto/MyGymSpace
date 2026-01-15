package graphicalcontrollers.cli;

import utils.session.SessionManager;

public class AthleteMenuCLI {
    public void showMenu() {
        System.out.println("1) Prenota una sessione di allenamento");
        System.out.println("2) Eventi futuri");
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
        SessionManager sM = SessionManager.getInstance();

        if(sM.getLoggedUser() != null) {
            if(sM.getBookingSession() != null)
                SessionManager.getInstance().freeBookingSession();
            new TrainingSelectionPageCLI().start();
        } else {// Non dovrebbe mai accadere a runtime
            new LoginCLI().start();
        }
    }

    public void goToEvents() {// Da implementare

    }

    public void goToMyBookings() {// Da implementare

    }

    public void goToMyProfile() {// Da implementare

    }

    public void logout() {
        if(SessionManager.getInstance().getBookingSession() != null)
            SessionManager.getInstance().freeBookingSession();
        // ALTRE EVENTUALI SESSIONI
        SessionManager.getInstance().freeSession();
        new  HomepageCLI().start();
    }
}
