package utils;

import exceptions.DataLoadException;

import java.util.Properties;

public class PriceConfig {
    private static Properties prices;
    private static final double DEFAULT_EXTRA_PRICE = 5.0;

    private PriceConfig() {
        throw new IllegalStateException("Utility class");
    }

    static {
        try {
            prices = ResourceLoader.loadProperties("/config/prices.properties");
        } catch (Exception e) {
            throw new DataLoadException("Impossibile caricare il file di configurazione dei prezzi", e);
        }
    }

    public static double getPrice(String finalKey, double defaultPrice) {
        if (finalKey == null) return defaultPrice;

        String value   = prices.getProperty(finalKey);
        if (value == null) return defaultPrice;

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultPrice;
        }
    }

    public static double getExtraPrice(String extraName) {
        if (extraName == null)
            return 0.0;

        String key = "price." + extraName.toLowerCase();
        return getPrice (key, DEFAULT_EXTRA_PRICE);
    }
}
