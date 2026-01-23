package graphicalcontrollers.cli;

import beans.BookingRecapBean;
import controllers.PersonalBookingsController;

import java.util.List;
import java.util.Scanner;

public class MyBookingsPageCLI {
    private static final String INVALIDINPUT = "Opzione selezionata non valida. Riprovare";
    private static final String SEPARATOR = "------------------------------------------------";
    AthleteMenuCLI athleteMenuCLI = new AthleteMenuCLI();
    private static final Scanner sc = new Scanner(System.in);

    PersonalBookingsController pBController = new PersonalBookingsController();

    public void start() {
        while (true) {
            System.out.println(SEPARATOR);
            System.out.println("1) Visualizza Prenotazioni Attive");
            System.out.println("2) Visualizza Prenotazioni Passate");
            System.out.println("3) Torna alla Homepage");
            System.out.println("4) Logout");
            System.out.print("--> ");

            String choice = sc.nextLine();
            handleChoice(choice);
        }
    }

    private void handleChoice(String choice) {
        switch (choice) {
            case "1":
                showActiveBookings();
                break;
            case "2":
                showPastBookings();
                break;
            case "3":
                athleteMenuCLI.goToHome();
                break;
            case "4":
                athleteMenuCLI.logout();
                break;
            default:
                System.out.println(INVALIDINPUT);
                break;
        }
    }

    private void showActiveBookings() {
        List<BookingRecapBean> activeList = pBController.getActiveBookingsFromMap();
        System.out.println("\n");
        showBookings(activeList);
        System.out.println("\n");
    }

    private void showPastBookings() {
        List<BookingRecapBean> pastList = pBController.getPastBookingsFromMap();
        System.out.println("\n");
        showBookings(pastList);
        System.out.println("\n");
    }

    private void showBookings(List<BookingRecapBean> bList) {
        System.out.println(SEPARATOR);
        for(BookingRecapBean bean : bList) {
            System.out.println("Allenamento: " + bean.getTrainingName() + " - PT: " + bean.getPtTraining());
            System.out.println("Atleta: " + bean.getAthCompleteName());
            System.out.println("Data e ora: " + bean.getDate() + ", " + bean.getStartTime());
            System.out.println("Extra selezionati: " + bean.getDescription());
            System.out.println("Costo totale: " + bean.getPrice() + "€");
        }
    }
}
