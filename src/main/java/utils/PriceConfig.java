package utils;

import java.util.Properties;

public class PriceConfig {
    private static Properties prices;

    static {
        prices = ResourceLoader.loadProperties("/config/prices.properties");
    }

    public static double getExtraPrice(String extraName) {
        if (extraName == null)
            return 0.0;

        String key = "price." + extraName.toLowerCase();
        String val = prices.getProperty(key);
        if (val == null) {
            System.err.println("[WARNING] Prezzo non trovato per: " + extraName);
            return 5.0;     //Valore minimo degli extra
        }

        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            System.err.println("[WARNING] Il prezzo " + key + " non è un numero valido.");
            return 5.0;
        }
    }
}
