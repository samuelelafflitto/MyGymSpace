package graphicalcontrollers.cli;

import beans.ProfileStatsBean;
import controllers.ProfileController;
import models.user.User;
import utils.session.SessionManager;

import java.util.Scanner;

public class MyProfilePageCLI {
    private static final Scanner sc = new Scanner(System.in);
    private static final String INVALIDINPUT = "Opzione selezionata non valida. Riprovare";
    private static final String SEPARATOR = "------------------------------------------------";
    private static final String ATHLETE_TYPE = "ATH";
    ProfileController pController = new ProfileController();
    AthleteMenuCLI athleteMenuCLI = new AthleteMenuCLI();
    PersonalTrainerMenuCLI personalTrainerMenuCLI = new PersonalTrainerMenuCLI();
    User user = SessionManager.getInstance().getLoggedUser();
    String type = user.getType();

    public void start() {
        while(true) {
            System.out.println("\n" + SEPARATOR);
            System.out.println("            MYGYMSPACE - PROFILO");
            System.out.println(SEPARATOR);

            printUserData();
            printUserStats();
            printOptions();

            System.out.print("--> ");

            String choice = sc.nextLine();

            handleChoice(choice);
        }
    }

    private void handleChoice(String choice) {
        switch (choice) {
            case "1":
                break;
            case "2":
                if(type.equals(ATHLETE_TYPE)) {
                    personalTrainerMenuCLI.goToHome();
                } else {
                    athleteMenuCLI.goToHome();
                }
                break;
            case "3":
                if(type.equals(ATHLETE_TYPE)) {
                    personalTrainerMenuCLI.logout();
                } else {
                    athleteMenuCLI.logout();
                }
                break;
            default:
                System.out.println(INVALIDINPUT);
                break;
        }
    }

    private void printUserData() {
        String type = user.getType().equals("ATH") ? "Atleta" : "Personal Trainer";
        System.out.println(" Nome:            " + user.getFirstName());
        System.out.println(" Cognome:         " + user.getLastName());
        System.out.println(" Username:        " + user.getUsername());
        System.out.println(" Ruolo:           " + type);
        System.out.println(SEPARATOR);
    }

    private void printUserStats() {
        ProfileStatsBean bean = pController.getProfileStats();
        String nextSession;
        if(bean.getNextDate() == null && bean.getNextTime() == null) {
            nextSession = "Nessuna prenotazione attiva";
        } else {
            nextSession = bean.getNextDate().toString() + ", " + bean.getNextTime().toString();
        }

        System.out.println(" Sessioni totali:      " + bean.getStats1());
        System.out.println(" Sessioni future:      " + bean.getStats2());
        System.out.println(" Prossima sessione:    " + nextSession);
    }

    private void printOptions() {
        System.out.println(SEPARATOR);
        System.out.println(" 1. Modifica Dati Personali");
        System.out.println(" 2. Torna al Menu Principale");
        System.out.println(" 3. Logout");
        System.out.println(SEPARATOR);
    }

//    public MyProfileCLI() {
//        // Recuperiamo l'utente dalla sessione globale
//        this.currentUser = SessionManager.getInstance().getCurrentUser();
//    }
//
//    public void start() {
//        boolean exit = false;
//
//        while (!exit) {
//            printHeader();
//            printUserData();
//
//            int choice = ScannerUtil.readInt("Scelga un'opzione: ", 1, 4);
//            exit = handleChoice(choice);
//        }
//    }
//
//    private void printHeader() {
//        System.out.println("\n==================================================");
//        System.out.println("                MYGYMSPACE - PROFILO");
//        System.out.println("==================================================");
//    }
//
//    private void printUserData() {
//        // Utilizziamo i getter dell'oggetto User caricato dal DB [cite: 2025-12-14]
//        System.out.printf(" Nome:          %s\n", currentUser.getFirstName());
//        System.out.printf(" Cognome:       %s\n", currentUser.getLastName());
//        System.out.printf(" Username:      %s\n", currentUser.getUsername());
//        System.out.printf(" Ruolo:         %s\n", currentUser.getType().equals("ATH") ? "Atleta" : "Personal Trainer");
//        System.out.println("--------------------------------------------------");
//
//        // Qui potremmo aggiungere una chiamata rapida al DAO per le statistiche
//        printQuickStats();
//
//        System.out.println("--------------------------------------------------");
//        System.out.println(" 1. Modifica Password");
//        System.out.println(" 2. Visualizza Storico");
//        System.out.println(" 3. Torna al Menu Principale");
//        System.out.println(" 4. Logout");
//        System.out.println("==================================================");
//    }
//
//    private boolean handleChoice(int choice) {
//        switch (choice) {
//            case 1 -> goToChangePassword();
//            case 2 -> goToHistory();
//            case 3 -> { return true; } // Torna al menu precedente
//            case 4 -> performLogout();
//        }
//        return false;
//    }
}
