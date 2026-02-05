package exceptions;

import controllers.PopUpManager;
import start.Main;

public class InvalidPasswordConfirmationException extends RuntimeException {
    private static final String MSG = "Incorrect password confirmation!";

    public InvalidPasswordConfirmationException() {
        super(MSG);
    }

    public void handleException() {
        if(Main.isCLI()) {
            System.out.println(MSG);
        } else
            PopUpManager.popUp(MSG);
    }
}
