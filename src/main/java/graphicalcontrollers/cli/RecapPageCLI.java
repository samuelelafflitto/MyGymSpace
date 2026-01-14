package graphicalcontrollers.cli;

import beans.BookingRecapBean;
import controllers.BookingController;
import exceptions.InvalidTimeSlotException;
import utils.session.BookingSession;
import utils.session.SessionManager;

import java.util.Scanner;

public class RecapPageCLI {
    private static final String INVALIDINPUT = "Opzione selezionata non valida. Riprovare";
    BookingSession bSession = SessionManager.getInstance().getBookingSession();
    BookingController bController = new BookingController();
    AthleteMenuCLI athleteMenuCLI = new AthleteMenuCLI();
    private final Scanner sc = new Scanner(System.in);

    BookingRecapBean bookingRecap;

    public void start() {
        while(true) {
            System.out.println("------------------------");
            showRecap();

            System.out.println("-----------------------");
            System.out.println("1) Conferma e prenota");
            System.out.println("2) Annulla");
            System.out.println("--> ");

            String choice = sc.nextLine();

            // Tutti i casi in cui non si effettua la prenotazione
            if (!handleChoice(choice)) {
                clearAndGoToHome();
            }
        }
    }

    private boolean handleChoice(String choice) {
        switch (choice) {
            // Conferma e prenota
            case "1":
                try {
                    if(bController.saveBooking()) {
                        return bSession.clearBookingSession();
                    }
                    return false;
//                    saveBookingOnPersistence();
                } catch (InvalidTimeSlotException e) {
                    e.handleException();
                    return false;
                }
            // Annulla e torna alla Home
            case "2":
                return false;
            default:
                System.out.println(INVALIDINPUT);
                return true;
        }
    }

    private void showRecap() {
        bookingRecap = bController.getBookingRecap();

        System.out.println("Allenamento: " + bookingRecap.getTrainingName() + " - PT: " + bookingRecap.getPtTraining());
        System.out.println("Data e ora: " + bookingRecap.getDate() + ", " + bookingRecap.getStartTime());
        System.out.println("Extra selezionati: " + bookingRecap.getDescription());
        System.out.println("Costo totale: " + bookingRecap.getPrice());
    }

//    private void saveBookingOnPersistence() {
//        bController.saveBooking(/*bSession, booking*/);
//    }




    private void clearBookingSessionIfBookingFailed() {
        bSession.clearBookingSession();
    }

    private void clearAndGoToHome() {
        clearBookingSessionIfBookingFailed();
        athleteMenuCLI.goToHome();
    }

}
