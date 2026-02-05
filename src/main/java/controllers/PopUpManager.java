package controllers;

import javafx.scene.control.Alert;

public class PopUpManager {
    private PopUpManager() {// The constructor does not need parameters
    }

    public static void popUp(String msg) {
        Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
