package models.dao.factory;

import models.booking.BookingDAO;
import models.booking.BookingDAOMem;
import models.dailyschedule.DailyScheduleDAO;
import models.dailyschedule.DailyScheduleDAOMem;
import models.training.TrainingDAO;
import models.training.TrainingDAOMem;
import models.user.UserDAO;
import models.user.UserDAOMem;

public class MemDAO extends FactoryDAO {
    @Override
    public UserDAO createUserDAO() {return UserDAOMem.getInstance();}
    @Override
    public TrainingDAO createTrainingDAO() {return TrainingDAOMem.getInstance();}
    @Override
    public DailyScheduleDAO createDailyScheduleDAO() {return DailyScheduleDAOMem.getInstance();}
    @Override
    public BookingDAO createBookingDAO() {return BookingDAOMem.getInstance();}
}
