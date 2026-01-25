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
import javafx.util.Callback;

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
        Callback<TableColumn<BookingRecapBean, Void>, TableCell<BookingRecapBean, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<BookingRecapBean, Void> call(final TableColumn<BookingRecapBean, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Delete");
                    {
                        btn.getStyleClass().add("delete-button");
                        btn.setOnAction(event -> {
                            BookingRecapBean booking = getTableView().getItems().get(getIndex());
                            deleteBooking(booking);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    protected void setTableData(List<BookingRecapBean> list) {
        ObservableList<BookingRecapBean> data = FXCollections.observableArrayList(list);
        bookingsTable.setItems(data);
    }

    protected abstract void loadData();

    protected abstract void deleteBooking(BookingRecapBean booking);
}
