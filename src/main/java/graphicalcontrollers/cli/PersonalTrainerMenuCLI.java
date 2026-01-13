package graphicalcontrollers.cli;

import utils.session.SessionManager;

public class PersonalTrainerMenuCLI {
    public void showMenu() {
        System.out.println("1) Aggiungi un nuovo evento");
        System.out.println("2) Gestisci eventi");
        System.out.println("3) Prenotazioni degli Atleti");
        System.out.println("4) Il mio Profilo");
    }

    public void goToHome() {
        if(SessionManager.getInstance().getBookingSession() != null)
            SessionManager.getInstance().freeBookingSession();
        // ALTRE EVENTUALI SESSIONI
        new HomepageCLI().start();
    }

    public void goToAddEvent() {

    }

    public void goToManageEvents() {

    }

    public void goToAthleteBookings() {

    }

    public void goToMyProfile() {

    }

//    public void logout() {
//        if(SessionManager.getInstance().getBookingSession() != null)
//            SessionManager.getInstance().freeBookingSession();
//        // ALTRE EVENTUALI SESSIONI
//        SessionManager.getInstance().freeSession();
//        new  HomepageCLI().start();
//    }

}