package graphicalcontrollers.gui;

import beans.ProfileStatsBean;
import controllers.BookingController;
import controllers.ProfileController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.user.User;
import utils.session.SessionManager;

public class MyProfileController {
    User user = SessionManager.getInstance().getLoggedUser();
    private static final String PT_TYPE = "PT";
    private static final String COMPACT_BUTTON_STYLE = "compact-button";

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
    private Button btnEditPersonalData;
    @FXML
    private Button btnEditTraining;
    @FXML
    private Button btnDeleteAccount;

    @FXML
    public void initialize() {
        ProfileController pController = new ProfileController();
        ProfileStatsBean profileStatsBean = pController.getProfileStats();
        populateView(profileStatsBean);

        configurePtButton();
    }

    private void configurePtButton() {
        if(user.getType().equals(PT_TYPE)) {
            btnEditTraining.setVisible(true);
            btnEditTraining.setManaged(true);

            applyCompactStyleCSS(btnEditPersonalData);
            applyCompactStyleCSS(btnEditTraining);
            applyCompactStyleCSS(btnDeleteAccount);
        } else{
            btnEditTraining.setVisible(false);
            btnEditTraining.setManaged(false);

            removeCompactStyleCSS(btnEditPersonalData);
            removeCompactStyleCSS(btnDeleteAccount);
        }
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
    private void onDeleteAccountClick() {
        ViewManager.changePage("/views/DeleteAccountConfirmation.fxml");
    }

    @FXML
    private void onEditTrainingClick() {
        ViewManager.changePage("/views/EditTraining.fxml");
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

    // HELPER
    private void applyCompactStyleCSS(Button btn) {
        if(!btn.getStyleClass().contains(COMPACT_BUTTON_STYLE)) {
            btn.getStyleClass().add(COMPACT_BUTTON_STYLE);
        }
    }

    private void removeCompactStyleCSS(Button btn) {
        btn.getStyleClass().remove(COMPACT_BUTTON_STYLE);
    }
}
