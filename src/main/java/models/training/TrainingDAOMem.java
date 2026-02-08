package models.training;

import models.dailyschedule.DailyScheduleDAO;
import models.dao.factory.FactoryDAO;
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

        Training demoTraining1 = newDemoTraining(pt1, title1, description1, price1);

        Training demoTraining2 = newDemoTraining(pt2, title2, description2, price2);

        this.trainings.add(demoTraining1);
        this.trainings.add(demoTraining2);
    }

    @Override
    public void deleteDemoTraining(PersonalTrainer pt) {
        Training demoTraining = getTrainingByPT(pt);
        if(demoTraining != null) {
            DailyScheduleDAO dsDAO = FactoryDAO.getInstance().createDailyScheduleDAO();
            dsDAO.deleteDemoDailySchedule(demoTraining);
            trainings.removeIf(t -> t.getPersonalTrainer().equals(pt));
        }
    }

    @Override
    public void updateTrainingDetails(Training t) {
        for(Training training : trainings) {
            if(training.getPersonalTrainer().getUsername().equals(t.getPersonalTrainer().getUsername())) {
                training.setDescription(t.getDescription());
                training.setBasePrice(t.getBasePrice());

                return;
            }
        }
        System.out.println("No training found");
    }

    private Training newDemoTraining(PersonalTrainer pt, String title, String description, BigDecimal basePrice) {
        Training newT = new Training();
        newT.setPersonalTrainer(pt);
        newT.setName(title);
        newT.setDescription(description);
        newT.setBasePrice(basePrice);

        return newT;
    }
}
