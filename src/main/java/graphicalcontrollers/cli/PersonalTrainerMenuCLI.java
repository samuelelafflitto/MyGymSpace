package graphicalcontrollers.cli;

import utils.session.SessionManager;

public class PersonalTrainerMenuCLI {
    SessionManager sessionManager = SessionManager.getInstance();

    public void showMenu() {
        System.out.println("1) Add a New Event (COMING SOON)");
        System.out.println("2) Manage Events (COMING SOON)");
        System.out.println("3) Athlete's Bookings");
        System.out.println("4) My Profile");
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