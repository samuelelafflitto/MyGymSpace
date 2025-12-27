package models.dao.factory;

import models.booking.BookingDAO;
import models.training.TrainingDAO;
import models.user.UserDAO;
import start.Main;

public abstract class FactoryDAO {
    private static FactoryDAO instance;

    protected  FactoryDAO() {
    }

    public static FactoryDAO getInstance() {
        if (instance == null) {
            String execMode = Main.getPersistenceMode();
            if (execMode.equals("demo")) {
                instance = new MemDAO;
            } else if (execMode.equals("db")) {
                instance = new DBDAO;
            } else if (execMode.equals("fsys")) {
                instance = new FsysDB;
            }
        }
        return instance;
    }

    public abstract UserDAO createUserDAO();
    public abstract TrainingDAO createTrainingDAO();
    public abstract DailyScheduleDAO createDailyScheduleDAO();
    public abstract BookingDAO createBookingDAO();
}
