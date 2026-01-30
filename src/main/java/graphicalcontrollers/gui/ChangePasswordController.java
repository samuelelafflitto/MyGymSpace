package graphicalcontrollers.gui;

import beans.ProfileDataBean;
import controllers.ProfileController;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputControl;

public class ChangePasswordController extends BaseDataChangeController{
    @FXML
    private PasswordField newPasswordField;

    @Override
    protected TextInputControl[] getFieldsToValidate() {
        return new TextInputControl[]{newPasswordField, oldPasswordField};
    }

    @Override
    protected void populateSpecificBeanData(ProfileDataBean bean) {
        bean.setNewPassword(newPasswordField.getText());
    }

    @Override
    protected boolean performControllerAction(ProfileController pController, ProfileDataBean bean) {
        return pController.changePassword(bean);
    }

    @Override
    protected String getSuccessMessage() {
        return "Password modificata con successo!";
    }
}
