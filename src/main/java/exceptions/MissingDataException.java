package exceptions;

import controllers.PopUpManager;
import start.Main;

public class MissingDataException extends RuntimeException {
    private static final String MSG = "Dati mancanti. Riprovare";

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
