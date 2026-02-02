package graphicalcontrollers.gui;

import beans.BookingRecapBean;
import controllers.PersonalBookingsController;
import javafx.fxml.FXML;
import utils.session.SessionManager;

import java.util.List;

public class MyBookingsController extends MyBookingsBaseController {

    @Override
    protected void loadData() {
        PersonalBookingsController pBController = new PersonalBookingsController();
        List<BookingRecapBean> list = pBController.getActiveBookingsFromMap();

        setTableData(list);
    }

    @Override
    protected void deleteBooking(BookingRecapBean booking) {
        SessionManager.getInstance().setSelectedBookingToDelete(booking);
        ViewManager.changePage("/views/DeleteBookingConfirmation.fxml");
    }

    @FXML
    public void onViewPastBookingsClick() {
        ViewManager.changePage("/views/MyPastBookings.fxml");
    }
}
