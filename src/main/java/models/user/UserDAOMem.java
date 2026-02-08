package models.user;

import models.booking.BookingDAO;
import models.dao.factory.FactoryDAO;
import models.training.TrainingDAO;
import models.training.TrainingDAOMem;

import java.util.HashMap;
import java.util.Map;

public class UserDAOMem extends UserDAO {
    private final Map<String, User> users;
    private static UserDAOMem instance;
    private static final String PT_TYPE = "PT";
    private static final String ATHLETE_TYPE = "ATH";

    TrainingDAO tDAO = FactoryDAO.getInstance().createTrainingDAO();
    BookingDAO bDAO = FactoryDAO.getInstance().createBookingDAO();

    protected UserDAOMem() {
        users = new HashMap<>();
        initializeDemoData();
    }

    public static UserDAOMem getInstance() {
        if (instance == null) {
            instance = new UserDAOMem();
        }
        return instance;
    }

    @Override
    public User getUser(String username, String password) {
        if(users.containsKey(username)) {
            User user = users.get(username);
            if(user.getPassword() != null && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return users.get(username);
    }

    public void addUser(String username, User user) {
        if(!users.containsKey(username)) {
            users.put(username, user);
            System.out.println("[MEM] User " + username + " successfully added!");
        } else {
            System.err.println("[MEM] Username already used: " + username);
        }
    }

    @Override
    public void deleteUser(String username) {
        if(users.containsKey(username)) {
            if(users.get(username).getType().equals(PT_TYPE)) {
                PersonalTrainer pt = (PersonalTrainer) users.get(username);

                bDAO.deleteBooking(null, pt.getUsername(), null, null);
                tDAO.deleteDemoTraining(pt);
            } else if(users.get(username).getType().equals(ATHLETE_TYPE)) {
                Athlete ath = (Athlete) users.get(username);
                bDAO.deleteBooking(ath.getUsername(), null, null, null);
            }
            users.remove(username);
            System.out.println("[MEM] User " + username + " successfully deleted!");
        } else {
            System.err.println("[MEM] No user found with username: " + username);
        }
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        User user = users.get(username);
        if(user != null) {
            user.setPassword(newPassword);
            System.out.println("[MEM] Password updated for user with username: " + username);
        }
    }

    @Override
    public void updateName(String username, String newFirstName, String newLastName) {
        User user = users.get(username);
        if(user != null) {
            user.setFirstName(newFirstName);
            user.setLastName(newLastName);
            System.out.println("[MEM] Personal data updated for user with username: " + username);
        }
    }


    private void initializeDemoData() {
        // User Entity Creation
        PersonalTrainer pt1 = new PersonalTrainer ("trainer1", "pass1", "Mario", "Rossi", PT_TYPE);
        PersonalTrainer pt2 = new PersonalTrainer ("trainer2", "pass2", "Luigi", "Mangione", PT_TYPE);
        Athlete athlete = new Athlete ("athlete1", "pass1", "Luca", "Bianchi",  ATHLETE_TYPE);

        // Adding Users
        users.put(pt1.getUsername(), pt1);
        users.put(pt2.getUsername(), pt2);
        users.put(athlete.getUsername(), athlete);

        TrainingDAOMem trainingDAO = (TrainingDAOMem) FactoryDAO.getInstance().createTrainingDAO();
        trainingDAO.initializeDemoData(pt1, pt2);
    }

    @Override
    public User fetchUserFromPersistence(String username, String type, Map<String, User> userCache) {
        User user = users.get(username);
        if(user != null) {
            userCache.put(username, user);
        }
        return user;
    }

}
