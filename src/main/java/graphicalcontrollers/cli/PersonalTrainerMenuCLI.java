package graphicalcontrollers.cli;

import utils.session.BookingSession;
import utils.session.SessionManager;

public class PersonalTrainerMenuCLI {
    SessionManager sessionManager = SessionManager.getInstance();
    BookingSession bSession = SessionManager.getInstance().getBookingSession();

    public void showMenu() {
        System.out.println("1) Aggiungi un nuovo evento");
        System.out.println("2) Gestisci eventi");
        System.out.println("3) Prenotazioni degli Atleti");
        System.out.println("4) Il mio Profilo");
        System.out.println("5) Logout");
    }

    public void goToHome() {
        if(bSession != null)
            sessionManager.freeBookingSession();
        // ALTRE EVENTUALI SESSIONI
        new HomepageCLI().start();
    }

    public void goToAddEvent() {// Da implementare

    }

    public void goToManageEvents() {// Da implementare

    }

    public void goToAthleteBookings() {// Da implementare

    }

    public void goToMyProfile() {
        if(bSession != null)
            sessionManager.freeBookingSession();
        new MyProfilePageCLI().start();
    }

    public void logout() {
        if(bSession != null)
            sessionManager.freeBookingSession();
        // ALTRE EVENTUALI SESSIONI
        sessionManager.freeSession();
        new  HomepageCLI().start();
    }

}