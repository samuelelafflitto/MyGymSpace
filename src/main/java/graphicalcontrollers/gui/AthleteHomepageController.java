package graphicalcontrollers.gui;

import javafx.fxml.FXML;

public class AthleteHomepageController {
    @FXML
    void onBookSessionClick() {
        ViewManager.changePage("/views/TrainingSelection.fxml");
    }

    @FXML
    void onUpcomingEventsClick() {
        System.out.println("Vai a Upcoming Events");
    }

    @FXML
    void onMyBookingsClick() {
        ViewManager.changePage("/views/MyBookings.fxml");
    }
}
