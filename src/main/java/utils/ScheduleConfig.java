package utils;

import java.util.Properties;

public class ScheduleConfig {
    private static Properties props;

    private ScheduleConfig() {
        throw new IllegalStateException("Utility class");
    }

    static {
        props = ResourceLoader.loadProperties("/config/schedule.properties");
    }

    public static int getInt(String key, int defValue) {
        String value = props.getProperty(key);

        if (value == null) return defValue;

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("[ERROR] Orario non valido per " + key + ": " + value + ". Uso orario di default: " + defValue);
            System.err.println("Causa: " + e.getMessage());
            return defValue;
        }
    }
}
