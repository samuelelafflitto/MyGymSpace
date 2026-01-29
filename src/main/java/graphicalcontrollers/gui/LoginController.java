package graphicalcontrollers.gui;

import beans.LoginBean;
import controllers.AuthController;
import exceptions.MissingDataException;
import exceptions.UserSearchFailedException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.ValidationUtils;
import utils.session.SessionManager;

public class LoginController {
    private static final String ATHLETE_TYPE = "ATH";
    private static final String PT_TYPE = "PT";

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
        ValidationUtils.resetErrorOnType(usernameField, passwordField);
    }

    @FXML
    void onLoginClick() {
        try {
            ValidationUtils.validateNotEmpty(usernameField, passwordField);

            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            LoginBean loginBean = new LoginBean();
            loginBean.setUsername(username);
            loginBean.setPassword(password);

            checkIfSuccessfullyLoggedIn(loginBean);

        } catch (UserSearchFailedException e) {
            e.handleException();
        } catch (MissingDataException e) {
            e.handleException();
        }
    }

    @FXML
    void onCreateAccountClick() {
        ViewManager.changePage("/views/SignUp.fxml");
    }

    private void checkIfSuccessfullyLoggedIn(LoginBean bean) {
        AuthController authController = new AuthController();

        if(authController.authUser(bean)) {
            System.out.println("\nLogin riuscito! Benvenuto, " + bean.getUsername());
            if(SessionManager.getInstance().getLoggedUser().getType().equals(ATHLETE_TYPE)) {
                ViewManager.changePage("/views/AthleteHomepage.fxml");
            } else if (SessionManager.getInstance().getLoggedUser().getType().equals(PT_TYPE)) {
                ViewManager.changePage("/views/PTHomepage.fxml");
            }
        }
    }
}