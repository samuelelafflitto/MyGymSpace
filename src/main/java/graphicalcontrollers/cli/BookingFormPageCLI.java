package graphicalcontrollers.cli;

import beans.SelectedDateBean;
import beans.SelectedSlotAndExtraBean;
import beans.SelectedTrainingBean;
import controllers.BookingController;
import exceptions.*;
import utils.PriceConfig;
import utils.session.BookingSession;
import utils.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class BookingFormPageCLI {
    private static final Scanner sc = new Scanner(System.in);
    private static final String INVALIDINPUT = "Opzione selezionata non valida. Riprovare";
    private static final String SEPARATOR = "------------------------------------------------";
    BookingController bController = new BookingController();
    AthleteMenuCLI athleteMenuCLI = new AthleteMenuCLI();

    String enteredDate;
    SelectedDateBean selectedD;
    List<String> slots = new ArrayList<>();
    int selectedSlot;
    SelectedSlotAndExtraBean slotAndExtraBean;

    private static final int MAX_ATTEMPTS = 3;
    public void start() {
        SelectedTrainingBean selectedTraining = bController.getSelectedTraining();
        System.out.println("\nAllenamento selezionato: " + selectedTraining.getName());

        while(true) {
            System.out.println(SEPARATOR);
            System.out.println("1) Inserisci la Data");
            System.out.println("2) Indietro");
            System.out.println("3) Torna alla Homepage");
            System.out.println("4) Logout");
            System.out.print("--> ");

            String choice = sc.nextLine();
            handleChoice(choice);
        }
    }

    private void handleChoice(String choice) {
        switch (choice) {
            // Inserisci la Data
            case "1":
                try {
                    if(selectDate()) {
                        // Mostra i time slot disponibili
                        showTimeSlots();
                    } else {
                        clearBookingSession();
                        athleteMenuCLI.goToHome();
                    }
                } catch (TrainingsSearchFailedException e) {
                    // Gestione eccezione "non ci sono allenamenti"
                    e.handleException();
                } catch (AttemptsException e) {
                    // Gestione eccezione "tentativi terminati"
                    e.handleException();

                    // Fallimento causa tentativi terminati, ritorno alla Homepage
                    isAttemptsTeminated(e);
                }
                break;
            // Indietro
            case "2":
                clearAndGoBack();
                break;
            // Torna alla Homepage (annulla la prenotazione)
            case "3":
                clearAndGoToHome();
                break;
            // Logout
            case "4":
                athleteMenuCLI.logout();
                break;
            default:
                System.out.println(INVALIDINPUT);
                break;
        }
    }





    private boolean selectDate() {
        int attempts = 0;

        while(attempts < MAX_ATTEMPTS) {
            System.out.println("\nInserire la data in cui si intende effettuare la prenotazione (YYYY-MM-DD): ");

            try {
                enteredDate = sc.nextLine();
                selectedD = new SelectedDateBean(enteredDate);
                // Se arriviamo qui il formato della data è giusto
                if(bController.isPastDate(selectedD.getSelectedDate())) {
                    throw new DateException(DateErrorType.PAST_DATE);
                }
                if(bController.isHoliday(selectedD.getSelectedDate())) {
                    throw new DateException(DateErrorType.HOLIDAY);
                }
                onDateSelection(selectedD);
                return true;
            } catch (DateException e) {
                attempts++;
                e.handleException();

                internalCheck(attempts);

            } catch (InvalidDateFormatException e) {
                attempts++;
                e.handleException();

                internalCheck(attempts);

            } catch (AttemptsException e) {
                e.handleException();
            }
        }
        return false;
    }

    private void internalCheck(int attempts) {
        try {
            bController.checkAttempts(attempts, MAX_ATTEMPTS);
        } catch (AttemptsException e1) {
            if(e1.getErrorType() == AttemptsErrorType.MAX_ATTEMPTS_REACHED) {
                throw e1;
            }
            e1.handleException();
        }
    }

    private void onDateSelection(SelectedDateBean selectedDate) {
        bController.setBookingSessionDate(selectedDate);
    }


    private void showTimeSlots() {
        slots.clear();

        try {
            slots = bController.getAvailableSlots();

            System.out.println(SEPARATOR);
            System.out.println("TIME SLOT DISPONIBILI per il giorno " + selectedD.getSelectedDate().toString());
            System.out.println(SEPARATOR);

            for(int i = 0; i < slots.size(); i++) {
                System.out.println((i+1) + ") " + slots.get(i));
            }

            selectTimeSlot();

        } catch (DateException e) {
            e.handleException();
        }
    }


    private void selectTimeSlot() {
        int attempts = 0;

        while(attempts < MAX_ATTEMPTS) {
            System.out.println("\nSelezionare uno tra gli slot disponibili: ");

            try {
                slotAndExtraBean = new SelectedSlotAndExtraBean();
                // Raccoglie input
                int input = sc.nextInt();
                //Risale a orario di inizio slot
                String hourSelected = slots.get(input - 1);
                //Ricava slot orario
                selectedSlot = Integer.parseInt(hourSelected.split(":")[0]);

                slotAndExtraBean.setSelectedSlot(selectedSlot);
            } catch (InvalidTimeSlotException e) {
                attempts++;
                e.handleException();
                if(attempts < MAX_ATTEMPTS) {
                    bController.checkAttempts(attempts, MAX_ATTEMPTS);
                }
            }

            selectExtraOptions();
        }
    }

    private void selectExtraOptions() {

        if(sc.hasNextLine()) {
            sc.nextLine();
        }

        System.out.println("\nIndicare se si vogliono aggiungere le seguenti opzioni extra o no (y/n)");

        try {
            askExtra("towel", slotAndExtraBean::setTowel);
            askExtra("sauna", slotAndExtraBean::setSauna);
            askExtra("energizer", slotAndExtraBean::setEnergizer);
            askExtra("video", slotAndExtraBean::setVideo);

        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
        }

        bController.setBookingSessionBooking(slotAndExtraBean);

        try {
            if(!bController.checkSameDateSameTimeBooking()) {
                goToRecapPage();
            }
        } catch (SameDateSameTimeException e) {
            e.handleException();
            new BookingFormPageCLI().start();
        }
    }

    private void goToRecapPage() {
        new RecapPageCLI().start();
    }

    // Utilizzo di un setter per richiamare le varie opzioni extra con un unico metodo privato
    private void askExtra(String extraKey, Consumer<String> setter) {
        int localAttempts = 0;

        while(localAttempts < MAX_ATTEMPTS) {
            try {
                System.out.println(PriceConfig.getExtraName(extraKey) + " (+" + PriceConfig.getExtraPrice(extraKey) + "€)" + ": ");
                String extraSelection = sc.nextLine();

                setter.accept(extraSelection);
                break;
            } catch (InvalidSelectionException e) {
                localAttempts++;
                e.handleException();
                attemptsErrorTypeCheck(localAttempts);
            }
        }
    }






    private void clearBookingSession() {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();
        bSession.clearBookingSession();
    }

    private void attemptsErrorTypeCheck(int attempts) {
        try {
            bController.checkAttempts(attempts, MAX_ATTEMPTS);
        } catch (AttemptsException e2) {
            e2.handleException();

            isAttemptsTeminated(e2);
        }
    }

    private void isAttemptsTeminated(AttemptsException e) {
        if(e.getErrorType() == AttemptsErrorType.MAX_ATTEMPTS_REACHED) {
            clearAndGoToHome();
        }
    }



    private void clearAndGoBack() {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();
        bSession.clearBookingSession();
        new TrainingSelectionPageCLI().start();
    }

    private void clearAndGoToHome() {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();
        bSession.clearBookingSession();
        athleteMenuCLI.goToHome();
    }
}
