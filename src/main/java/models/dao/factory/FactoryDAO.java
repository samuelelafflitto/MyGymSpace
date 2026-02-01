package models.dao.factory;

import models.booking.BookingDAO;
import models.dailyschedule.DailyScheduleDAO;
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
            switch (execMode) {
                case "demo":
                    instance = new MemDAO();
                    break;
                case "db":
                    instance = new DBDAO();
                    break;
                case "fsys":
                    instance = new FsysDAO();
                    break;
                default:
                    break;
            }
        }
        return instance;
    }

    public boolean isFsys() {
        return Main.getPersistenceMode().equals("fsys");
    }

    public abstract UserDAO createUserDAO();
    public abstract TrainingDAO createTrainingDAO();
    public abstract DailyScheduleDAO createDailyScheduleDAO();
    public abstract BookingDAO createBookingDAO();
}
