package models.user;

import models.training.Training;

public class PersonalTrainer extends User {
    private Training managedTraining;

    // Costruttore con password
    public PersonalTrainer(String username, String password, String fName, String lName, String type){
        super(username, password, fName, lName, type);
        this.managedTraining = null;
    }

    // Costruttore senza password
    public PersonalTrainer(String username, String fName, String lName, String type){
        super(username, null, fName, lName, type);
        this.managedTraining = null;
    }

    public Training getTraining() {
        return managedTraining;
    }

    public void setTraining(Training managedTraining) {
        this.managedTraining = managedTraining;
    }
}
