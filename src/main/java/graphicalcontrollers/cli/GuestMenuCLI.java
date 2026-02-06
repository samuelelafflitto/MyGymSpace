package graphicalcontrollers.cli;

public class GuestMenuCLI {

    public void showMenu() {
        System.out.println("1) Upcoming Events (COMING SOON)");
        System.out.println("2) Login");
    }

    public void goToHome() {
        new HomepageCLI().start();
    }

    public void goToEvents() {
        System.out.println("UPCOMING EVENTS - COMING SOON");
        new HomepageCLI().start();
    }

    public void goToLogin() {
        new LoginCLI().start();
    }
}
