package graphicalcontrollers.gui;

import javafx.fxml.FXML;

public class PTHomepageController {
    @FXML
    void onNewEventClick() {
        System.out.println("ADD NEW EVENT - Funzione non ancora implementata");
    }

    @FXML
    void onManageEventsClick() {
        System.out.println("MANAGE EVENTS - Funzione non ancora implementata");
    }

    @FXML
    void onAthleteBookingsClick() {
        ViewManager.changePage("/views/MyBookings.fxml");
    }
}
