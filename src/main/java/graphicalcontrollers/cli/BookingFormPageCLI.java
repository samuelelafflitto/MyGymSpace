package graphicalcontrollers.cli;

import beans.BookingRecapBean;
import beans.SelectedDateBean;
import beans.SelectedSlotAndExtraBean;
import controllers.BookingController;
import exceptions.*;
import utils.PriceConfig;
import utils.session.BookingSession;
import utils.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookingFormPageCLI {
    private static final String INVALIDINPUT = "Opzione selezionata non valida. Riprovare";
    private static final String SEPARATOR = "------------------------";
    BookingController bController = new BookingController();
    AthleteMenuCLI athleteMenuCLI = new AthleteMenuCLI();
    private final Scanner sc = new Scanner(System.in);

    String enteredDate;
    SelectedDateBean selectedD;
    List<String> slots = new ArrayList<>();
    int selectedSlot;
//    boolean towel;
//    boolean sauna;
//    boolean energizer;
//    boolean video;
    SelectedSlotAndExtraBean slotAndExtraBean;
    BookingRecapBean recapBean;

    private static final int MAX_ATTEMPTS = 3;

    public void start() {

        while (true) {
            System.out.println(SEPARATOR);
            System.out.println("1) Inserisci la Data");
            System.out.println("2) Indietro");
            System.out.println("3) Torna alla Homepage");
            System.out.println("4) Logout");
            System.out.println("--> ");

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

                        // Raccolta time slot selezionato
                        selectTimeSlot();

                        // Raccolta opzioni extra
                        selectExtraOptions();

                        goToRecapPage();

//                        //Mostra un recap della prenotazione
//                        showRecap();
//
//                        // Richiesta all'utente se salvare o annullare la prenotazione
//                        getConfirmation();
                    } else {
                        clearBookingSessionIfBookingFailed();
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
                athleteMenuCLI.goToHome();
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
                onDateSelection(selectedD);
                return true;
            } catch (DateException e) {
                attempts++;
                e.handleException();
                if(attempts < MAX_ATTEMPTS) {
                    bController.checkAttempts(attempts, MAX_ATTEMPTS);
                }
            } catch (InvalidDateFormatException e) {
                attempts++;
                e.handleException();
                if(attempts < MAX_ATTEMPTS) {
                    bController.checkAttempts(attempts, MAX_ATTEMPTS);
                }
            }
        }
        return false;
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

            for (String slot : slots) {
                System.out.println("1) " + slot);
            }

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
                selectedSlot = sc.nextInt();
                slotAndExtraBean.setSelectedSlot(selectedSlot);
            } catch (InvalidTimeSlotException e) {
                attempts++;
                e.handleException();
                if(attempts < MAX_ATTEMPTS) {
                    bController.checkAttempts(attempts, MAX_ATTEMPTS);
                }
            }
        }
    }

    private void selectExtraOptions() {
        int attempts1 = 0;
        int attempts2 = 0;
        int attempts3 = 0;
        int attempts4 = 0;

        System.out.println("\nIndicare se si vogliono aggiungere le seguenti opzioni extra o no (y/n)");

        try {
            while(attempts1 < MAX_ATTEMPTS) {
                try {
                    System.out.println(PriceConfig.getExtraName("towel") + " (" + PriceConfig.getExtraPrice("towel") + ")" + ": ");
                    String extraSelection1 = sc.nextLine();
                    slotAndExtraBean.setTowel(extraSelection1);  // Può lanciare InvalidSelectionException
                } catch (InvalidSelectionException e1) {
                    attempts1++;
                    e1.handleException();
                    attemptsErrorTypeCheck(attempts1);
                }
            }

            while(attempts2 < MAX_ATTEMPTS) {
                try {
                    System.out.println(PriceConfig.getExtraName("sauna") + " (" + PriceConfig.getExtraPrice("sauna") + ")" + ": ");
                    String extraSelection2 = sc.nextLine();
                    slotAndExtraBean.setSauna(extraSelection2);
                } catch (InvalidSelectionException e1) {
                    attempts2++;
                    e1.handleException();
                    attemptsErrorTypeCheck(attempts2);
                }
            }

            while(attempts3 < MAX_ATTEMPTS) {
                try {
                    System.out.println(PriceConfig.getExtraName("energizer") + " (" + PriceConfig.getExtraPrice("energizer") + ")" + ": ");
                    String extraSelection3 = sc.nextLine();
                    slotAndExtraBean.setEnergizer(extraSelection3);
                } catch (InvalidSelectionException e1) {
                    attempts3++;
                    e1.handleException();
                    attemptsErrorTypeCheck(attempts3);
                }
            }

            while(attempts4 < MAX_ATTEMPTS) {
                try {
                    System.out.println(PriceConfig.getExtraName("video") + " (" + PriceConfig.getExtraPrice("video") + ")" + ": ");
                    String extraSelection4 = sc.nextLine();
                    slotAndExtraBean.setVideo(extraSelection4);
                } catch (InvalidSelectionException e1) {
                    attempts4++;
                    e1.handleException();
                    attemptsErrorTypeCheck(attempts4);
                }
            }
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
        }

        bController.setBookingSessionBooking(slotAndExtraBean);
    }

    private void goToRecapPage() {
        new RecapPageCLI().start();
    }

    private void showRecap() {
        recapBean = bController.getBookingRecap(slotAndExtraBean);

    }

//    private void getConfirmation() {
//
//    }






    private void clearBookingSessionIfBookingFailed() {
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

    private void clearAndGoToHome() {
        clearBookingSessionIfBookingFailed();
        athleteMenuCLI.goToHome();
    }
}
