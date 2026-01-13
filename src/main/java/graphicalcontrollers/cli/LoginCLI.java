package graphicalcontrollers.cli;

import beans.LoginBean;
import controllers.AuthController;
import exceptions.UserSearchFailedException;
import utils.session.SessionManager;

import java.util.Scanner;

public class LoginCLI {
    private static final String INVALIDINPUT = "Opzione selezionata non valida. Riprovare";

    GuestMenuCLI guestMenuCLI =  new GuestMenuCLI();

    private Scanner sc = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("------------------------");
            System.out.println("LOG IN PAGE");
            System.out.println("------------------------");
            System.out.println("1) Log In");
            System.out.println("2) Sign Up");
            System.out.println("3) Torna alla Homepage");
            System.out.println("-> ");
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
        System.out.println("----------------------------");

        System.out.println("Inserisci il tuo username: ");
        String username = sc.nextLine();

        System.out.println("Inserisci la tua password: ");
        String password = sc.nextLine();

        LoginBean loginBean = new LoginBean();
        loginBean.setUsername(username);
        loginBean.setPassword(password);

        AuthController authController = new AuthController();

        try {
            authController.authUser(loginBean);
            System.out.println("Login riuscito! Benvenuto, " + username);
            new HomepageCLI().start();
        } catch (UserSearchFailedException e) {
            e.handleException();
            this.login();
        }
    }

    public void goToSignup() {
        new SignupCLI().start();
    }
}
