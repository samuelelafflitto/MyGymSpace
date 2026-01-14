package exceptions;

public enum DateErrorType {
    PAST_DATE("Impossibile selezionare una data passata!"),
    HOLIDAY("Impossibile selezionare la data in quanto giorno festivo."),
    FULL_SCHEDULE("Impossibile prenotare questa data in quanto tutti gli slot sono occupati.");

    private final String msg;

    DateErrorType(String msg) {
        this.msg = msg;
    }

    public String getMSG() {
        return msg;
    }
}
