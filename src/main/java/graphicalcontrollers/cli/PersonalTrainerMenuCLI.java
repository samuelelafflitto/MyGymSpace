package graphicalcontrollers.cli;

import controllers.BookingController;
import utils.session.SessionManager;

public class PersonalTrainerMenuCLI {
    BookingController bController = new BookingController();
    SessionManager sessionManager = SessionManager.getInstance();

    public void showMenu() {
        System.out.println("1) Aggiungi un nuovo evento");
        System.out.println("2) Gestisci eventi");
        System.out.println("3) Prenotazioni degli Atleti");
        System.out.println("4) Il mio Profilo");
        System.out.println("5) Logout");
    }

    public void goToHome() {
        if(bController.isBookingSessionOpen())
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
        if(bController.isBookingSessionOpen())
            sessionManager.freeBookingSession();
        new MyProfilePageCLI().start();
    }

    public void logout() {
        if(bController.isBookingSessionOpen())
            sessionManager.freeBookingSession();
        // ALTRE EVENTUALI SESSIONI
        sessionManager.freeSession();
        new  HomepageCLI().start();
    }

}