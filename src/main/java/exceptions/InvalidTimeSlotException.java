package exceptions;

import controllers.PopUpManager;
import start.Main;

public class InvalidTimeSlotException extends RuntimeException {
    private static final String MSG = "Lo slot selezionato risulta già occupato, prenotazione annullata!";

    public InvalidTimeSlotException() {
        super(MSG);
    }

    public void handleException() {
        if(Main.isCLI()) {
            System.out.println(MSG);
        } else
            PopUpManager.popUp(MSG);
    }
}
