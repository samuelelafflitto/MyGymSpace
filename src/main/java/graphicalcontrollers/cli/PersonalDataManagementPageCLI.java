package graphicalcontrollers.cli;

import beans.ProfileDataBean;
import controllers.ProfileController;
import exceptions.InvalidPasswordConfirmationException;
import models.user.User;
import utils.session.SessionManager;

import java.util.Scanner;

public class PersonalDataManagementPageCLI {
    private static final Scanner sc = new Scanner(System.in);
    private static final String INVALIDINPUT = "Opzione selezionata non valida. Riprovare";
    private static final String SEPARATOR = "------------------------------------------------";
    private static final String ATHLETE_TYPE = "ATH";

    ProfileController pController = new ProfileController();
    User user = SessionManager.getInstance().getLoggedUser();
    String type = user.getType();

    public void start() {
        while(true) {
            System.out.println("\n" + SEPARATOR);
            System.out.println("      MYGYMSPACE - GESTIONE DATI PERSONALI");
            System.out.println(SEPARATOR);

            System.out.println("1) Modifica Password");
            System.out.println("2) Modifica Nome e Cognome");
            System.out.println("3) Torna alla Homepage");
            System.out.println("4) Logout ");
            System.out.print("--> ");
            String choice = sc.nextLine();

            handleChoice(choice);
        }
    }

    private void handleChoice(String choice) {
        switch (choice) {
            case "1":
                changePassword();
                new MyProfilePageCLI().start();
                break;
            case "2":
                changeName();
                new MyProfilePageCLI().start();
                break;
            case "3":
                if(type.equals(ATHLETE_TYPE)) {
                    PersonalTrainerMenuCLI ptMenu = new PersonalTrainerMenuCLI();
                    ptMenu.goToHome();
                } else {
                    AthleteMenuCLI  athMenu = new AthleteMenuCLI();
                    athMenu.goToHome();
                }
                break;
            case "4":
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

    private void changePassword() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("            MODIFICA LA TUA PASSWORD");
        System.out.println(SEPARATOR);

        System.out.print("Inserisci la nuova password: ");
        String newPassword = sc.nextLine();

        System.out.print("Per confermare, inserisci la password attuale: ");
        String currentPassword = sc.nextLine();

        ProfileDataBean profileDataBean = new ProfileDataBean();
        profileDataBean.setNewPassword(newPassword);
        profileDataBean.setCurrentPassword(currentPassword);

        try {
            pController.changePassword(profileDataBean);
        } catch (InvalidPasswordConfirmationException e) {
            e.handleException();
        }
    }

    private void changeName() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("            MODIFICA NOME E COGNOME");
        System.out.println(SEPARATOR);

        System.out.print("Inserisci il nuovo Nome: ");
        String newFirstName = sc.nextLine();

        System.out.print("Inserisci il nuovo Cognome: ");
        String newLastName = sc.nextLine();

        System.out.print("Per confermare, inserisci la password attuale: ");
        String currentPassword = sc.nextLine();

        ProfileDataBean profileDataBean = new ProfileDataBean();
        profileDataBean.setNewFirstName(newFirstName);
        profileDataBean.setNewLastName(newLastName);
        profileDataBean.setCurrentPassword(currentPassword);

        try {
            pController.changeName(profileDataBean);
        } catch (InvalidPasswordConfirmationException e) {
            e.handleException();
        }
    }
}
