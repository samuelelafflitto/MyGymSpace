package exceptions;

public enum AttemptsErrorType {
    MAX_ATTEMPTS_REACHED("Tentativi terminati!\nPrenotazione annullata - ritorno alla Homepage."),
    REMAINING("Tentativi rimasti: %d");

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