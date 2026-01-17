package graphicalcontrollers.cli;

import utils.session.SessionManager;

import java.util.Scanner;

public class HomepageCLI {
    private static final Scanner sc = new Scanner(System.in);
    private static final String SEPARATOR = "------------------------------------------------";
    private static final String INVALIDINPUT = "Opzione selezionata non valida. Riprovare";
    private static final String PT_TYPE = "PT";
    private static final String ATHLETE_TYPE = "ATH";

    public void start() {
        String loggedUserType = (SessionManager.getInstance().getLoggedUser() != null)
                ? SessionManager.getInstance().getLoggedUser().getType()
                : null;

        String msg = null;
        if (loggedUserType == null) {
            msg = "            Benvenuto in MyGymSpace!";
        } else if (loggedUserType.equals(ATHLETE_TYPE)) {
            msg = "               Bentornato Atleta!";
        } else if (loggedUserType.equals(PT_TYPE)) {
            msg = "          Bentornato Personal Trainer!";
        }

//        System.out.println("BENVENUTO IN MYGYMSPACE!");
        System.out.println(SEPARATOR);
        System.out.println("                    HOMEPAGE");
        System.out.println(msg);
        System.out.println(SEPARATOR);
        loadMenu();
    }

    public void loadMenu() {
        String loggedUserType = (SessionManager.getInstance().getLoggedUser() != null)
                ? SessionManager.getInstance().getLoggedUser().getType()
                : null;

        if(loggedUserType == null) {
            GuestMenuCLI guestMenu = new GuestMenuCLI();
            while(true) {
                guestMenu.showMenu();
                System.out.print("--> ");
                String choice = sc.nextLine();
                handleGuestChoice(choice, guestMenu);
            }
        } else if (loggedUserType.equals(PT_TYPE)) {
            PersonalTrainerMenuCLI personalTrainerMenu = new PersonalTrainerMenuCLI();
            while (true) {
                personalTrainerMenu.showMenu();
                System.out.print("--> ");
                String choice = sc.nextLine();
                handlePTChoice(choice, personalTrainerMenu);
            }
        } else if (loggedUserType.equals(ATHLETE_TYPE)) {
            AthleteMenuCLI athleteMenu = new AthleteMenuCLI();
            while (true) {
                athleteMenu.showMenu();
                System.out.print("--> ");
                String choice = sc.nextLine();
                handleAthleteChoice(choice, athleteMenu);
            }
        }
    }

    private void handleGuestChoice(String choice, GuestMenuCLI guestMenu) {
        switch (choice) {
            case "1":
                guestMenu.goToEvents();
                break;
            case "2":
                guestMenu.goToLogin();
                break;
            default:
                System.out.println("\n" + INVALIDINPUT);
                break;

        }
    }

    private void handlePTChoice(String choice, PersonalTrainerMenuCLI ptMenu) {
        switch (choice) {
            case "1":
                ptMenu.goToAddEvent();
                break;
            case "2":
                ptMenu.goToManageEvents();
                break;
            case "3":
                ptMenu.goToAthleteBookings();
                break;
            case "4":
                ptMenu.goToMyProfile();
                break;
            case "5":
                ptMenu.logout();
                break;
            default:
                System.out.println("\n" + INVALIDINPUT);
                break;
        }
    }

    private void handleAthleteChoice(String choice, AthleteMenuCLI athleteMenu) {
        switch (choice) {
            case "1":
                athleteMenu.goToBookASession();
                break;
            case "2":
                athleteMenu.goToEvents();
                break;
            case "3":
                athleteMenu.goToMyBookings();
                break;
            case "4":
                athleteMenu.goToMyProfile();
                break;
            case "5":
                athleteMenu.logout();
                break;
            default:
                System.out.println("\n" + INVALIDINPUT);
                break;
        }
    }
}
