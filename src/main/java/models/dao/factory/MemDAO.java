package models.dao.factory;

import models.booking.BookingDAO;
import models.dailyschedule.DailyScheduleDAO;
import models.training.TrainingDAO;
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
