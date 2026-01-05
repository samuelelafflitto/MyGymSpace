package beans;

import models.user.PersonalTrainer;

public class SelectedTrainingBean {
    private final String name;
    private final PersonalTrainer personalTrainer;

    public SelectedTrainingBean(String name, PersonalTrainer personalTrainer) {
        this.name = name;
        this.personalTrainer = personalTrainer;
    }

    public String getName() {
        return name;
    }

    public PersonalTrainer getPersonalTrainer() {
        return personalTrainer;
    }
}
