package exceptions;

import controllers.PopUpManager;
import start.Main;

public class TimeSlotOccupiedException extends RuntimeException {
    private static final String MSG = "The selected slot is busy!";

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
