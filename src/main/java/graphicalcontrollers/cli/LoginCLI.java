package graphicalcontrollers.cli;

import beans.LoginBean;
import controllers.AuthController;
import exceptions.UserSearchFailedException;

import java.util.Scanner;

public class LoginCLI {
    private static final Scanner sc = new Scanner(System.in);
    private static final String SEPARATOR = "------------------------------------------------";
    private static final String INVALIDINPUT = "Invalid Option! Try again";

    GuestMenuCLI guestMenuCLI =  new GuestMenuCLI();

    public void start() {
        while (true) {
            System.out.println("\n" + SEPARATOR);
            System.out.println("                   LOGIN PAGE");
            System.out.println(SEPARATOR);
            System.out.println("1) Log In");
            System.out.println("2) Sign Up");
            System.out.println("3) Back to Homepage");
            System.out.print("--> ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    login();
                    break;
                case "2":
                    goToSignup();
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

    private void login() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("             ENTER YOUR CREDENTIALS");
        System.out.println(SEPARATOR);

        System.out.print("Enter your username: ");
        String username = sc.nextLine();

        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        LoginBean loginBean = new LoginBean();
        loginBean.setUsername(username);
        loginBean.setPassword(password);

        AuthController authController = new AuthController();

        try {
            if(authController.authUser(loginBean)) {
                System.out.println("\nLogin successful! Welcome " + username);
                new HomepageCLI().start();
            }
        } catch (UserSearchFailedException e) {
            e.handleException();
        }
    }

    public void goToSignup() {
        new SignupCLI().start();
    }
}
