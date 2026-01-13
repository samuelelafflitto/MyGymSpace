package exceptions;

public enum AttemptsErrorType {
    MAX_ATTEMPTS_REACHED("Tentativi terminati!\nPrenotazione annullata - ritorno alla Homepage."),
    REMAINING("Tentativi rimasti: %d");

    private final String MSG;

    AttemptsErrorType(String msg) {
        this.MSG = msg;
    }

    public String getMSG(Object... args) {
        if(args.length > 0) {
            return String.format(MSG, args);
        }
        return MSG;
    }
}