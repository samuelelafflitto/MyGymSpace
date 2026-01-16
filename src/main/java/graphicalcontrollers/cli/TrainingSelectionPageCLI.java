package graphicalcontrollers.cli;

import beans.AvailableTrainingBean;
import controllers.BookingController;
import exceptions.AttemptsErrorType;
import exceptions.AttemptsException;
import exceptions.TrainingsSearchFailedException;

import java.util.List;
import java.util.Scanner;

public class TrainingSelectionPageCLI {
    private static final String INVALIDINPUT = "Opzione selezionata non valida. Riprovare";
    private static final String SEPARATOR = "------------------------";
//    BookingController bController = new BookingController();
    AthleteMenuCLI athleteMenuCLI = new AthleteMenuCLI();
    private static final Scanner sc = new Scanner(System.in);

    private static final int MAX_ATTEMPTS = 3;

    public void start() {
        while (true) {
            System.out.println(SEPARATOR);
            System.out.println("1) Seleziona Allenamento");
            System.out.println("2) Annulla prenotazione");
            System.out.println("3) Torna alla Homepage");
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
                    // Gestione eccezione "non ci sono allenamenti"
                    e.handleException();
                } catch (AttemptsException e) {
                    // Gestione eccezione "tentativi terminati"
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


//    private void goToBookingForm() {
//        new BookingFormPageCLI().start();
//    }

    private void selectTraining() {
        BookingController bController = new BookingController();
        List<AvailableTrainingBean> trainings = bController.getAvailableTrainings();

        // Controlla se la lista ottenuta è vuota
        if (trainings.isEmpty()) {
            throw new TrainingsSearchFailedException();
        }

        // Viene mostrata la lista
//        System.out.println(SEPARATOR);
        for(int i = 0; i < trainings.size(); i++) {
            AvailableTrainingBean training = trainings.get(i);
            showTrainingDetails(training, i + 1);
        }

        // Per gestione tentativi effettuati e massimi
        int attempts = 0;

        while(attempts < MAX_ATTEMPTS) {
            System.out.print("Selezionare un allenamento tra quelli visualizzati: ");
            // Raccoglie indice dell'allenamento da selezionare
            int selectedTraining = Integer.parseInt(sc.nextLine()) - 1;

//            int input = sc.nextInt();
//            int selectedTrainingIndex = input - 1;

            // Controlla se l'indice inserito rientra nel range della lista
            if(selectedTraining >= 0 && selectedTraining < trainings.size()) {
                // Se si, ricrea il Bean con l'allenamento selezionato
                AvailableTrainingBean selectedT = trainings.get(selectedTraining);
                // Setta la BookingSession con il Training
                onSelection(selectedT);
            } else {
                attempts++;
                System.out.println("Selezione non valida. Inserire un numero tra 1 e " + trainings.size());
                // Controlla quanti tentativi mancano o se sono terminati
                checkAttempts(attempts);
            }
        }
        // A tentativi terminati lancia eccezione
        throw new AttemptsException(AttemptsErrorType.MAX_ATTEMPTS_REACHED);
    }

    private void showTrainingDetails(AvailableTrainingBean tBean, int index) {
        System.out.println("\n" + index + ") " + tBean.getName());
        System.out.println("Personal Trainer: " +  tBean.getPersonalTrainer());
        System.out.println("Descrizione: " + tBean.getDescription());
        System.out.println("Prezzo base: " + tBean.getBasePrice() + "€");
        System.out.println(SEPARATOR);
    }

    private void onSelection(AvailableTrainingBean bean) {
        BookingController bController = new BookingController();
        bController.setBookingSessionTraining(bean);
        new BookingFormPageCLI().start();
    }

    private void checkAttempts(int currAttempt) {
        if(currAttempt < MAX_ATTEMPTS) {
            System.out.println("Tentativi rimasti: " + (MAX_ATTEMPTS - currAttempt));
        }
    }
}
