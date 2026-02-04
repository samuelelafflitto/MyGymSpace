package graphicalcontrollers.cli;

import beans.TrainingToEditBean;
import controllers.ProfileController;
import models.user.PersonalTrainer;
import models.user.User;
import utils.session.SessionManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class EditTrainingPageCLI {
    private static final Scanner sc = new Scanner(System.in);
    private static final String INVALIDINPUT = "Opzione selezionata non valida. Riprovare";
    private static final String SEPARATOR = "------------------------------------------------";
    private static final String ATHLETE_TYPE = "ATH";
    private static final BigDecimal MAX_PRICE = new BigDecimal("30.00");

    ProfileController pController = new ProfileController();
    User user = SessionManager.getInstance().getLoggedUser();
    PersonalTrainer pt = (PersonalTrainer) user;
    String type = user.getType();

    public void start() {
        while(true) {
            System.out.println("\n" + SEPARATOR);
            System.out.println("       MYGYMSPACE - MODIFICA ALLENAMENTO");
            System.out.println(SEPARATOR);

            printTraining();

            System.out.println("1) Modifica Descrizione");
            System.out.println("2) Modifica Prezzo Base");
            System.out.println("3) Torna al Profilo");
            System.out.println("4) Logout ");
            System.out.print("--> ");
            String choice = sc.nextLine();

            handleChoice(choice);
        }
    }

    private void handleChoice(String choice) {
        switch (choice) {
            case "1":
                changeDescription();
                break;
            case "2":
                changeBasePrice();
                break;
            case "3":
                new MyProfilePageCLI().start();
                break;
            case "4":
                if(type.equals(ATHLETE_TYPE)) {
                    PersonalTrainerMenuCLI ptMenu = new PersonalTrainerMenuCLI();
                    ptMenu.logout();
                } else {
                    AthleteMenuCLI  athMenu = new AthleteMenuCLI();
                    athMenu.logout();
                }
                break;
            default:
                System.out.println(INVALIDINPUT);
                break;
        }
    }


    private void printTraining() {
        System.out.println(" Nome Allenamento:      " + pt.getTraining().getName());
        System.out.println(" Descrizione:           " + pt.getTraining().getDescription());
        System.out.println(" Prezzo base (MAX 30):  " + pt.getTraining().getBasePrice());
        System.out.println(SEPARATOR);
    }

    private void changeDescription() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("            MODIFICA LA DESCRIZIONE");
        System.out.println(SEPARATOR);

        System.out.print("Inserisci la nuova descrizione: ");
        String newDescription = sc.nextLine();

        pt.getTraining().setDescription(newDescription);

        submitTrainingUpdate();
    }

    private void changeBasePrice() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("            MODIFICA IL PREZZO BASE");
        System.out.println(SEPARATOR);

        System.out.print("Inserisci il nuovo prezzo: ");
        String stringBasePrice = sc.nextLine();

        if(stringBasePrice.trim().isEmpty()) {
            System.out.println("Il prezzo non può essere vuoto.");
            return;
        }

        BigDecimal newBasePrice;
        String priceText = stringBasePrice.replace(",", ".");

        try {
            newBasePrice = new BigDecimal(priceText).setScale(2, RoundingMode.HALF_UP);

            if(newBasePrice.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Il prezzo non può essere negativo!");
                return;
            }

            if(newBasePrice.compareTo(MAX_PRICE) > 0) {
                System.out.println("Il prezzo supera il massimo consentito. Impostato a " + MAX_PRICE + "€");
                newBasePrice = MAX_PRICE;
            }

            pt.getTraining().setBasePrice(newBasePrice);

            submitTrainingUpdate();
        } catch (NumberFormatException e) {
            System.out.println("Formato del prezzo non valido " + e.getMessage());
        }
    }

    private void submitTrainingUpdate() {
        try {
            TrainingToEditBean bean = new TrainingToEditBean();

            bean.setName(pt.getTraining().getName());
            bean.setDescription(pt.getTraining().getDescription());
            bean.setBasePrice(pt.getTraining().getBasePrice());

            if(pController.updateTraining(bean)) {
                System.out.println("Aggiornamento riuscito!");
            } else {
                System.out.println("Aggiornamento non riuscito!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
