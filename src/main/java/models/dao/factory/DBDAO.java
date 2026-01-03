package models.dao.factory;

import models.booking.BookingDAO;
import models.booking.BookingDAO_DB;
import models.dailyschedule.DailyScheduleDAO;
import models.dailyschedule.DailyScheduleDAO_DB;
import models.training.TrainingDAO;
import models.training.TrainingDAO_DB;
import models.user.UserDAO;

import models.user.UserDAO_DB;

public class DBDAO extends FactoryDAO {
    @Override
    public UserDAO createUserDAO() {return new UserDAO_DB();}
    @Override
    public TrainingDAO createTrainingDAO() {return new TrainingDAO_DB();}
    @Override
    public DailyScheduleDAO createDailyScheduleDAO() {return new DailyScheduleDAO_DB();}
    @Override
    public BookingDAO createBookingDAO() {return new BookingDAO_DB();}
}
