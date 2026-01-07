package graphicalcontrollers.cli;

import models.user.User;
import utils.session.SessionManager;

import java.util.Scanner;

public class HomepageCLI {
    private Scanner sc = new Scanner(System.in);
    private static final String INVALIDINPUT = "Opzione selezionata non valida. Riprovare";
    private static final String PT_TYPE = "PT";
    private static final String ATHLETE_TYPE = "ATH";

    public void start() {
        System.out.println("------------------------");
        System.out.println("BENVENUTO IN MYGYMSPACE!");
        System.out.println("------------------------");
        loadMenu();
    }

    private void loadMenu() {

        User currentUser = SessionManager.getInstance().getLoggedUser();

        if (currentUser == null) {
            /*System.out.print("Nessun utente loggato: caricamento Menu Guest...");*/
            GuestMenuCLI guestMenuCLI = new GuestMenuCLI();
            while (true) {
                guestMenuCLI.showMenu();
                System.out.println("-> ");
                String choice = sc.nextLine();
                handleGuestChoice(choice, guestMenuCLI);

                /*if(SessionManager.getInstance().getLoggedUser() != null) {
                    loadMenu();
                    return;
                }*/
            }
        }

        String loggedUserType = (SessionManager.getInstance().getLoggedUser().getType() != null)
                ? SessionManager.getInstance().getLoggedUser().getType()
                : "None";

        if(loggedUserType.equals(PT_TYPE)) {
            PersonalTrainerMenuCLI ptMenuCLI = new PersonalTrainerMenuCLI();
            while(true) {
                ptMenuCLI.showMenu();
                System.out.println("-> ");
                String choice = sc.nextLine();
                handlePTChoice(choice, ptMenuCLI);
            }
        } else if (loggedUserType.equals(ATHLETE_TYPE)) {
            AthleteMenuCLI athleteMenuCLI = new AthleteMenuCLI();
            while (true) {
                athleteMenuCLI.showMenu();
                System.out.println("-> ");
                String choice = sc.nextLine();
                handleAthleteChoice(choice, athleteMenuCLI);
            }
        }/*else {
            GuestMenuCLI guestMenuCLI = new GuestMenuCLI();
            while (true) {
                guestMenuCLI.showMenu();
                System.out.println("-> ");
                String choice = sc.nextLine();
                handleGuestChoice(choice, guestMenuCLI);
            }
        }*/
    }

    private void handlePTChoice(String choice, PersonalTrainerMenuCLI menuCLI) {
        switch (choice) {
            case "1":
                menuCLI.goToHome();
                break;
            case "2":
                menuCLI.goToAddEvent();
                break;
            case "3":
                menuCLI.goToManageEvents();
                break;
            case "4":
                menuCLI.goToAthleteBookings();
                break;
            case "5":
                menuCLI.logout();
                break;
            default:
                System.out.println(INVALIDINPUT);
                break;
        }
    }

    private void handleAthleteChoice(String choice, AthleteMenuCLI menuCLI) {
        switch (choice) {
            case "1":
                menuCLI.goToHome();
                break;
            case "2":
                menuCLI.goToBook();
                break;
            case "3":
                menuCLI.goToEvents();
                break;
            case "4":
                menuCLI.goToMyBookings();
                break;
            case "5":
                menuCLI.logout();
                break;
            default:
                System.out.println(INVALIDINPUT);
                break;
        }
    }

    private void handleGuestChoice(String choice, GuestMenuCLI menuCLI) {
        switch (choice) {
            case "1":
                menuCLI.goToHome();
                break;
            case "2":
                menuCLI.goToEvents();
                break;
            case "3":
                menuCLI.login();
                break;
            default:
                System.out.println(INVALIDINPUT);
                break;
        }
    }
}
