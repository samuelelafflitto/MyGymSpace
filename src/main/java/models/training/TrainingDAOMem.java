package models.training;

import models.dailyschedule.DailySchedule;
import models.user.PersonalTrainer;
import utils.PriceConfig;

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

    @Override
    public void updateDailySchedule(PersonalTrainer pt, DailySchedule ds) {
        if(ds != null) {
            System.out.println("[MEM] Simulazione salvataggio orari per PT: " + pt.getUsername());
            System.out.println("Data: " + ds.getDate() + " -> Bitmask aggiornata: " + ds.getTimeSlotBits());
        }
    }


    private void initializeDemoData() {
        PersonalTrainer pt = new PersonalTrainer("Mario", "Rossi", "trainer1", "pass1", "Personal Trainer");

        String title = "Box";
        String description = "Private Box Training Session with Personal Trainer";
        double price = PriceConfig.getPrice("training.boxing.price", 20.00);

        Training demoTraining = new Training(title, description, pt, price);
        this.trainings.add(demoTraining);
    }
}
