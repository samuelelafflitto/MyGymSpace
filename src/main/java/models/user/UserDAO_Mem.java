package models.user;

import models.dao.factory.FactoryDAO;
import models.training.Training;
import models.training.TrainingDAO;

import java.util.HashMap;
import java.util.Map;

public class UserDAO_Mem extends UserDAO {
    private final Map<String, User> users;
    private static UserDAO_Mem instance;
    private static final String PT_TYPE = "PT";
    private static final String ATHLETE_TYPE = "ATHLETE";

    protected UserDAO_Mem() {
        this.users = new HashMap<>();
        initializeDemoData();
    }

    public static UserDAO_Mem getInstance() {
        if (instance == null) {
            instance = new UserDAO_Mem();
        }
        return instance;
    }

    @Override
    public User getUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
            return users.get(username);
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
        Training training = trainingDAO.getTraining("Box");
        training.setPersonalTrainer(pt);
        trainingDAO.insertTraining(training);
        pt.setTraining(training);
        users.put(pt.getUsername(), pt);

        Athlete ath = new Athlete("Luca", "Bianchi", "athlete1", "pass1",  ATHLETE_TYPE);
        users.put(ath.getUsername(), ath);

        System.out.println("[MEM] Dati demo caricati: user='trainer1' e user='athlete1' (con psw='pass1')");
    }

}
