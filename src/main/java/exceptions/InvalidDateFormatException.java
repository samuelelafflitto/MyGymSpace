package exceptions;

import controllers.PopUpManager;
import start.Main;

public class InvalidDateFormatException extends RuntimeException {
    private static final String MSG = "Formato della data non valido!";

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
