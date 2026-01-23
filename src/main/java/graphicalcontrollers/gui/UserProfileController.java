package graphicalcontrollers.gui;

import beans.ProfileStatsBean;
import controllers.BookingController;
import controllers.ProfileController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.user.User;
import utils.session.SessionManager;

public class UserProfileController {
    User user = SessionManager.getInstance().getLoggedUser();

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;

    @FXML
    private Label totalSessionsLabel;
    @FXML
    private Label futureSessionsLabel;
    @FXML
    private Label nextSessionLabel;

    @FXML
    public void initialize() {
        ProfileController pController = new ProfileController();
        ProfileStatsBean profileStatsBean = pController.getProfileStats();
        populateView(profileStatsBean);
    }

    private void populateView(ProfileStatsBean statsBean) {
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        usernameField.setText(user.getUsername());

        totalSessionsLabel.setText(String.valueOf(statsBean.getStats1()));
        futureSessionsLabel.setText(String.valueOf(statsBean.getStats2()));

        if(statsBean.getNextDate() == null || String.valueOf(statsBean.getNextDate()).isEmpty()) {
            nextSessionLabel.setText("Nessuna prenotazione attiva");
        } else {
            nextSessionLabel.setText(String.valueOf(statsBean.getNextDate()));
        }
    }

    @FXML
    private void onEditPasswordClick() {
        ViewManager.changePage("/views/ChangePassword.fxml");
    }

    @FXML
    private void onEditPersonalDataClick() {
        ViewManager.changePage("/views/ChangeName.fxml");
    }

    @FXML
    private void onLogoutClick() {
        freeAll();
        ViewManager.changePage("/views/GuestHomepage.fxml");
    }

    private void freeBSessionIfNotNull() {
        BookingController bController = new BookingController();
        if(bController.isBookingSessionOpen())
            SessionManager.getInstance().freeBookingSession();
    }

    private void freeAll() {
        freeBSessionIfNotNull();
        SessionManager.getInstance().freeSession();
    }
}
