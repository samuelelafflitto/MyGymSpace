package graphicalcontrollers.cli;

import beans.AvailableTrainingBean;
import controllers.BookingController;
import exceptions.AttemptsErrorType;
import exceptions.AttemptsException;
import exceptions.TrainingsSearchFailedException;

import java.util.List;
import java.util.Scanner;

public class TrainingSelectionPageCLI {
    private static final String INVALIDINPUT = "Invalid Option! Try again";
    private static final String SEPARATOR = "------------------------------------------------";
    AthleteMenuCLI athleteMenuCLI = new AthleteMenuCLI();
    private static final Scanner sc = new Scanner(System.in);

    BookingController bController = new BookingController();

    private static final int MAX_ATTEMPTS = 3;

    public void start() {
        while (true) {
            System.out.println(SEPARATOR);
            System.out.println("1) Select a Training");
            System.out.println("2) Cancel");
            System.out.println("3) Back to Homepage");
            System.out.println("4) Logout");
            System.out.print("--> ");

            String choice = sc.nextLine();
            handleChoice(choice);
        }
    }

    private void handleChoice(String choice) {
        switch (choice) {
            case "1":
                try {
                    selectTraining();
                } catch (TrainingsSearchFailedException e) {
                    e.handleException();
                } catch (AttemptsException e) {
                    e.handleException();
                    athleteMenuCLI.goToHome();
                }
                break;
            case "2", "3":
                athleteMenuCLI.goToHome();
                break;
            case "4":
                athleteMenuCLI.logout();
                break;
            default:
                System.out.println(INVALIDINPUT);
                break;
        }
    }

    private void selectTraining() {
        List<AvailableTrainingBean> trainings = bController.getAvailableTrainings();

        if (trainings.isEmpty()) {
            throw new TrainingsSearchFailedException();
        }

        for(int i = 0; i < trainings.size(); i++) {
            AvailableTrainingBean training = trainings.get(i);
            showTrainingDetails(training, i + 1);
        }

        int attempts = 0;

        while(attempts < MAX_ATTEMPTS) {
            System.out.print("Select a Training from those displayed: ");

            int selectedTraining = Integer.parseInt(sc.nextLine()) - 1;

            if(selectedTraining >= 0 && selectedTraining < trainings.size()) {
                AvailableTrainingBean selectedT = trainings.get(selectedTraining);
                onSelection(selectedT);
            } else {
                attempts++;
                System.out.println(INVALIDINPUT + ". Enter a number from 1 to " + trainings.size());
                checkAttempts(attempts);
            }
        }
        // A tentativi terminati lancia eccezione
        throw new AttemptsException(AttemptsErrorType.MAX_ATTEMPTS_REACHED);
    }

    private void showTrainingDetails(AvailableTrainingBean tBean, int index) {
        System.out.println("\n" + index + ") " + tBean.getName());
        System.out.println("Personal Trainer: " +  tBean.getPtLastName());
        System.out.println("Description: " + tBean.getDescription());
        System.out.println("Base price: " + tBean.getBasePrice() + "€");
        System.out.println(SEPARATOR);
    }

    private void onSelection(AvailableTrainingBean bean) {
        bController.setBookingSessionTraining(bean);
        new BookingFormPageCLI().start();
    }

    private void checkAttempts(int currAttempt) {
        if(currAttempt < MAX_ATTEMPTS) {
            System.out.println("Remaining attempts: " + (MAX_ATTEMPTS - currAttempt));
        }
    }
}
