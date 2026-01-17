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
                    PersonalTrainerMenuCLI ptMenu = new PersonalTrainerMenuCLI();
                    ptMenu.goToHome();
                } else {
                    AthleteMenuCLI  athMenu = new AthleteMenuCLI();
                    athMenu.goToHome();
                }
                break;
            case "3":
                if(type.equals(ATHLETE_TYPE)) {
                    PersonalTrainerMenuCLI ptMenu = new PersonalTrainerMenuCLI();
                    ptMenu.logout();
                } else {
                    AthleteMenuCLI  athMenu = new AthleteMenuCLI();
                    athMenu.logout();
                }
                break;
            default:
                System.out.println(INVALIDINPUT);
                break;
        }
    }

    private void printUserData() {
        String typeName = user.getType().equals("ATH") ? "Atleta" : "Personal Trainer";
        System.out.println(" Nome:            " + user.getFirstName());
        System.out.println(" Cognome:         " + user.getLastName());
        System.out.println(" Username:        " + user.getUsername());
        System.out.println(" Ruolo:           " + typeName);
        System.out.println(SEPARATOR);
    }

    private void printUserStats() {
        ProfileController pController = new  ProfileController();
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
}
