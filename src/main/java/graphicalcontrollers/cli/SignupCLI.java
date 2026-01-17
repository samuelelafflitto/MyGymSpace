package graphicalcontrollers.cli;

import beans.SignupBean;
import controllers.AuthController;
import exceptions.ExistingUserException;

import java.util.Scanner;

public class SignupCLI {
    private static final String SEPARATOR = "------------------------------------------------";

    private static final Scanner sc = new Scanner(System.in);

    public void start() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("REGISTRATI");
        System.out.println(SEPARATOR);

        System.out.print("Inserisci il tuo Nome: ");
        String firstName = sc.nextLine();

        System.out.print("Inserisci il tuo Cognome: ");
        String lastName = sc.nextLine();

        System.out.print("Inserisci il tuo username: ");
        String username = sc.nextLine();

        System.out.print("Inserisci la tua password: ");
        String password = sc.nextLine();

        SignupBean signupBean = new SignupBean();
        signupBean.setFirstName(firstName);
        signupBean.setLastName(lastName);
        signupBean.setUsername(username);
        signupBean.setPassword(password);

        AuthController authController = new AuthController();

        try {
            if (authController.registerUser(signupBean)) {
                System.out.println("Registrazione riuscita! Benvenuto, " + username + "!");
                new HomepageCLI().start();
            }
        } catch (ExistingUserException e) {
            e.handleException();
        }
    }
}
