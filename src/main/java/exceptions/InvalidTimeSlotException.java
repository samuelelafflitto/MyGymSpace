package exceptions;

import controllers.PopUpManager;
import start.Main;

public class InvalidTimeSlotException extends RuntimeException {
    private static final String MSG = "The selected slot is busy, booking cancelled!";

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
