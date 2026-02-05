package exceptions;

import controllers.PopUpManager;
import start.Main;

public class FailedBookingCancellationException extends RuntimeException {
    private static final String MSG = "Error while deleting! Try again later";

    public FailedBookingCancellationException() {
        super(MSG);
    }

    public void handleException() {
        if(Main.isCLI()) {
            System.out.println(MSG);
        } else {
            PopUpManager.popUp(MSG);
        }
    }
}
