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
    private static final String INVALIDINPUT = "Invalid Option! Try again";
    private static final String SEPARATOR = "------------------------------------------------";
    AthleteMenuCLI athleteMenuCLI = new AthleteMenuCLI();
    private static final Scanner sc = new Scanner(System.in);

    AuthController authController = new AuthController();
    PersonalBookingsController pBController = new PersonalBookingsController();
    BookingController bController = new BookingController();

    public void start() {
        while (true) {
            System.out.println(SEPARATOR);
            System.out.println("1) View Active Bookings");
            System.out.println("2) View Past Bookings");
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
                System.out.print("\n" + SEPARATOR);
                showActiveBookings();
                break;
            case "2":
                System.out.print("\n" + SEPARATOR);
                System.out.println("\nPAST BOOKINGS LIST");
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

        System.out.println("\nACTIVE BOOKINGS LIST");

        if(activeList.isEmpty()) {
            System.out.print(SEPARATOR);
            System.out.println("No active bookings found");
        } else {
            showBookings(activeList);
        }
        System.out.println(SEPARATOR);
        System.out.print("1) Delete a Booking");
        if(activeList.isEmpty()) {
            System.out.println(" (not available)");
        } else {
            System.out.print("\n");
        }
        System.out.println("2) View Past Booking");
        System.out.println("3) Back to Homepage");
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
                System.out.println("PAST BOOKINGS LIST");
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
            System.out.println((counter + 1) + ") Training: " + bean.getTrainingName() + " - PT: " + bean.getPtLastName());
            System.out.println("Athlete: " + bean.getAthCompleteName());
            System.out.println("Date and Hour: " + bean.getDate() + ", " + bean.getStartTime());
            System.out.println("Selected extra options: " + bean.getDescription());
            System.out.println("Final price: " + bean.getPrice() + "€");
            counter++;
        }
    }

    private void handleDeleteBooking(List<BookingRecapBean> activeList) {
        System.out.print("Enter the number of the booking to delete: ");
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
                System.out.print(INVALIDINPUT);
                showActiveBookings();
            }
        } catch (FailedBookingCancellationException e) {
            e.handleException();
        } catch (NumberFormatException _) {
            System.out.println(INVALIDINPUT + ". Enter a number from 0 to " + activeList.size());
        }
    }

    private void deletionAttempt(BookingRecapBean bookingToDelete) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("              DELETE CONFIRMATION");
        System.out.println(SEPARATOR);

        System.out.print("Enter your password: ");
        String pass1 = sc.nextLine();

        System.out.print("Confirm your password: ");
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
                    System.out.println("Booking successfully deleted!");
                    showActiveBookings();
                } else {
                    System.out.println("Booking deletion failed!");
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
