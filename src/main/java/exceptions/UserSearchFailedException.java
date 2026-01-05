package exceptions;

import controllers.PopUpManager;
import start.Main;

public class UserSearchFailedException extends RuntimeException {
    private static final String MSG = "Credenziali incorrette. Riprovare";

    public UserSearchFailedException() {
        super(MSG);
    }

    public void handleException() {
        if(Main.isCLI()) {
            System.out.println(MSG);
        } else
            PopUpManager.popUp(MSG);
    }
}
