package graphicalcontrollers.cli;

import beans.SignupBean;
import controllers.AuthController;
import exceptions.ExistingUserException;
import utils.session.SessionManager;

import java.util.Scanner;

public class SignupCLI {
    private static final String INVALIDINPUT = "Opzione selezionata non valida. Riprovare";

    GuestMenuCLI guestMenuCLI = new GuestMenuCLI();

    private Scanner sc = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("------------------------");
            System.out.println("SIGN UP PAGE");
            System.out.println("------------------------");
            System.out.println("1) Sign Up");
            System.out.println("2) Log In");
            System.out.println("3) Torna alla Homepage");
            System.out.println("-> ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    signup();
                    break;
                case "2":
                    goToLogin();
                    break;
                case "3":
                    guestMenuCLI.goToHome();
                    break;
                default:
                    System.out.println(INVALIDINPUT);
                    break;
            }
        }
    }

    public void signup() {
        System.out.println("----------------------------");

        System.out.println("Inserisci il tuo Nome: ");
        String firstName = sc.nextLine();

        System.out.println("Inserisci il tuo Cognome: ");
        String lastName = sc.nextLine();

        System.out.println("Inserisci il tuo username: ");
        String username = sc.nextLine();

        System.out.println("Inserisci la tua password: ");
        String password = sc.nextLine();

        SignupBean signupBean = new SignupBean();
        signupBean.setFirstName(firstName);
        signupBean.setLastName(lastName);
        signupBean.setUsername(username);
        signupBean.setPassword(password);

        AuthController authController = new AuthController();

        try {
            authController.registerUser(signupBean);
            System.out.println("Registrazione riuscita! Benvenuto, " + username + "!");
            new HomepageCLI().start();
        } catch(ExistingUserException e) {
            e.handleException();
            this.signup();
        }
    }

    public void goToLogin() {
        new LoginCLI().start();
    }
}
