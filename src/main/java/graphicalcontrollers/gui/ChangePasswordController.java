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

            checkIfSuccessfullyModified(bean);

        } catch (InvalidPasswordConfirmationException e) {
            e.handleException();
        } catch (MissingDataException e) {
            e.handleException();
        }
    }

    @FXML
    private void onCancelClick() {
        ViewManager.changePage("/views/MyProfile.fxml");
    }

    private void checkIfSuccessfullyModified(ProfileDataBean bean) {
        ProfileController pController = new ProfileController();

        if(pController.changePassword(bean)) {
            System.out.println("Password modificata con successo!");
            ViewManager.changePage("/views/MyProfile.fxml");
        }
        pController.changeName(bean);
    }
}
