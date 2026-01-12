package models.user;

import models.training.Training;

public class PersonalTrainer extends User {
    private Training managedTraining;

    public PersonalTrainer(String username, String password, String fName, String lName, String type){
        super(username, password, fName, lName, type);
        this.managedTraining = getTraining();
    }

    public Training getTraining() {
        return managedTraining;
    }

    public void setTraining(Training managedTraining) {
        this.managedTraining = managedTraining;
    }
}
