package beans;

import models.user.PersonalTrainer;

public class SelectedTrainingBean {
    private final String name;
    private final PersonalTrainer personalTrainer;

    public SelectedTrainingBean(String name, PersonalTrainer personalTrainer) {
        this.name = name;
        this.personalTrainer = personalTrainer;
    }

    // GETTER
    public String getName() {
        return name;
    }

    // SETTER
    public PersonalTrainer getPersonalTrainer() {
        return personalTrainer;
    }
}
