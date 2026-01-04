package models.user;

import models.dao.factory.FactoryDAO;
import models.training.Training;
import models.training.TrainingDAO;

import java.util.HashMap;
import java.util.Map;

public class UserDAOMem extends UserDAO {
    private final Map<String, User> users;
    private static UserDAOMem instance;
    private static final String PT_TYPE = "PT";
    private static final String ATHLETE_TYPE = "ATHLETE";

    protected UserDAOMem() {
        this.users = new HashMap<>();
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
        }
        System.err.println("[MEM] Username già esistente: " + username);
    }

    /*@Override
    public void addUser(String firstName, String lastName, String username, String password) {
        if (!users.containsKey(username)) {
            User newUser = new Athlete(firstName, lastName, username, password, ATHLETE_TYPE);
            users.put(username, newUser);
            System.out.println("[MEM] Nuovo Atleta registrato: " + username);
        } else {
            System.err.println("[MEM] Username già esistente: " + username);
        }
    }*/

    private void initializeDemoData() {
        PersonalTrainer pt = new PersonalTrainer("Mario", "Rossi", "trainer1", "pass1", PT_TYPE);
        TrainingDAO trainingDAO = FactoryDAO.getInstance().createTrainingDAO();
        Training training = trainingDAO.getTrainingByPT(pt);

        if(training != null) {
            pt.setTraining(training);
            training.setPersonalTrainer(pt);
        } else {
            System.out.println("[MEM] Nessun allenamento trovato per " + pt.getUsername());
        }
        users.put(pt.getUsername(), pt);

        Athlete ath = new Athlete("Luca", "Bianchi", "athlete1", "pass1",  ATHLETE_TYPE);
        users.put(ath.getUsername(), ath);

        System.out.println("[MEM] Dati demo caricati: user='trainer1' e user='athlete1' (con psw='pass1')");
    }

}
