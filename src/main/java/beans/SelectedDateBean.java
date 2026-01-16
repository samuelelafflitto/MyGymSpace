package beans;

import exceptions.DateException;
import exceptions.InvalidDateFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SelectedDateBean {

    private LocalDate selectedDate;

    public SelectedDateBean(String date) {
        stringToDate(date);
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    // CONVERSIONE STRING TO LOCALDATE E CONTROLLO SULLA DATA
    private void stringToDate(String input) throws DateException {
        if(input.isEmpty()) {
            throw new InvalidDateFormatException();
        }

        try {
            this.selectedDate = LocalDate.parse(input);
        } catch (DateTimeParseException _) {
            throw new InvalidDateFormatException();
        }
    }
}
