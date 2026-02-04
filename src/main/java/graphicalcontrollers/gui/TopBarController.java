package graphicalcontrollers.gui;

import javafx.fxml.FXML;
import utils.session.SessionManager;

public class TopBarController {
    private static final String ATHLETE_TYPE = "ATH";
    private static final String PT_TYPE = "PT";

    @FXML
    public void onHomeClick() {
        SessionManager sessionManager = SessionManager.getInstance();
        if(sessionManager.getLoggedUser() != null) {
            sessionManager.freeBookingSession();
            sessionManager.setSelectedBookingToDelete(null);
            // ALTRE SESSIONI DA LIBERARE
            if(sessionManager.getLoggedUser().getType().equals(ATHLETE_TYPE)) {
                ViewManager.changePage("/views/AthleteHomepage.fxml");
            } else if(sessionManager.getLoggedUser().getType().equals(PT_TYPE)) {
                ViewManager.changePage("/views/PTHomepage.fxml");
            }
        } else {
            ViewManager.changePage("/views/GuestHomepage.fxml");
        }
    }

    @FXML
    void onProfileClick() {
        SessionManager sessionManager = SessionManager.getInstance();
        if(sessionManager.getLoggedUser() != null) {
            sessionManager.freeBookingSession();
            sessionManager.setSelectedBookingToDelete(null);
            // ALTRE SESSIONI DA LIBERARE
            ViewManager.changePage("/views/MyProfile.fxml");
        } else {
            ViewManager.changePage("/views/Login.fxml");
        }
    }
}