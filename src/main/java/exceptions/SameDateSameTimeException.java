package exceptions;

import controllers.PopUpManager;
import start.Main;

public class SameDateSameTimeException extends RuntimeException {
    private static final String MSG = "You already have a booking for the selected date and time. Try again";

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
