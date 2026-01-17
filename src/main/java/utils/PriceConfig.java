package utils;

import exceptions.DataLoadException;

import java.math.BigDecimal;
import java.util.Properties;

public class PriceConfig {
    private static final Properties prices;
    private static final BigDecimal DEFAULT_EXTRA_PRICE = new BigDecimal("5.0");

    private PriceConfig() throws IllegalStateException {
        throw new IllegalStateException("Utility class");
    }

    static {
        try {
            prices = ResourceLoader.loadProperties("/config/prices.properties");
        } catch (Exception e) {
            throw new DataLoadException("Impossibile caricare il file di configurazione dei prezzi", e);
        }
    }

    public static String getName(String finalKey) {
        String name = prices.getProperty(finalKey);
        if (name == null) {
            throw new DataLoadException("Dato non trovato nel file.");
        }
        return name;
    }

    public static BigDecimal getPrice(String finalKey, BigDecimal defaultPrice) {
        if (finalKey == null) return defaultPrice;

        String value   = prices.getProperty(finalKey);
        if (value == null) return defaultPrice;

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException _) {
            return defaultPrice;
        }
    }

    public static String getExtraName(String extraName) {
        String key = "name." + extraName.toLowerCase();
        return getName(key);
    }

    public static BigDecimal getExtraPrice(String extraName) {
        if (extraName == null)
            return new BigDecimal("0.0");

        String key = "price." + extraName.toLowerCase();
        return getPrice (key, DEFAULT_EXTRA_PRICE);
    }
}
