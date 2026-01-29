package graphicalcontrollers.gui;

import beans.SignupBean;
import controllers.AuthController;
import exceptions.ExistingUserException;
import exceptions.MissingDataException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.ValidationUtils;
import utils.session.SessionManager;

public class SignUpController {
    private static final String ATHLETE_TYPE = "ATH";
    private static final String PT_TYPE = "PT";

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
        ValidationUtils.resetErrorOnType(firstNameField, lastNameField, usernameField, passwordField);
    }

    @FXML
    void onCreateClick() {
        try {
            ValidationUtils.validateNotEmpty(firstNameField, lastNameField, usernameField, passwordField);

            String fName = firstNameField.getText();
            String lName = lastNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();

            SignupBean signupBean = new SignupBean();
            signupBean.setFirstName(fName);
            signupBean.setLastName(lName);
            signupBean.setUsername(username);
            signupBean.setPassword(password);

            checkIfSuccessfullySignedUp(signupBean);

        } catch (ExistingUserException e) {
            e.handleException();
        } catch (MissingDataException e) {
            e.handleException();
        }
    }

    @FXML
    void onLoginClick() {
        ViewManager.changePage("/views/Login.fxml");
    }

    private void checkIfSuccessfullySignedUp(SignupBean bean) {
        AuthController authController = new AuthController();

        if (authController.registerUser(bean)) {
            System.out.println("Registrazione riuscita! Benvenuto, " + bean.getUsername() + "!");
            if(SessionManager.getInstance().getLoggedUser().getType().equals(ATHLETE_TYPE)) {
                ViewManager.changePage("/views/AthleteHomepage.fxml");
            } else if (SessionManager.getInstance().getLoggedUser().getType().equals(PT_TYPE)) {
                ViewManager.changePage("/views/PTHomepage.fxml");
            }
        }
    }
}
