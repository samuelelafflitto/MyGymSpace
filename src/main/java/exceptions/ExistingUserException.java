package exceptions;

import controllers.PopUpManager;
import start.Main;

public class ExistingUserException extends RuntimeException {
    private static final String MSG = "Username già utilizzato. Scegliere un altro username oppure effettuare il Login.";


    public ExistingUserException() {
        super(MSG);
    }

    public void handleException() {
        if(Main.isCLI()) {
            System.out.println(MSG);
        } else {
            PopUpManager.popUp(MSG);
        }
    }
}
