package utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Set;

public class HolidayChecker {
    private static final DateTimeFormatter FORMATTER_ANNUAL = DateTimeFormatter.ofPattern("dd/MM");
    private static final DateTimeFormatter FORMATTER_SPECIFIC = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public boolean isClosedForHoliday(LocalDate date) {
        if(date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return true;
        }

        Properties prop = ResourceLoader.loadProperties("/config/closingDays.properties");
        Set<String> dates = prop.stringPropertyNames();

        for(String dateString : dates) {
            if(dateString.length() == 5) {
                MonthDay holiday = MonthDay.parse(dateString, FORMATTER_ANNUAL);
                if(MonthDay.from(date).equals(holiday)) {
                    return true;
                }
            } else if (dateString.length() == 10) {
                LocalDate specificDay = LocalDate.parse(dateString, FORMATTER_SPECIFIC);
                if(date.equals(specificDay)) {
                    return true;
                }
            }
        }
        return false;
    }
}
