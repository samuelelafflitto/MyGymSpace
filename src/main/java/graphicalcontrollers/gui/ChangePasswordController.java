package graphicalcontrollers.gui;

import beans.ProfileDataBean;
import controllers.ProfileController;
import exceptions.InvalidPasswordConfirmationException;
import exceptions.MissingDataException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import utils.ValidationUtils;

public class ChangePasswordController {
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField oldPasswordField;

    @FXML
    public void initialize() {
        ValidationUtils.resetErrorOnType(newPasswordField, oldPasswordField);
    }

    @FXML
    private void onConfirmClick() {
        try {
            ValidationUtils.validateNotEmpty(newPasswordField, oldPasswordField);

            String newPsw = newPasswordField.getText();
            String oldPsw = oldPasswordField.getText();

            ProfileDataBean bean = new ProfileDataBean();
            bean.setNewPassword(newPsw);
            bean.setCurrentPassword(oldPsw);

            ProfileController pController = new ProfileController();

            try {
                pController.changePassword(bean);
            } catch (InvalidPasswordConfirmationException e1) {
                e1.handleException();
            }
        } catch (MissingDataException e2) {
            e2.handleException();
        }
        onCancelClick();
    }

    @FXML
    private void onCancelClick() {
        ViewManager.changePage("/views/MyProfile.fxml");
    }
}
