package beans;

import controllers.BookingController;
import exceptions.DateErrorType;
import exceptions.DateException;
import exceptions.InvalidDateFormatException;
import utils.HolidayChecker;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SelectedDateBean {
    BookingController bController = new BookingController();
    HolidayChecker hc = new HolidayChecker();

    private LocalDate selectedDate;

    public SelectedDateBean(String date) {
        this.selectedDate = stringToDate(date);
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }





    // CONVERSIONE STRING TO LOCALDATE E CONTROLLO SULLA DATA
    private LocalDate stringToDate(String input) throws DateException {
        if(input.isEmpty()) {
            throw new InvalidDateFormatException();
        }

        try {
            LocalDate selectedDate = LocalDate.parse(input);
            if(bController.isPastDate(selectedDate)) {
                throw new DateException(DateErrorType.PAST_DATE);
            }
            if(bController.isHoliday(selectedDate)) {
                throw new DateException(DateErrorType.HOLIDAY);
            }
            return selectedDate;
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }
    }
}
