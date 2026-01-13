package exceptions;

import controllers.PopUpManager;
import start.Main;

public class DateException extends RuntimeException {
    private final DateErrorType errorType;

    public DateException(DateErrorType errorType) {
        super(errorType.getMSG());
        this.errorType = errorType;
    }

//    public String getErrorMSG() {
//        return errorType.getMSG();
//    }
//
//    public DateErrorType getErrorType() {
//        return errorType;
//    }

    public void handleException() {
        if(Main.isCLI()) {
            System.out.println(errorType.getMSG());
        } else
            PopUpManager.popUp(errorType.getMSG());
    }
}
