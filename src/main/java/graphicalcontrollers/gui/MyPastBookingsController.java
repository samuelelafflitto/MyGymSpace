package graphicalcontrollers.gui;

import beans.BookingRecapBean;
import controllers.PersonalBookingsController;
import javafx.fxml.FXML;

import java.util.List;

public class MyPastBookingsController extends MyBookingsBaseController {
    @Override
    public void initialize() {
        super.initialize();
        actionColumn.setVisible(false);
    }

    @Override
    protected void loadData() {
        PersonalBookingsController PBController = new PersonalBookingsController();
        List<BookingRecapBean> list = PBController.getPastBookingsFromMap();

        setTableData(list);
    }

    @Override
    protected void deleteBooking(BookingRecapBean booking) {
        // Lo storico non può essere cancellato dall'utente
    }

    @FXML
    public void onViewFutureBookingsClick() {
        ViewManager.changePage("/views/MyBookings.fxml");
    }
}
