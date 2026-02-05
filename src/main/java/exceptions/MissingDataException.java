package exceptions;

import controllers.PopUpManager;
import start.Main;

public class MissingDataException extends RuntimeException {
    private static final String MSG = "Missing data! Try again";

    public MissingDataException() {
        super(MSG);
    }

    public void handleException() {
        if(Main.isCLI()) {
            System.out.println(MSG);
        } else
            PopUpManager.popUp(MSG);
    }
}
