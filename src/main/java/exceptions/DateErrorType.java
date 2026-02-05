package exceptions;

public enum DateErrorType {
    PAST_DATE("Unable to select a past date!"),
    HOLIDAY("Unable to select date as it is a holiday!"),
    FULL_SCHEDULE("Unable to select this date as all slots are busy!");

    private final String msg;

    DateErrorType(String msg) {
        this.msg = msg;
    }

    public String getMSG() {
        return msg;
    }
}
