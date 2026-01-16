package utils;

import exceptions.DataLoadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceLoader {

    private ResourceLoader() throws IllegalStateException {
        throw new IllegalStateException("Utility class");
    }

    public static Properties loadProperties(String resourcePath) {
        Properties prop = new Properties();

        try (InputStream in = ResourceLoader.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new DataLoadException("Impossibile trovate il file: " + resourcePath);
            }
            prop.load(in);
            return prop;
        } catch (IOException e) {
            throw new DataLoadException("Errore durante la lettura del file: " + resourcePath, e);
        }
    }
}
