package controllers;

import javafx.scene.control.Alert;

public class PopUpManager {
    private PopUpManager() {
    }

    public static void popUp(String msg) {
        Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
