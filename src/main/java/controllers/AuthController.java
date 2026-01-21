package controllers;

import beans.LoginBean;
import beans.SignupBean;
import exceptions.DataLoadException;
import exceptions.ExistingUserException;
import exceptions.UserSearchFailedException;
import models.booking.BookingDAO;
import models.booking.BookingInterface;
import models.booking.ConcreteBooking;
import models.booking.record.BasicBookingDataFromPersistence;
import models.booking.record.BookingDataWithTraining;
import models.booking.record.BookingDataWithUsers;
import models.booking.record.FinalBookingData;
import models.dailyschedule.DailySchedule;
import models.dailyschedule.DailyScheduleDAO;
import models.dao.factory.FactoryDAO;
import models.training.Training;
import models.training.TrainingDAO;
import models.user.Athlete;
import models.user.PersonalTrainer;
import models.user.User;
import models.user.UserDAO;
import utils.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthController {
    private static final String ATHLETE_TYPE = "ATH";
    private static final String PT_TYPE = "PT";

    public AuthController() {// Il costruttore non ha bisogno di parametri
    }

    public boolean authUser(LoginBean loginBean) {
        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
        String username = loginBean.getUsername();
        String password = loginBean.getPassword();

        try {
            User userFromPersistence = userDAO.getUser(username, password);

            if(userFromPersistence != null) {
                User user = populateUser(userFromPersistence);
                SessionManager.getInstance().setLoggedUser(user);
                return true;
            } else {
                throw new UserSearchFailedException();
            }
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean registerUser(SignupBean signupBean) {
        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
        String firstName = signupBean.getFirstName();
        String lastName = signupBean.getLastName();
        String username = signupBean.getUsername();
        String password = signupBean.getPassword();
        Athlete user;

        try {
            User existingUser = userDAO.getUserByUsername(username);
            if(existingUser == null) {
                throw new ExistingUserException();
            } else {
                user = new Athlete(username, password, firstName, lastName, ATHLETE_TYPE);
                userDAO.addUser(username, user);
                SessionManager.getInstance().setLoggedUser(user);
                return true;
            }

        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Associa tutto il necessario all'Utente loggato
    private User populateUser(User user) {
        // Se user è ATHLETE
        if (user.getType().equals(ATHLETE_TYPE)) {
            List<BookingInterface> bookings = new ArrayList<>();

            try {
                bookings = getBookingByUser(user);
            } catch (DataLoadException e) {
                System.out.println(e.getMessage());
            }
            ( (Athlete) user).setBookings(bookings);
        } else if (user.getType().equals(PT_TYPE)) {
            List<BookingInterface> privateSessions = new ArrayList<>();

            TrainingDAO trainingDAO = FactoryDAO.getInstance().createTrainingDAO();
            Training training = null;

            try {
                privateSessions = getBookingByUser(user);
                training = trainingDAO.getTrainingByPT((PersonalTrainer)user);
                training.setPersonalTrainer((PersonalTrainer)user);
            } catch (DataLoadException e) {
                System.out.println(e.getMessage());
            }
            ( (PersonalTrainer) user).setPrivateSessions(privateSessions);
            ( (PersonalTrainer) user).setTraining(training);
        }
        return user;
    }





    private List<BookingInterface> getBookingByUser(User user) {
        // BookingDAO
        BookingDAO bookingDAO = FactoryDAO.getInstance().createBookingDAO();
        List<BasicBookingDataFromPersistence> dbRecordsA = bookingDAO.fetchBasicBookingData(user);

        // UserDAO
        List<BookingDataWithUsers> dbRecordsB = enrichWithUsers(dbRecordsA, user);
        // TrainingDAO
        List<BookingDataWithTraining> dbRecordsC = enrichWithTraining(dbRecordsB);
        // DailyScheduledDAO
        List<FinalBookingData> dbFinalRecords = enrichWithDailySchedules(dbRecordsC);

        return createFinalBookings(dbFinalRecords);
    }

    private List<BookingDataWithUsers> enrichWithUsers(List<BasicBookingDataFromPersistence> baseRecords, User user) {
        List<BookingDataWithUsers> enrichedRecords = new ArrayList<>();
        Map<String, User> userCache = new HashMap<>();

        for(BasicBookingDataFromPersistence element : baseRecords) {
            Athlete athlete;
            PersonalTrainer pt;

            if(user.getType().equals(ATHLETE_TYPE) && element.athleteUsername().equals(user.getUsername())) {
                athlete = (Athlete) user;
            } else {
                UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
                athlete = (Athlete) userDAO.fetchUserFromPersistence(element.athleteUsername(), ATHLETE_TYPE, userCache);
            }

            if(user.getType().equals(PT_TYPE) && element.ptUsername().equals(user.getUsername())) {
                pt = (PersonalTrainer) user;
            } else {
                UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
                pt = (PersonalTrainer) userDAO.fetchUserFromPersistence(element.ptUsername(), PT_TYPE, userCache);
            }

            if(athlete != null && pt != null) {
                BookingDataWithUsers newR = new BookingDataWithUsers(athlete, pt, element);
                enrichedRecords.add(newR);
            }
        }
        return enrichedRecords;
    }

    private List<BookingDataWithTraining> enrichWithTraining(List<BookingDataWithUsers> baseRecords) {
        List<BookingDataWithTraining> enrichedRecords = new ArrayList<>();
        Map<String, Training> trainingCache = new HashMap<>();

        for(BookingDataWithUsers element : baseRecords) {
            PersonalTrainer pt = element.pt();
            String ptUsername = pt.getUsername();

            Training training = trainingCache.get(ptUsername);

            if(training == null) {
                TrainingDAO trainingDAO = FactoryDAO.getInstance().createTrainingDAO();
                training = trainingDAO.getTrainingByPT(pt);

                if(training != null) {
                    trainingCache.put(ptUsername, training);
                }
            }
            BookingDataWithTraining newR = new BookingDataWithTraining(element.athlete(), training, element.previousRecord());
            enrichedRecords.add(newR);
        }
        return enrichedRecords;
    }

    private List<FinalBookingData> enrichWithDailySchedules(List<BookingDataWithTraining> baseRecords) {
        List<FinalBookingData> finalRecords = new ArrayList<>();
        Map<String, List<DailySchedule>> dailyScheduleCache = new HashMap<>();

        for(BookingDataWithTraining element : baseRecords) {
            String ptUsername = element.training().getPersonalTrainer().getUsername();
            List<DailySchedule> schedules = dailyScheduleCache.get(ptUsername);

            if(schedules == null) {
                DailyScheduleDAO dailyScheduleDAO = FactoryDAO.getInstance().createDailyScheduleDAO();
                schedules = dailyScheduleDAO.getSchedulesByTraining(element.training());
                dailyScheduleCache.put(ptUsername, schedules);
            }
            element.training().setSchedules(schedules);

            DailySchedule day = null;
            for(DailySchedule s : schedules) {
                if(s.getDate().equals(element.record().date())) {
                    day = s;
                    break;
                }
            }
            FinalBookingData newR = new FinalBookingData(element.athlete(), element.training(), day, element.record());
            finalRecords.add(newR);
        }
        return finalRecords;
    }

    private List<BookingInterface> createFinalBookings(List<FinalBookingData> records) {
        List<BookingInterface> bookings = new ArrayList<>();

        for(FinalBookingData element: records) {
            ConcreteBooking booking = new ConcreteBooking();

            booking.setAthlete(element.athlete());
            booking.setTraining(element.training());
            booking.setDailySchedule(element.dailySchedule());
            booking.setSelectedSlot(element.previousRecord().selectedSlot());
            booking.setDescription(element.previousRecord().description());
            booking.setFinalPrice(element.previousRecord().finalPrice());

            bookings.add(booking);
        }
        return bookings;
    }
}
