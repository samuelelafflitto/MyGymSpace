package models.dao.factory;

import models.booking.BookingDAO;
import models.booking.BookingDAOFsys;
import models.dailyschedule.DailyScheduleDAO;
import models.dailyschedule.DailyScheduleDAOFsys;
import models.training.TrainingDAO;
import models.training.TrainingDAOFsys;
import models.user.UserDAO;
import models.user.UserDAOFsys;

public class FsysDAO extends FactoryDAO {
    public UserDAO createUserDAO() {return new UserDAOFsys();}
    public TrainingDAO createTrainingDAO() {return new TrainingDAOFsys();}
    public DailyScheduleDAO createDailyScheduleDAO() {return new DailyScheduleDAOFsys();}
    public BookingDAO createBookingDAO() {return new BookingDAOFsys();}
}
