package beans;

import exceptions.DateException;
import exceptions.InvalidDateFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SelectedDateBean {
//    BookingController bController = new BookingController();
//    HolidayChecker hc = new HolidayChecker();

    private LocalDate selectedDate;

    public SelectedDateBean(String date) {
        stringToDate(date);
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

//    public void setSelectedDate(LocalDate selectedDate) {
//        this.selectedDate = selectedDate;
//    }





    // CONVERSIONE STRING TO LOCALDATE E CONTROLLO SULLA DATA
    private void stringToDate(String input) throws DateException {
        if(input.isEmpty()) {
            throw new InvalidDateFormatException();
        }

        try {
            this.selectedDate = LocalDate.parse(input);
//            if(bController.isPastDate(selectedDate)) {
//                throw new DateException(DateErrorType.PAST_DATE);
//            }
//            if(bController.isHoliday(selectedDate)) {
//                throw new DateException(DateErrorType.HOLIDAY);
//            }
        } catch (DateTimeParseException _) {
            throw new InvalidDateFormatException();
        }
    }
}
