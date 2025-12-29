package models.dao.factory;

import models.booking.BookingDAO;
import models.dailyschedule.DailyScheduleDAO;
import models.training.TrainingDAO;
import models.training.TrainingDAO_Fsys;
import models.user.UserDAO;
import models.user.UserDAO_Fsys;

public class FsysDAO extends FactoryDAO {
    public UserDAO createUserDAO() {return new UserDAO_Fsys();}
    public TrainingDAO createTrainingDAO() {return new TrainingDAO_Fsys();}
    public DailyScheduleDAO createDailyScheduleDAO() {return new DailyScheduleDAO_Fsys();}
    public BookingDAO createBookingDAO() {return new BookingDAO_Fsys();}
}
