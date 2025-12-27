package models.user;

import models.training.Training;

public class PersonalTrainer extends User{
    private Training managedTraining;

    public PersonalTrainer(String fName, String lName, String username, String password, String type){
        super(fName, lName, username, password, type);
    }
}
