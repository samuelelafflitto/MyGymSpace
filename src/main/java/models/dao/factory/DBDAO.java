package models.dao.factory;

import models.booking.BookingDAO;
import models.booking.BookingDAODB;
import models.dailyschedule.DailyScheduleDAO;
import models.dailyschedule.DailyScheduleDAODB;
import models.training.TrainingDAO;
import models.training.TrainingDAODB;
import models.user.UserDAO;

import models.user.UserDAODB;

public class DBDAO extends FactoryDAO {
    @Override
    public UserDAO createUserDAO() {return new UserDAODB();}
    @Override
    public TrainingDAO createTrainingDAO() {return new TrainingDAODB();}
    @Override
    public DailyScheduleDAO createDailyScheduleDAO() {return new DailyScheduleDAODB();}
    @Override
    public BookingDAO createBookingDAO() {return new BookingDAODB();}
}
