package graphicalcontrollers.gui;

import beans.BookingRecapBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public abstract class MyBookingsBaseController {
    @FXML
    protected TableView<BookingRecapBean> bookingsTable;
    @FXML
    protected TableColumn<BookingRecapBean, String> trainingColumn;
    @FXML
    protected TableColumn<BookingRecapBean, String> athColumn;
    @FXML
    protected TableColumn<BookingRecapBean, LocalDate> dateColumn;
    @FXML
    protected TableColumn<BookingRecapBean, LocalTime> hourColumn;
    @FXML
    protected TableColumn<BookingRecapBean, String> ptColumn;
    @FXML
    protected TableColumn<BookingRecapBean, BigDecimal> priceColumn;
    @FXML
    protected TableColumn<BookingRecapBean, Void> actionColumn;

    @FXML
    public void initialize() {
        trainingColumn.setCellValueFactory(new PropertyValueFactory<>("trainingName"));
        ptColumn.setCellValueFactory(new PropertyValueFactory<>("ptLastName"));
        athColumn.setCellValueFactory(new PropertyValueFactory<>("athCompleteName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        hourColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        addButtonToTable();

        loadData();
    }

    private void addButtonToTable() {
        actionColumn.setCellFactory(_ -> new DeleteButtonCell());
    }

    private class DeleteButtonCell extends TableCell<BookingRecapBean, Void> {
        private final Button rowBtn = new Button("Delete");

        public DeleteButtonCell() {
            rowBtn.getStyleClass().add("DeleteButtonCell");
            rowBtn.setOnAction(_ -> {
                BookingRecapBean booking = getTableView().getItems().get(getIndex());
                deleteBooking(booking);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if(empty) {
                setGraphic(null);
            } else {
                setGraphic(rowBtn);
            }
        }
    }

    protected void setTableData(List<BookingRecapBean> list) {
        ObservableList<BookingRecapBean> data = FXCollections.observableArrayList(list);
        bookingsTable.setItems(data);
    }

    protected abstract void loadData();

    protected abstract void deleteBooking(BookingRecapBean booking);
}
