package graphicalcontrollers.gui;

import beans.BookingRecapBean;
import beans.ProfileDataBean;
import controllers.AuthController;
import controllers.BookingController;
import exceptions.InvalidPasswordConfirmationException;
import exceptions.MissingDataException;
import exceptions.UserSearchFailedException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import utils.ValidationUtils;
import utils.session.SessionManager;

public class DeleteBookingController {
    AuthController authController = new AuthController();
    BookingController bController = new BookingController();

    @FXML
    private TextField trainingPtField;
    @FXML
    private TextField dateHourField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField costField;

    @FXML
    private PasswordField inputPassword;
    @FXML
    private PasswordField confirmPassword;

    private BookingRecapBean bookingToDelete;

    @FXML
    public void initialize() {
        ValidationUtils.resetErrorOnType(getFieldsToValidate());

        this.bookingToDelete = SessionManager.getInstance().getSelectedBookingToDelete();

        if(bookingToDelete != null) {
            trainingPtField.setText(bookingToDelete.getTrainingName() + " - " + bookingToDelete.getPtLastName());
            dateHourField.setText(bookingToDelete.getDate() + ", " + bookingToDelete.getStartTime());
            descriptionField.setText(bookingToDelete.getDescription());
            costField.setText(bookingToDelete.getPrice() + "€");
        }
    }

    @FXML
    public void onConfirmClick() {
        try {
            ValidationUtils.validateNotEmpty(getFieldsToValidate());

            String pass1 = inputPassword.getText();
            String pass2 = confirmPassword.getText();

            if(pass1 == null || pass2 == null) {
                throw new MissingDataException();
            }

            if(!pass1.equals(pass2)) {
                throw new InvalidPasswordConfirmationException();
            }

            ProfileDataBean bean = new ProfileDataBean();
            bean.setInputPassword(inputPassword.getText());
            bean.setConfirmPassword(confirmPassword.getText());

            if(authController.isPasswordWrong(bean)) {
                throw new UserSearchFailedException();
            } else {
                if(bController.deleteBooking(bookingToDelete)) {
                    System.out.println("Eliminazione prenotazione riuscita!");
                    onCancelClick();
                }
            }

        } catch (MissingDataException e) {
            e.handleException();
        } catch (InvalidPasswordConfirmationException e) {
            e.handleException();
        } catch (UserSearchFailedException e) {
            e.handleException();
        }
    }

    @FXML
    protected void onCancelClick() {
        SessionManager.getInstance().setSelectedBookingToDelete(null);
        ViewManager.changePage("/views/MyBookings.fxml");
    }

    @FXML
    protected TextInputControl[] getFieldsToValidate() {
        return new TextInputControl[]{inputPassword, confirmPassword};
    }
}
