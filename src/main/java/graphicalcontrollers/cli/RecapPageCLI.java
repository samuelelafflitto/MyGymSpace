package graphicalcontrollers.cli;

import beans.BookingRecapBean;
import controllers.BookingController;
import exceptions.InvalidTimeSlotException;
import utils.session.BookingSession;
import utils.session.SessionManager;

import java.util.Scanner;

public class RecapPageCLI {
    private static final String INVALIDINPUT = "Invalid Option! Try again";
    private static final String SEPARATOR = "------------------------------------------------";
    BookingSession bSession = SessionManager.getInstance().getBookingSession();
    BookingController bController = new BookingController();
    AthleteMenuCLI athleteMenuCLI = new AthleteMenuCLI();
    private final Scanner sc = new Scanner(System.in);

    BookingRecapBean bookingRecap;

    public void start() {
        System.out.println(SEPARATOR);
        showRecap();

        while(true) {
            System.out.println(SEPARATOR);
            System.out.println("1) Confirm and Book");
            System.out.println("2) Cancel and return to the Homepage");
            System.out.println("3) Logout");
            System.out.print("--> ");

            String choice = sc.nextLine();

            handleChoice(choice);
        }
    }

    private void handleChoice(String choice) {
        switch (choice) {
            case "1":
                try {
                    if(bController.saveBooking()) {
                        bSession.clearBookingSession();
                        clearAndGoToHome();
                    }
                    clearAndGoToHome();
                } catch (InvalidTimeSlotException e) {
                    e.handleException();
                    clearAndGoToHome();
                }
                break;
            case "2":
                clearAndGoToHome();
                break;
            case "3":
                athleteMenuCLI.logout();
                break;
            default:
                System.out.println(INVALIDINPUT);
                break;
        }
    }

    private void showRecap() {
        bookingRecap = bController.getBookingRecap();

        System.out.println("Training: " + bookingRecap.getTrainingName() + " - PT: " + bookingRecap.getPtLastName());
        System.out.println("Date and Hour: " + bookingRecap.getDate() + ", " + bookingRecap.getStartTime());
        System.out.println("Selected extra options: " + bookingRecap.getDescription());
        System.out.println("Final price: " + bookingRecap.getPrice() + "€");
    }

    private void clearAndGoToHome() {
        bSession.clearBookingSession();
        athleteMenuCLI.goToHome();
    }

}
