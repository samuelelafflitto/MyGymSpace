package exceptions;

import controllers.PopUpManager;
import start.Main;

public class UserSearchFailedException extends RuntimeException {
    private static final String MSG = "Incorrect credentials! Try again";

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
