package graphicalcontrollers.cli;

import beans.BookingRecapBean;
import beans.ProfileDataBean;
import controllers.AuthController;
import controllers.BookingController;
import controllers.PersonalBookingsController;
import exceptions.FailedBookingCancellationException;
import exceptions.InvalidPasswordConfirmationException;
import exceptions.MissingDataException;
import exceptions.UserSearchFailedException;

import java.util.List;
import java.util.Scanner;

public class MyBookingsPageCLI {
    private static final String INVALIDINPUT = "Opzione selezionata non valida. Riprovare";
    private static final String SEPARATOR = "------------------------------------------------";
    AthleteMenuCLI athleteMenuCLI = new AthleteMenuCLI();
    private static final Scanner sc = new Scanner(System.in);

    AuthController authController = new AuthController();
    PersonalBookingsController pBController = new PersonalBookingsController();
    BookingController bController = new BookingController();

    public void start() {
        while (true) {
            System.out.println(SEPARATOR);
            System.out.println("1) Visualizza Prenotazioni Attive");
            System.out.println("2) Visualizza Prenotazioni Passate");
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
                System.out.print("\n" + SEPARATOR);
                showActiveBookings();
                break;
            case "2":
                System.out.print("\n" + SEPARATOR);
                System.out.println("\nELENCO PRENOTAZIONI PASSATE");
                showPastBookings();
                break;
            case "3":
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

    private void showActiveBookings() {
        List<BookingRecapBean> activeList = pBController.getActiveBookingsFromMap();

        System.out.println("\nELENCO PRENOTAZIONI ATTIVE");

        if(activeList.isEmpty()) {
            System.out.print(SEPARATOR);
            System.out.println("Nessuna prenotazione attiva");
        } else {
            showBookings(activeList);
        }
        System.out.println(SEPARATOR);
        System.out.print("1) Elimina una prenotazione");
        if(activeList.isEmpty()) {
            System.out.println(" (non disponibile)");
        } else {
            System.out.print("\n");
        }
        System.out.println("2) Visualizza Prenotazioni Passate");
        System.out.println("3) Torna alla Homepage");
        System.out.println("4) Logout");
        System.out.print("--> ");

        String internalChoice = sc.nextLine();

        switch (internalChoice) {
            case "1":
                if(!activeList.isEmpty()) {
                    handleDeleteBooking(activeList);
                } else {
                    System.out.println(INVALIDINPUT);
                }
                break;
            case "2":
                System.out.println("\n" + SEPARATOR);
                System.out.println("ELENCO PRENOTAZIONI PASSATE");
                showPastBookings();
                break;
            case "3":
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

    private void showPastBookings() {
        List<BookingRecapBean> pastList = pBController.getPastBookingsFromMap();
        showBookings(pastList);
    }

    private void showBookings(List<BookingRecapBean> bList) {
        int counter = 0;
        for(BookingRecapBean bean : bList) {
            System.out.println(SEPARATOR);
            System.out.println((counter + 1) + ") Allenamento: " + bean.getTrainingName() + " - PT: " + bean.getPtLastName());
            System.out.println("Atleta: " + bean.getAthCompleteName());
            System.out.println("Data e ora: " + bean.getDate() + ", " + bean.getStartTime());
            System.out.println("Extra selezionati: " + bean.getDescription());
            System.out.println("Costo totale: " + bean.getPrice() + "€");
            counter++;
        }
    }

    private void handleDeleteBooking(List<BookingRecapBean> activeList) {
        System.out.print("Inserisci il numero della prenotazione da eliminare: ");
        String input = sc.nextLine();

        try {
            int selection = Integer.parseInt(input);
            if(selection == 0) {
                return;
            }

            int index = selection - 1;

            if(index >= 0 && index < activeList.size()) {
                BookingRecapBean bean = activeList.get(index);

                deletionAttempt(bean);

            } else {
                System.out.print("Nessuna prenotazione presente a quel numero.");
                showActiveBookings();
            }
        } catch (FailedBookingCancellationException e) {
            e.handleException();
        } catch (NumberFormatException _) {
            System.out.println("Input non valido. Inserire un numero da 0 a " + activeList.size());
        }
    }

    private void deletionAttempt(BookingRecapBean bookingToDelete) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("              CONFERMA ELIMINAZIONE");
        System.out.println(SEPARATOR);

        System.out.print("Inserisci la tua password: ");
        String pass1 = sc.nextLine();

        System.out.print("Conferma la tua password: ");
        String pass2 = sc.nextLine();

        try {
            if(pass1 == null || pass2 == null) {
                throw new MissingDataException();
            }

            if(!pass1.equals(pass2)) {
                throw new InvalidPasswordConfirmationException();
            }

            ProfileDataBean bean = new ProfileDataBean();
            bean.setInputPassword(pass1);
            bean.setConfirmPassword(pass2);

            if(authController.isPasswordWrong(bean)) {
                throw new UserSearchFailedException();
            } else {
                if(bController.deleteBooking(bookingToDelete)) {
                    System.out.println("Eliminazione prenotazione riuscita!");
                    showActiveBookings();
                } else {
                    System.out.println("Errore in fase di eliminazione!");
                }
            }
        } catch (MissingDataException e) {
            e.handleException();
        } catch (InvalidPasswordConfirmationException e) {
            e.handleException();
        } catch (UserSearchFailedException e) {
            e.handleException();
        }
    }
}
