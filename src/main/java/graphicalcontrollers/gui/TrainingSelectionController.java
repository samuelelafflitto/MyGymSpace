package graphicalcontrollers.gui;

import beans.AvailableTrainingBean;
import controllers.BookingController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TrainingSelectionController {
    @FXML
    private TopBarController topBarController = new TopBarController();

    @FXML
    private TableView<AvailableTrainingBean> trainingTable;
    @FXML
    private TableColumn<AvailableTrainingBean, String> trainingColumn;
    @FXML
    private TableColumn<AvailableTrainingBean, String> ptColumn;
    @FXML
    private TableColumn<AvailableTrainingBean, String> descriptionColumn;
    @FXML
    private TableColumn<AvailableTrainingBean, BigDecimal> basePriceColumn;

    @FXML
    private ComboBox<AvailableTrainingBean> trainingSelector;

    @FXML
    public Button confirmButton;

    private static final String ERROR_STYLE = "input-error";

    @FXML
    private void initialize() {
        trainingColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ptColumn.setCellValueFactory(new PropertyValueFactory<>("ptLastName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        basePriceColumn.setCellValueFactory(new PropertyValueFactory<>("basePrice"));

        trainingSelector.setConverter(new StringConverter<>() {
            @Override
            public String toString(AvailableTrainingBean bean) {
                if (bean == null) {
                    return null;
                }
                return bean.getName() + " - " + bean.getPtLastName();
            }

            @Override
            public AvailableTrainingBean fromString(String string) {
                return null;
            }
        });

        trainingSelector.valueProperty().addListener((_, _, newText) -> {
            if(newText != null) {
                trainingSelector.getStyleClass().remove(ERROR_STYLE);
            }
        });

        loadData();
    }

    public void loadData() {
        BookingController bController = new BookingController();

        List<AvailableTrainingBean> list = bController.getAvailableTrainings();
        ObservableList<AvailableTrainingBean> tableData = FXCollections.observableArrayList(list);
        trainingTable.setItems(tableData);

        List<String> comboBoxOptions = new ArrayList<>();
        for(AvailableTrainingBean b: list) {
            String boxLabel = b.getName() + " - " + b.getPtLastName();
            if(!comboBoxOptions.contains(boxLabel)) {
                comboBoxOptions.add(boxLabel);
            }
        }

        trainingSelector.getItems().setAll(list);
    }

    @FXML
    private void onConfirmClick() {
        BookingController bController = new BookingController();

        if(trainingSelector.getValue() == null) {
            if(!trainingSelector.getStyleClass().contains(ERROR_STYLE)) {
                trainingSelector.getStyleClass().add(ERROR_STYLE);
            }
            return;
        }

        AvailableTrainingBean selectedTrainer = trainingSelector.getValue();
        bController.setBookingSessionTraining(selectedTrainer);

        ViewManager.changePage("/views/BookingForm.fxml");
    }

    @FXML
    private void onBackClick() {
        topBarController.onHomeClick();
    }
}
