package graphicalcontrollers.gui;

import exceptions.DataLoadException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ViewManager {
    private static Stage initialStage;
    private static String currentTheme;

    private ViewManager() {//Il costruttore non ha bisogno di parametri

    }

    public static void setInitialStage(Stage s) {
        initialStage = s;
    }

    public static void setTheme(String theme) {
        currentTheme = theme;
    }

    public static void changePage(String fxmlPath) {
        try {
            URL resource = ViewManager.class.getResource(fxmlPath);
            if(resource == null) {
                throw new IllegalArgumentException("Resource not found: " + fxmlPath);
            }

            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);

            if(currentTheme != null) {
                String cssPath = "/css/" + currentTheme + ".css";
                URL cssResource = ViewManager.class.getResource(cssPath);
                if(cssResource != null) {
                    scene.getStylesheets().add(cssResource.toExternalForm());
                }
            }

            initialStage.setScene(scene);
            initialStage.setResizable(false);
            initialStage.setTitle("MyGymSpace");
            initialStage.show();
        } catch (IOException _) {
            throw new DataLoadException("Impossibile caricare la scena " + fxmlPath);
        }
    }
}
