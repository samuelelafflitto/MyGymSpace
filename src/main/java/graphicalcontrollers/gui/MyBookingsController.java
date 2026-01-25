package graphicalcontrollers.gui;

import beans.BookingRecapBean;
import controllers.BookingController;
import controllers.PersonalBookingsController;
import javafx.fxml.FXML;

import java.util.List;

public class MyBookingsController extends MyBookingsBaseController {

    @Override
    protected void loadData() {
        PersonalBookingsController PBController = new PersonalBookingsController();
        List<BookingRecapBean> list = PBController.getActiveBookingsFromMap();

        setTableData(list);
    }

    @Override
    protected void deleteBooking(BookingRecapBean booking) {
        BookingController bController = new BookingController();
        // TODO richiamo al controller applicativo per eliminare
        // Metodo deve restituire un boolean
        // deve eliminare la Bookings
        // deve liberare lo slot
        boolean success = false;
        if(bController.deleteBooking(booking)) {
            //Rimozione visiva dalla tabella senza ricaricare tutto
            bookingsTable.getItems().remove(booking);
        }
    }

    @FXML
    public void onViewPastBookingsClick() {
        ViewManager.changePage("/views/MyPastBookings.fxml");
    }
}
