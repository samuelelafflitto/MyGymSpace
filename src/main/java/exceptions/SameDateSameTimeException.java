package exceptions;

import controllers.PopUpManager;
import start.Main;

public class SameDateSameTimeException extends RuntimeException {
    private static final String MSG = "Hai già una prenotazione per quella Data e Ora. Riprovare.";

    public SameDateSameTimeException() {
        super(MSG);
    }

    public void handleException() {
        if(Main.isCLI()) {
            System.out.println(MSG);
        } else
            PopUpManager.popUp(MSG);
    }
}
