package graphicalcontrollers.gui;

import beans.BookingRecapBean;
import controllers.PersonalBookingsController;
import javafx.fxml.FXML;

import java.util.List;

public class MyPastBookingsController extends MyBookingsBaseController {
    @Override
    protected void loadData() {
        PersonalBookingsController PBController = new PersonalBookingsController();
        List<BookingRecapBean> list = PBController.getPastBookingsFromMap();

        setTableData(list);
    }

    @FXML
    public void onViewFutureBookingsClick() {
        ViewManager.changePage("/views/MyBookings.fxml");
    }
}
