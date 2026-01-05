package beans;

import models.user.PersonalTrainer;

public class AvailableTrainingBean {
    private String name;
    private String description;
    private double basePrice;
    private PersonalTrainer personalTrainer;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public PersonalTrainer getPersonalTrainer() {
        return personalTrainer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public void setPersonalTrainer(PersonalTrainer personalTrainer) {
        this.personalTrainer = personalTrainer;
    }
}
