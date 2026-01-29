package graphicalcontrollers.gui;

import beans.ProfileDataBean;
import controllers.ProfileController;
import exceptions.InvalidPasswordConfirmationException;
import exceptions.MissingDataException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.ValidationUtils;

public class ChangeNameController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private PasswordField oldPasswordField;

    @FXML
    public void initialize() {
        ValidationUtils.resetErrorOnType(firstNameField, lastNameField, oldPasswordField);
    }

    @FXML
    private void onConfirmClick() {
        try {
            ValidationUtils.validateNotEmpty(firstNameField, lastNameField, oldPasswordField);

            String newFN = firstNameField.getText();
            String newLN = lastNameField.getText();
            String oldPsw = oldPasswordField.getText();

            ProfileDataBean bean = new ProfileDataBean();
            bean.setNewFirstName(newFN);
            bean.setNewLastName(newLN);
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

        if(pController.changeName(bean)) {
            System.out.println("Anagrafica modificata con successo!");
            onCancelClick();
        }
        pController.changeName(bean);
    }
}
