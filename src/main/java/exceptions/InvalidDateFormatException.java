package exceptions;

import controllers.PopUpManager;
import start.Main;

public class InvalidDateFormatException extends RuntimeException {
    private static final String MSG = "Invalid date format!";

    public InvalidDateFormatException() {
        super(MSG);
    }

    public void handleException() {
        if(Main.isCLI()) {
            System.out.println(MSG);
        } else
            PopUpManager.popUp(MSG);
    }
}
