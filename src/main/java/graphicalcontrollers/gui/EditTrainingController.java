package graphicalcontrollers.gui;

import beans.TrainingToEditBean;
import controllers.ProfileController;
import exceptions.MissingDataException;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.training.Training;
import models.user.PersonalTrainer;
import models.user.User;
import utils.ValidationUtils;
import utils.session.SessionManager;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EditTrainingController {
    ProfileController pController = new ProfileController();

    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField priceField;

    private static final String ERROR_STYLE = "input-error";
    private static final BigDecimal MAX_PRICE = new BigDecimal("30.00");
    private Training t;


    @FXML
    private void initialize() {
        ValidationUtils.resetErrorOnType(priceField);

        User currentUser = SessionManager.getInstance().getLoggedUser();

        t = ((PersonalTrainer) currentUser).getTraining();

        String tName = t.getName();
        String tDescription =  t.getDescription();
        BigDecimal tBasePrice =  t.getBasePrice();

        if(t != null) {
            nameField.setText(tName);
            descriptionArea.setText(tDescription);

            if(tBasePrice != null) {
                priceField.setText(tBasePrice.toPlainString());
            } else {
                priceField.setText("0.00");
            }
        } else {
            System.out.println("Errore nel recupero dell'allenamento");
            onCancelClick();
        }
    }

    @FXML
    private void onSaveClick() {
        try {
            ValidationUtils.validateNotEmpty(priceField);
        } catch (MissingDataException e) {
            e.handleException();
            return;
        }

        BigDecimal newBasePrice;
        String priceText = priceField.getText().replace(",", ".");

        try {
            newBasePrice = new BigDecimal(priceText).setScale(2, RoundingMode.HALF_UP);

            if(newBasePrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new NumberFormatException("Prezzo negativo!");
            }

            priceField.getStyleClass().remove(ERROR_STYLE);
        } catch (NumberFormatException e) {
            if(!priceField.getStyleClass().contains(ERROR_STYLE)) {
                priceField.getStyleClass().add(ERROR_STYLE);
            }
            System.out.println("Il prezzo deve essere un numero valido e non negativo.");
            return;
        }

        try {
            t.setDescription(descriptionArea.getText());
            if(newBasePrice.compareTo(MAX_PRICE) > 0) {
                t.setBasePrice(MAX_PRICE);
            } else {
                t.setBasePrice(newBasePrice);
            }

            TrainingToEditBean bean = new TrainingToEditBean();
            bean.setName(t.getName());
            bean.setDescription(t.getDescription());
            bean.setBasePrice(t.getBasePrice());

            if(pController.updateTraining(bean)) {
                ViewManager.changePage("/views/SuccessfulPage.fxml");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onCancelClick() {
        ViewManager.changePage("/views/MyProfile.fxml");
    }
}
