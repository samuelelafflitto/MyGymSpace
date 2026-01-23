package graphicalcontrollers.gui;

import beans.BookingRecapBean;
import controllers.BookingController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.booking.BookingInterface;
import utils.session.BookingSession;
import utils.session.SessionManager;

public class BookingRecapController {
    @FXML
    private TextField trainingPtField;
    @FXML
    private TextField dateHourField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField costField;

    BookingController bController = new BookingController();
    TopBarController tController = new TopBarController();

    @FXML
    public void initialize  () {
        BookingRecapBean bean = bController.getBookingRecap();

        String trainingName = bean.getTrainingName();
        String ptLastName = bean.getPtTraining();
        String date = bean.getDate().toString();
        String hour = bean.getStartTime().toString();
        String description = bean.getDescription();
        String cost = bean.getPrice() + "€";

        String trainingAndPtInfo = trainingName + " - " + ptLastName;
        String dateAndHourInfo = date + ", " +  hour;

        trainingPtField.setText(trainingAndPtInfo);
        dateHourField.setText(dateAndHourInfo);
        descriptionField.setText(description.isEmpty() ? "No extras selected" : description);
        costField.setText(cost);
    }

    @FXML
    private void onConfirmClick() {
        if(bController.saveBooking())
            SessionManager.getInstance().getBookingSession().clearBookingSession();
        tController.onHomeClick();
    }

    @FXML
    private void onCancelClick() {
        SessionManager.getInstance().getBookingSession().clearBookingSession();
        tController.onHomeClick();
    }
}
