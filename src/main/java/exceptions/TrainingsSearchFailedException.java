package exceptions;

import controllers.PopUpManager;
import start.Main;

public class TrainingsSearchFailedException extends RuntimeException {

    private static final String MSG = "No training available!";

    public TrainingsSearchFailedException() {
        super(MSG);
    }

    public void handleException() {
        if(Main.isCLI()) {
            System.out.println(MSG);
        } else
            PopUpManager.popUp(MSG);
    }
}
