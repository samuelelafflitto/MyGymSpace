package models.dao.factory;

import models.booking.BookingDAO;
import models.booking.BookingDAO_Mem;
import models.dailyschedule.DailyScheduleDAO;
import models.dailyschedule.DailyScheduleDAO_Mem;
import models.training.TrainingDAO;
import models.training.TrainingDAO_Mem;
import models.user.UserDAO;
import models.user.UserDAO_Mem;

public class MemDAO extends FactoryDAO {
    @Override
    public UserDAO createUserDAO() {return UserDAO_Mem.getInstance();}
    @Override
    public TrainingDAO createTrainingDAO() {return TrainingDAO_Mem.getInstance();}
    @Override
    public DailyScheduleDAO createDailyScheduleDAO() {return DailyScheduleDAO_Mem.getInstance();}
    @Override
    public BookingDAO createBookingDAO() {return BookingDAO_Mem.getInstance();}
}
