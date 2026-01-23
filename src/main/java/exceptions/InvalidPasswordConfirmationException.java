package exceptions;

import controllers.PopUpManager;
import start.Main;

public class InvalidPasswordConfirmationException extends RuntimeException {
    private static final String MSG = "Password di conferma errata!";

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
