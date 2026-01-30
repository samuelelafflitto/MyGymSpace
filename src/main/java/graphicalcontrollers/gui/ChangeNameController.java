package graphicalcontrollers.gui;

import beans.ProfileDataBean;
import controllers.ProfileController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

public class ChangeNameController extends BaseDataChangeController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;

    @Override
    protected TextInputControl[] getFieldsToValidate() {
        return new TextInputControl[]{firstNameField, lastNameField, oldPasswordField};
    }

    @Override
    protected void populateSpecificBeanData(ProfileDataBean bean) {
        bean.setNewFirstName(firstNameField.getText());
        bean.setNewLastName(lastNameField.getText());
    }

    @Override
    protected boolean performControllerAction(ProfileController pController, ProfileDataBean bean) {
        return pController.changeName(bean);
    }

    @Override
    protected String getSuccessMessage() {
        return "Anagrafica modificata con successo!";
    }
}
