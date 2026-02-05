package exceptions;

public enum AttemptsErrorType {
    MAX_ATTEMPTS_REACHED("Attempts over!\nBooking cancelled - return to Homepage."),
    REMAINING("Attempts left: %d\n");

    private final String msg;

    AttemptsErrorType(String msg) {
        this.msg = msg;
    }

    public String getMSG(Object... args) {
        if(args.length > 0) {
            return String.format(msg, args);
        }
        return msg;
    }
}