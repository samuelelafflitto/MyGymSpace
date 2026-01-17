package utils;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Properties;

public class ScheduleConfig {
    private static final Properties props;

    private ScheduleConfig() {
        throw new IllegalStateException("Utility class");
    }

    static {
        props = ResourceLoader.loadProperties("/config/schedule.properties");
    }

    public static LocalTime getTime(String key, String defTime) {
        String value = props.getProperty(key);

        if (value == null) value = defTime;

        try {
            return LocalTime.parse(value);
        } catch (DateTimeParseException e) {
            System.err.println("[ERROR] Formato orario errato per " + key + ": " + value + ". Uso orario di default: " + defTime);
            System.err.println("Causa: " + e.getMessage());
            return LocalTime.parse(defTime);
        }
    }
}
