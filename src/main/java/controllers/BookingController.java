package controllers;

import beans.*;
import exceptions.*;
import models.booking.*;
import models.booking.record.BookingKey;
import models.dailyschedule.DailySchedule;
import models.dailyschedule.DailyScheduleDAO;
import models.dao.factory.FactoryDAO;
import models.training.Training;
import models.user.*;
import utils.HolidayChecker;
import utils.ScheduleConfig;
import utils.session.BookingSession;
import utils.session.SessionManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingController {
    private static final String ATHLETE_TYPE = "ATH";
    private static final String PT_TYPE = "PT";

    public BookingController() {// The constructor does not need parameters
    }

    // Check that the BookingSession is open
    public boolean isBookingSessionOpen() {
        return SessionManager.getInstance().getBookingSession() != null;
    }

    // Provides the list of available Trainings
    public List<AvailableTrainingBean> getAvailableTrainings() {
        List<AvailableTrainingBean> trainingBeans = new ArrayList<>();
        List<Training> trainings = new ArrayList<>();

        try {
            trainings = FactoryDAO.getInstance().createTrainingDAO().getAvailableTrainings();
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
        }

        for(Training t: trainings) {
            AvailableTrainingBean tBean = new AvailableTrainingBean();
            tBean.setName(t.getName());
            tBean.setDescription(t.getDescription());
            tBean.setBasePrice(t.getBasePrice());
            tBean.setPersonalTrainer(t.getPersonalTrainer().getUsername());
            tBean.setPtLastName(t.getPersonalTrainer().getLastName());

            trainingBeans.add(tBean);
        }
        return trainingBeans;
    }

    // Check the selected Training and create a new related BookingSession
    public void setBookingSessionTraining(AvailableTrainingBean selectedTrainingBean) {
        List<Training> trainings = new ArrayList<>();

        try {
            trainings = FactoryDAO.getInstance().createTrainingDAO().getAvailableTrainings();
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
        }

        for(Training t: trainings) {
            if(t.getName().equals(selectedTrainingBean.getName())) {
                BookingSession bSession = new BookingSession(t);
                SessionManager.getInstance().createBookingSession(bSession);
            }
        }
    }

    // Obtaining/Creating the DailySchedule for the selected date and saving it in the BookingSession
    public void setBookingSessionDate(SelectedDateBean selectedDateBean) {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();

        DailySchedule dailySchedule = getDailySchedule(selectedDateBean);

        bSession.setDailySchedule(dailySchedule);
    }

    // Creating Booking and saving in the BookingSession
    public void setBookingSessionBooking(SelectedSlotAndExtraBean selectedSlotAndExtraBean) {
        createBooking(selectedSlotAndExtraBean);
    }

//    // OTTENIMENTO ALLENAMENTO SELEZIONATO E INSERIMENTO IN UN BEAN - INUTILE
//    public SelectedTrainingBean getSelectedTraining() {
//        BookingSession bSession = SessionManager.getInstance().getBookingSession();
//        String selectedTraining = bSession.getTraining().getName();
//        PersonalTrainer personalTrainer = bSession.getTraining().getPersonalTrainer();
//        return new SelectedTrainingBean(selectedTraining, personalTrainer);
//    }

    // Obtaining available time slots for Training and the selected Date
    public List<String> getAvailableSlots() throws AttemptsException {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();

        DailySchedule dailySchedule = bSession.getDailySchedule();
        StringBuilder timeSlots = dailySchedule.getTimeSlots();

        List<String> availableSlots = new ArrayList<>();

        LocalDate selectedDate = dailySchedule.getDate();
        LocalDate today = LocalDate.now();

        boolean isToday = selectedDate.equals(today);
        int currentHour = LocalTime.now().getHour();

        // Morning shifts
        LocalTime morningStart = ScheduleConfig.getTime("schedule.morning.start", "09:00");
        LocalTime morningEnd = ScheduleConfig.getTime("schedule.morning.end", "13:00");

        // Afternoon shifts
        LocalTime afternoonStart = ScheduleConfig.getTime("schedule.afternoon.start", "15:00");
        LocalTime afternoonEnd = ScheduleConfig.getTime("schedule.afternoon.end", "20:00");

        // Number of slots available morning and afternoon
        int mSlot = (int) ChronoUnit.HOURS.between(morningStart, morningEnd);
        int aSlot = (int) ChronoUnit.HOURS.between(afternoonStart, afternoonEnd);

        // Calculation of indices relating to the first slot in the morning and the first slot in the afternoon
        int morningStartIndex = Integer.parseInt(morningStart.toString().split(":")[0]);
        int afternoonStartIndex = Integer.parseInt(afternoonStart.toString().split(":")[0]);

        addFreeTimeSlotsToList(timeSlots, availableSlots, isToday, currentHour, mSlot, morningStartIndex);
        addFreeTimeSlotsToList(timeSlots, availableSlots, isToday, currentHour, aSlot, afternoonStartIndex);

        if(availableSlots.isEmpty()) {
            throw new DateException(DateErrorType.FULL_SCHEDULE);
        }
        return availableSlots;
    }

    // Calculation of free (morning and afternoon) time slots and adding them to the availableSlots list
    private void addFreeTimeSlotsToList(StringBuilder timeSlots, List<String> availableSlots, boolean isToday, int currentHour, int mSlot, int morningStartIndex) {
        for(int i = morningStartIndex; i < (morningStartIndex + mSlot); i++) {
            if(timeSlots.charAt(i) == '0') {
                if(isToday && i <= currentHour) {
                    continue;
                }
                String hLabel = i + ":00";
                availableSlots.add(hLabel);
            }
        }
    }

    // Check on other Bookings on the same date and time for the logged User
    public boolean checkSameDateSameTimeBooking() {
        BookingInterface bookingInProgress = SessionManager.getInstance().getBookingSession().getBooking();
        User user = SessionManager.getInstance().getLoggedUser();

        Map<BookingKey, BookingInterface> bookingsMap = getBookingsMap(user);

        if(bookingsMap == null || bookingsMap.isEmpty()) {
            return true;
        }

        for(BookingInterface b : bookingsMap.values()) {
            boolean sameDate = b.getDailySchedule().getDate().equals(bookingInProgress.getDailySchedule().getDate());
            boolean sameTime = b.getSelectedSlot().equals(bookingInProgress.getSelectedSlot());

            if(sameDate && sameTime) {
                throw new SameDateSameTimeException();
            }
        }
        return true;
    }

    // Obtaining Booking info to display in the Booking Recap
    public BookingRecapBean getBookingRecap() {

        BookingSession bSession = SessionManager.getInstance().getBookingSession();

        BookingInterface currentBooking = bSession.getBooking();

        BookingRecapBean bean = new BookingRecapBean();
        bean.setTrainingName(currentBooking.getTraining().getName());
        bean.setPtTraining(currentBooking.getTraining().getPersonalTrainer().getUsername());
        bean.setPtLastName(currentBooking.getTraining().getPersonalTrainer().getLastName());
        bean.setAthCompleteName(currentBooking.getAthlete().getFirstName() + " " + currentBooking.getAthlete().getLastName());
        bean.setDate(currentBooking.getDailySchedule().getDate());
        bean.setStartTime(currentBooking.getSelectedSlot());
        bean.setDescription(currentBooking.getDescription());
        bean.setPrice(currentBooking.getFinalPrice());

        return bean;
    }

    // Saving the Booking
    public boolean saveBooking() {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();

        BookingInterface finalBooking = bSession.getBooking();

        updateDailySchedule();

        BookingDAO bDAO = FactoryDAO.getInstance().createBookingDAO();

        try {
            bDAO.saveBooking(finalBooking);
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
        }

        // Adding the Booking to the logged User's Booking list
        User loggedUser = SessionManager.getInstance().getLoggedUser();
        ((Athlete) loggedUser).addBooking(finalBooking);

        return true;
    }

    // Booking deletion
    public boolean deleteBooking(BookingRecapBean recapBean) {
        try {
            String athleteUsername = SessionManager.getInstance().getLoggedUser().getUsername();
            String ptUsername = recapBean.getPtTraining();
            LocalDate date = recapBean.getDate();
            LocalTime startTime = recapBean.getStartTime();

            BookingDAO bDAO = FactoryDAO.getInstance().createBookingDAO();
            bDAO.deleteBooking(athleteUsername, ptUsername, date, startTime);

            User loggedUser = SessionManager.getInstance().getLoggedUser();
            BookingKey keyBookingToRemove = new BookingKey(ptUsername, date, startTime);

            if (loggedUser.getType().equals(ATHLETE_TYPE)) {
                Athlete ath = (Athlete) loggedUser;

                BookingInterface b = ath.getBookings().get(keyBookingToRemove);
                if(b != null) {
                    int slotIndex = startTime.getHour();
                    b.getDailySchedule().getTimeSlots().setCharAt(slotIndex, '0');
                }
                ath.getBookings().remove(keyBookingToRemove);
            } else if (loggedUser.getType().equals(PT_TYPE)) {
                PersonalTrainer pt = (PersonalTrainer) loggedUser;

                BookingInterface b = pt.getPrivateSessions().get(keyBookingToRemove);
                if(b != null) {
                    int slotIndex = startTime.getHour();
                    b.getDailySchedule().getTimeSlots().setCharAt(slotIndex, '0');
                }
                pt.getPrivateSessions().remove(keyBookingToRemove);
            }

            return true;
        } catch (FailedBookingCancellationException e) {
            e.handleException();
            return false;
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Attempts control (for CLI mode)
    public void checkAttempts(int currAttempt, int maxAttempts){
        if(currAttempt < maxAttempts) {
            throw new AttemptsException(AttemptsErrorType.REMAINING, (maxAttempts - currAttempt));
        }
        throw new AttemptsException(AttemptsErrorType.MAX_ATTEMPTS_REACHED);
    }


    // Date check
    public boolean isPastDate(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    public boolean isHoliday(LocalDate date) {
        HolidayChecker hc = new  HolidayChecker();
        return hc.isClosedForHoliday(date);
    }

    public Map<BookingKey, BookingInterface> getBookingsMap(User user) {
        Map<BookingKey, BookingInterface> bookingsMap = new HashMap<>();

        if(user.getType().equals(ATHLETE_TYPE)) {
            bookingsMap = ((Athlete)user).getBookings();
        } else if(user.getType().equals(PT_TYPE)) {
            bookingsMap = ((PersonalTrainer)user).getPrivateSessions();
        }

        return bookingsMap;
    }


    // PRIVATE OPERATIONS
    // Obtaining the selected date and saving it to the BookingSession
    private void setSelectedDate(SelectedDateBean dateBean) {
        LocalDate selectedDate = dateBean.getSelectedDate();
        BookingSession currentBookingSession =  SessionManager.getInstance().getBookingSession();

        if(currentBookingSession == null) {
            throw new DataLoadException("ERROR: Booking session not initialized. Return to training selection.");
        }
        currentBookingSession.setDate(selectedDate);
    }

    // Creating the complete Booking
    private void createBooking(SelectedSlotAndExtraBean slotAndExtra) {
        BookingInterface booking = new ConcreteBooking();
        BookingSession bSession = SessionManager.getInstance().getBookingSession();

        // Athlete
        Athlete athlete = (Athlete) SessionManager.getInstance().getLoggedUser();
        // Training
        Training training = bSession.getTraining();
        // LocalDate for selectedDate
        LocalDate selectedDate = bSession.getDate();
        // DailySchedule
        DailySchedule dailySchedule = training.getSchedules().get(selectedDate);
        // String for selectedSlot
        String selectedSlot = slotAndExtra.getSelectedSlot();

        // Obtaining time based on index
        if(!selectedSlot.contains(":")) {
            selectedSlot = selectedSlot + ":00";
        }
        if(selectedSlot.length() == 4) {
            selectedSlot = "0" +  selectedSlot;
        }

        BigDecimal basePrice = training.getBasePrice();

        ConcreteBooking concreteBooking = (ConcreteBooking) booking;
        concreteBooking.setAthlete(athlete);
        concreteBooking.setTraining(training);
        concreteBooking.setDailySchedule(dailySchedule);
        concreteBooking.setSelectedSlot(LocalTime.parse(selectedSlot));
        concreteBooking.setFinalPrice(basePrice);

        // Decorator applications
        if(slotAndExtra.isTowel()) {
            booking = new TowelDecorator(booking);
        }

        if(slotAndExtra.isSauna()) {
            booking = new SaunaDecorator(booking);
        }

        if(slotAndExtra.isEnergizer()) {
            booking = new EnergizerDecorator(booking);
        }

        if(slotAndExtra.isVideo()) {
            booking = new VidAnalysisDecorator(booking);
        }

        bSession.setBooking(booking);
    }

    // Obtaining the date related DailySchedule
    private DailySchedule getDailySchedule(SelectedDateBean dateBean) {

        setSelectedDate(dateBean);

        LocalDate date;
        Training training;

        // Obtaining LocalDate and Training from BookingSession
        BookingSession bSession = SessionManager.getInstance().getBookingSession();

        try {
            date = dateBean.getSelectedDate();
            training = bSession.getTraining();
        } catch (Exception e) {
            throw new DataLoadException("Error retrieving selected Training and/or date ", e);
        }

        // Obtaining/Creating the DailySchedule for the selected date and saving it in the BookingSession
        if(!training.getSchedules().containsKey(date)) {
            DailySchedule newDS = new DailySchedule(training, date, null);

            DailyScheduleDAO dsDAO = FactoryDAO.getInstance().createDailyScheduleDAO();
            dsDAO.updateDailySchedule(training, newDS);

            training.getSchedules().put(date, newDS);
        }
        return training.getSchedules().get(date);
    }

    // DailySchedule update
    private void updateDailySchedule() {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();
        BookingInterface currentBooking = bSession.getBooking();

        Training currentTraining = bSession.getTraining();
        LocalDate selectedDate = currentBooking.getDailySchedule().getDate();

        String selectedTime = currentBooking.getSelectedSlot().toString();
        DailySchedule dailySchedule = currentTraining.getSchedules().get(selectedDate);

        int indexToOccupy = Integer.parseInt(selectedTime.split(":")[0]);
        dailySchedule.setSlotOccupied(indexToOccupy);

        DailyScheduleDAO dsDAO = FactoryDAO.getInstance().createDailyScheduleDAO();
        dsDAO.updateDailySchedule(currentTraining, dailySchedule);
    }
}