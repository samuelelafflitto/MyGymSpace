package exceptions;

import controllers.PopUpManager;
import start.Main;

public class TimeSlotOccupiedException extends RuntimeException {
    private static final String MSG = "Lo Slot selezionato risulta occupato!";

    public TimeSlotOccupiedException() {
        super(MSG);
    }

    public void handleException() {
        if(Main.isCLI()) {
            System.out.println(MSG);
        } else
            PopUpManager.popUp(MSG);
    }
}
