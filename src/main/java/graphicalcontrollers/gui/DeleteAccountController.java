package graphicalcontrollers.gui;

import beans.ProfileDataBean;
import controllers.AuthController;
import exceptions.InvalidPasswordConfirmationException;
import exceptions.MissingDataException;
import exceptions.UserSearchFailedException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputControl;
import utils.ValidationUtils;

public class DeleteAccountController {
    @FXML
    private PasswordField inputPassword;
    @FXML
    private PasswordField confirmPassword;

    @FXML
    public void initialize() {
        ValidationUtils.resetErrorOnType(getFieldsToValidate());
    }

    @FXML
    protected void onConfirmClick() {
        try {
            ValidationUtils.validateNotEmpty(getFieldsToValidate());

            ProfileDataBean bean = new ProfileDataBean();
            bean.setInputPassword(inputPassword.getText());
            bean.setConfirmPassword(confirmPassword.getText());

            AuthController authController = new AuthController();
            if(authController.deleteUser(bean)) {
                System.out.println("Eliminazione profilo riuscita!");
                ViewManager.changePage("/views/GuestHomepage.fxml");
            }
        } catch (MissingDataException e) {
            e.handleException();
        }catch (InvalidPasswordConfirmationException e) {
            e.handleException();
        } catch (UserSearchFailedException e) {
            e.handleException();
        }
    }

    @FXML
    protected void onCancelClick() {
        ViewManager.changePage("/views/MyProfile.fxml");
    }

    @FXML
    protected TextInputControl[] getFieldsToValidate() {
        return new TextInputControl[]{inputPassword, confirmPassword};
    }
}
