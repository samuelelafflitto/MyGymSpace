package graphicalcontrollers.gui;

import beans.SelectedDateBean;
import beans.SelectedSlotAndExtraBean;
import controllers.BookingController;
import exceptions.SameDateSameTimeException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import utils.session.BookingSession;
import utils.session.SessionManager;

import java.time.LocalDate;
import java.util.List;

public class BookingFormController {
    private final AthleteHomepageController athleteController = new AthleteHomepageController();
    private final BookingController bController = new BookingController();

    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> timeSlotSelector;

    @FXML
    private CheckBox towelCheck;
    @FXML
    private CheckBox saunaCheck;
    @FXML
    private CheckBox energizerCheck;
    @FXML
    private CheckBox videoCheck;

    @FXML
    public Button proceedButton;

    SelectedDateBean selectedDateBean;
    SelectedSlotAndExtraBean slotAndExtraBean;

    private static final String ERROR_STYLE = "input-error";

    @FXML
    public void initialize() {
        configureDatePicker();

        datePicker.valueProperty().addListener((_, _, newDate) -> {
            if(newDate != null) {
                onDateSelected(newDate);
            }
        });
    }

    private void configureDatePicker() {
        Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if(item ==  null || empty) {
                            return;
                        }

                        boolean isPast = bController.isPastDate(item);
                        boolean isHoliday = bController.isHoliday(item);

                        if(isPast) {
                            setDisable(true);
                            setStyle("-fx-background-color: #e2dfdf;");
                        } else if(isHoliday) {
                            setDisable(true);
                            setStyle("-fx-background-color: #e2dfdf;");
                            setTooltip(new Tooltip("La palestra è chiusa!"));
                        }
                    }
                };
            }
        };

        datePicker.setDayCellFactory(dayCellFactory);
    }

    private void onDateSelected(LocalDate date) {
        String dateString = date.toString();

        System.out.println("Data selezionata: " + date);

        selectedDateBean = new SelectedDateBean(dateString);
        bController.setBookingSessionDate(selectedDateBean);

        List<String> availableSlots = bController.getAvailableSlots();
        timeSlotSelector.getItems().clear();
        timeSlotSelector.getItems().addAll(availableSlots);

        timeSlotSelector.setDisable(false);
        towelCheck.setDisable(false);
        saunaCheck.setDisable(false);
        energizerCheck.setDisable(false);
        videoCheck.setDisable(false);
    }

    @FXML
    private void onProceedClick() {
        if(!isInputValid()) {
            return;
        }

        slotAndExtraBean = createSlotAndExtraBean();
        bController.setBookingSessionBooking(slotAndExtraBean);

        goToBookingRecap();
    }

    @FXML
    private void onBackClick() {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();
        bSession.clearBookingSession();
        athleteController.onBookSessionClick();
    }

    // HELPER
    private boolean isInputValid() {
        boolean valid = true;

        if(datePicker.getValue() == null) {
            addErrorStyle(datePicker);
            valid = false;
        }

        if(timeSlotSelector.getValue() == null) {
            addErrorStyle(timeSlotSelector);
            valid = false;
        }
        return valid;
    }

    private void addErrorStyle(Control node) {
        if(!node.getStyleClass().contains(ERROR_STYLE)) {
            node.getStyleClass().add(ERROR_STYLE);
        }
    }

    private SelectedSlotAndExtraBean createSlotAndExtraBean() {
        SelectedSlotAndExtraBean bean = new SelectedSlotAndExtraBean();

        String slotString = timeSlotSelector.getValue();
        int selectedSlot = Integer.parseInt(slotString.split(":")[0]);

        bean.setSelectedSlot(selectedSlot);

        bean.setTowel(towelCheck.isSelected() ? "y" : "n");
        bean.setSauna(saunaCheck.isSelected() ? "y" : "n");
        bean.setEnergizer(energizerCheck.isSelected() ? "y" : "n");
        bean.setVideo(videoCheck.isSelected() ? "y" : "n");

        return bean;
    }

    private void goToBookingRecap() {
        try{
            if(bController.checkSameDateSameTimeBooking()) {
                ViewManager.changePage("/views/bookingRecap.fxml");
            }
        } catch (SameDateSameTimeException e) {
            e.handleException();
        }
    }
}
