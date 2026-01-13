package models.training;

import models.user.PersonalTrainer;
import utils.PriceConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TrainingDAOMem extends TrainingDAO {

    private final List<Training> trainings;
    private static TrainingDAOMem instance;

    protected TrainingDAOMem() {
        trainings = new ArrayList<>();
        initializeDemoData();
    }

    public static synchronized TrainingDAOMem getInstance() {
        if (instance == null) {
            instance = new TrainingDAOMem();
        }
        return instance;
    }

    @Override
    public List<Training> getAvailableTrainings() {
        return new ArrayList<>(this.trainings);
    }

    @Override
    public Training getTrainingByPT(PersonalTrainer personalTrainer) {
        for (Training training : this.trainings) {
            if (training.getPersonalTrainer() != null && training.getPersonalTrainer().getUsername().equals(personalTrainer.getUsername())) {
                return training;
            }
        }
        return null;
    }


    private void initializeDemoData() {
        PersonalTrainer pt = new PersonalTrainer("trainer 1", "pass1", "Mario", "Rossi", "PT");

        String title = "Box";
        String description = "Sessione privata di Box con Personal Trainer";
        BigDecimal price = PriceConfig.getPrice("training.boxing.price", new BigDecimal("20.00"));

        Training demoTraining = new Training();
        demoTraining.setPersonalTrainer(pt);
        demoTraining.setName(title);
        demoTraining.setDescription(description);
        demoTraining.setBasePrice(price);

        this.trainings.add(demoTraining);
    }
}
