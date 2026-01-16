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
        /*initializeDemoData();*/
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

    @Override
    public void initializeDemoData(PersonalTrainer pt1, PersonalTrainer pt2) {
        String title1 = "Box";
        String description1 = "Sessione privata di Box con Personal Trainer";
        BigDecimal price1 = PriceConfig.getPrice("training.boxing.price", new BigDecimal("20.00"));

        String title2 = "Sala Pesi";
        String description2 = "Sessione privata di Sala Pesi con Personal Trainer";
        BigDecimal price2 = PriceConfig.getPrice("training.weight.price", new BigDecimal("18.00"));

        Training demoTraining1 = new Training();
        demoTraining1.setPersonalTrainer(pt1);
        demoTraining1.setName(title1);
        demoTraining1.setDescription(description1);
        demoTraining1.setBasePrice(price1);

        Training demoTraining2 = new Training();
        demoTraining2.setPersonalTrainer(pt2);
        demoTraining2.setName(title2);
        demoTraining2.setDescription(description2);
        demoTraining2.setBasePrice(price2);

        this.trainings.add(demoTraining1);
        this.trainings.add(demoTraining2);
    }
}
