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
        System.out.println("                  SIGNUP PAGE");
        System.out.println(SEPARATOR);

        System.out.print("Enter your First Name: ");
        String firstName = sc.nextLine();

        System.out.print("Enter your Last Name: ");
        String lastName = sc.nextLine();

        System.out.print("Enter your username: ");
        String username = sc.nextLine();

        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        SignupBean signupBean = new SignupBean();
        signupBean.setFirstName(firstName);
        signupBean.setLastName(lastName);
        signupBean.setUsername(username);
        signupBean.setPassword(password);

        AuthController authController = new AuthController();

        try {
            if (authController.registerUser(signupBean)) {
                System.out.println("Signup successful! Welcome " + username + "!");
                new HomepageCLI().start();
            }
        } catch (ExistingUserException e) {
            e.handleException();
        }
    }
}
