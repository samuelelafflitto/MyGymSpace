package exceptions;

import controllers.PopUpManager;
import start.Main;

public class InvalidSelectionException extends RuntimeException {
    private static final String MSG = "Selezione non valida. Riprovare";

    public InvalidSelectionException() {
        super(MSG);
    }

    public void handleException() {
        if(Main.isCLI()) {
            System.out.println(MSG);
        } else
            PopUpManager.popUp(MSG);
    }
}
