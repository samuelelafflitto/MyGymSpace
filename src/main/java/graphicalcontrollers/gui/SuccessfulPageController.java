package graphicalcontrollers.gui;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.util.Duration;

public class SuccessfulPageController {
    @FXML
    public void initialize() {
        PauseTransition delay = new PauseTransition(Duration.seconds(2));

        delay.setOnFinished(_ -> ViewManager.changePage("/views/MyProfile.fxml"));

        delay.play();
    }
}
