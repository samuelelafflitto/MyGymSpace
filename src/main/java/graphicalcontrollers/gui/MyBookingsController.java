package graphicalcontrollers.gui;

import beans.BookingRecapBean;
import controllers.PersonalBookingsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MyBookingsController {
    @FXML
    private TableView<BookingRecapBean> bookingsTable;
    @FXML
    private TableColumn<BookingRecapBean, String> trainingColumn;
    @FXML
    private TableColumn<BookingRecapBean, LocalDate> dateColumn;
    @FXML
    private TableColumn<BookingRecapBean, LocalTime> hourColumn;
    @FXML
    private TableColumn<BookingRecapBean, String> ptColumn;
    @FXML
    private TableColumn<BookingRecapBean, BigDecimal> priceColumn;

    @FXML
    public void initialize() {
        trainingColumn.setCellValueFactory(new PropertyValueFactory<>("trainingName"));
        ptColumn.setCellValueFactory(new PropertyValueFactory<>("ptTraining"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        hourColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadData();
    }

    private void loadData() {
        PersonalBookingsController PBController = new PersonalBookingsController();
        List<BookingRecapBean> list = PBController.getFutureBookingsFromMap();

        ObservableList<BookingRecapBean> data = FXCollections.observableArrayList(list);
        bookingsTable.setItems(data);
    }

    public void onViewPastBookingsClick() {

    }
}
