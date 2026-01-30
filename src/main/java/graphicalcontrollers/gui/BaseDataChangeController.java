package graphicalcontrollers.gui;

import beans.ProfileDataBean;
import controllers.ProfileController;
import exceptions.InvalidPasswordConfirmationException;
import exceptions.MissingDataException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputControl;
import utils.ValidationUtils;

public abstract class BaseDataChangeController {
    @FXML
    protected PasswordField oldPasswordField;

    @FXML
    public void initialize() {
        ValidationUtils.resetErrorOnType(getFieldsToValidate());
    }

    @FXML
    protected void onConfirmClick() {
        try {
            ValidationUtils.validateNotEmpty(getFieldsToValidate());

            ProfileDataBean bean = new ProfileDataBean();
            bean.setCurrentPassword(oldPasswordField.getText());

            populateSpecificBeanData(bean);

            ProfileController pController = new ProfileController();
            if(performControllerAction(pController, bean)) {
                System.out.println(getSuccessMessage());
                onCancelClick();
            }
        } catch (InvalidPasswordConfirmationException e) {
            e.handleException();
        } catch (MissingDataException e) {
            e.handleException();
        }
    }

    @FXML
    protected void onCancelClick() {
        ViewManager.changePage("/views/MyProfile.fxml");
    }

    protected abstract TextInputControl[] getFieldsToValidate();
    protected abstract void populateSpecificBeanData(ProfileDataBean bean);
    protected abstract boolean performControllerAction(ProfileController pController, ProfileDataBean bean);
    protected abstract String getSuccessMessage();
}
