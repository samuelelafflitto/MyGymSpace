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
    private static final String INVALIDINPUT = "Invalid Option! Try again";
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
            System.out.println("         MYGYMSPACE - EDIT TRAINING PAGE");
            System.out.println(SEPARATOR);

            printTraining();

            System.out.println("1) Edit Description");
            System.out.println("2) Edit Base Price");
            System.out.println("3) Back to My Profile");
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
        System.out.println(" Training name:         " + pt.getTraining().getName());
        System.out.println(" Description:           " + pt.getTraining().getDescription());
        System.out.println(" Base Price (MAX 30):   " + pt.getTraining().getBasePrice());
        System.out.println(SEPARATOR);
    }

    private void changeDescription() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("                EDIT DESCRIPTION");
        System.out.println(SEPARATOR);

        System.out.print("Enter the new description: ");
        String newDescription = sc.nextLine();

        pt.getTraining().setDescription(newDescription);

        submitTrainingUpdate();
    }

    private void changeBasePrice() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("                 EDIT BASE PRICE");
        System.out.println(SEPARATOR);

        System.out.print("Enter the new base price: ");
        String stringBasePrice = sc.nextLine();

        if(stringBasePrice.trim().isEmpty()) {
            System.out.println("The base price slot cannot be empty!");
            return;
        }

        BigDecimal newBasePrice;
        String priceText = stringBasePrice.replace(",", ".");

        try {
            newBasePrice = new BigDecimal(priceText).setScale(2, RoundingMode.HALF_UP);

            if(newBasePrice.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("The base price cannot be negative!");
                return;
            }

            if(newBasePrice.compareTo(MAX_PRICE) > 0) {
                System.out.println("The base price exceeds the maximum allowed. Set to " + MAX_PRICE + "€");
                newBasePrice = MAX_PRICE;
            }

            pt.getTraining().setBasePrice(newBasePrice);

            submitTrainingUpdate();
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format " + e.getMessage());
        }
    }

    private void submitTrainingUpdate() {
        try {
            TrainingToEditBean bean = new TrainingToEditBean();

            bean.setName(pt.getTraining().getName());
            bean.setDescription(pt.getTraining().getDescription());
            bean.setBasePrice(pt.getTraining().getBasePrice());

            if(pController.updateTraining(bean)) {
                System.out.println("Successful editing!");
            } else {
                System.out.println("Edit failed!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
