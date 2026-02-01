package graphicalcontrollers.cli;

import utils.session.SessionManager;

public class PersonalTrainerMenuCLI {
    SessionManager sessionManager = SessionManager.getInstance();

    public void showMenu() {
        System.out.println("1) Aggiungi un nuovo evento");
        System.out.println("2) Gestisci eventi");
        System.out.println("3) Prenotazioni degli Atleti");
        System.out.println("4) Il mio Profilo");
        System.out.println("5) Logout");
    }

    public void goToHome() {
        new HomepageCLI().start();
    }

    public void goToAddEvent() {
        System.out.println("ADD NEW EVENT - COMING SOON");
    }

    public void goToManageEvents() {
        System.out.println("MANAGE EVENTS - COMING SOON");
    }

    public void goToAthleteBookings() {
        new MyBookingsPageCLI().start();
    }

    public void goToMyProfile() {
        new MyProfilePageCLI().start();
    }

    public void logout() {
        sessionManager.freeSession();
        new  HomepageCLI().start();
    }

}