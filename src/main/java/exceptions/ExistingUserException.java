package exceptions;

import controllers.PopUpManager;
import start.Main;

public class ExistingUserException extends RuntimeException {
    private static final String MSG = "Username già utilizzato. Sceglierne un altro e riprovare";


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
