package graphicalcontrollers.gui;

import javafx.fxml.FXML;

public class PTHomepageController {
    @FXML
    void onNewEventClick() {
        System.out.println("ADD NEW EVENT - COMING SOON");
    }

    @FXML
    void onManageEventsClick() {
        System.out.println("MANAGE EVENTS - COMING SOON");
    }

    @FXML
    void onAthleteBookingsClick() {
        ViewManager.changePage("/views/MyBookings.fxml");
    }
}
