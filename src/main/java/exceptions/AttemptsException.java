package exceptions;

import controllers.PopUpManager;
import start.Main;

public class AttemptsException extends RuntimeException {
    private final AttemptsErrorType errorType;
    private final String msg;

    public AttemptsException(AttemptsErrorType errorType) {
        super(errorType.getMSG());
        this.errorType = errorType;
        this.msg = errorType.getMSG();
    }

    public AttemptsException(AttemptsErrorType errorType, Object... args) {
        super(errorType.getMSG(args));
        this.errorType = errorType;
        this.msg = errorType.getMSG(args);
    }

    public AttemptsErrorType getErrorType() {
        return errorType;
    }

    public void handleException() {
        if(Main.isCLI()) {
            System.out.println(msg);
        } else
            PopUpManager.popUp(msg);
    }
}
