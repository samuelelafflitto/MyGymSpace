package graphicalcontrollers.gui;

import javafx.fxml.FXML;

public class AthleteHomepageController {
    @FXML
    void onBookSessionClick() {
        ViewManager.changePage("/views/TrainingSelection.fxml");
    }

    @FXML
    void onUpcomingEventsClick() {
        System.out.println("UPCOMING EVENTS - COMING SOON");
    }

    @FXML
    void onMyBookingsClick() {
        ViewManager.changePage("/views/MyBookings.fxml");
    }
}
