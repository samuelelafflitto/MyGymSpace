package controllers;

import beans.*;
import exceptions.*;
import models.booking.*;
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
import java.util.List;

public class BookingController {
    private static final String PT_TYPE = "PT";
    private static final String ATHLETE_TYPE = "ATH";

//    // CHECK SESSIONE APERTA
//    private boolean isBookingSessionOpen() {
//        return SessionManager.getInstance().getBookingSession() != null;
//    }

//    // CHIUSURA SESSIONE
//    private void closeSession() {
//        SessionManager.getInstance().freeBookingSession();
//        // CHIUSURA DI ALTRE SESSIONI
//    }

    // [Book A Session Use Case]

    // Ricavo allenamenti disponibili e li fornisco sotto forma di lista
    // OTTENIMENTO LISTA DEGLI ALLENAMENTI DISPONIBILI
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

            trainingBeans.add(tBean);
        }
        return trainingBeans;
    }

    // Ricevo allenamento selezionato, avvio nuova BookingSession con Training inserito
    // VERIFICA ESISTENZA ALLENAMENTO SELEZIONATO E CREAZIONE NUOVA BOOKINGSESSION
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
                System.out.println(bSession.getTraining().getName());
            }
        }


//        String trainingName = selectedTrainingBean.getName();
//        String ptUsername = selectedTrainingBean.getPersonalTrainer();
//
//        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
//        PersonalTrainer personalTrainer = (PersonalTrainer) userDAO.getUserByUsername(ptUsername);
//
//        TrainingDAO trainingDAO = FactoryDAO.getInstance().createTrainingDAO();
//        Training selectedTraining = trainingDAO.getTrainingByPT(personalTrainer);
//
//        BookingSession bookingSession = new BookingSession(selectedTraining);
//        SessionManager.getInstance().createBookingSession(bookingSession);


        /*List<Training> allTrainings = new ArrayList<>();

        try {
            allTrainings = FactoryDAO.getInstance().createTrainingDAO().getAvailableTrainings();
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
        }

        // Cerca allenamento con dati come quelli ricevuti
        for(Training t: allTrainings) {
            // Se lo trova crea una nuova BookingSession senza inserire alcuna data
            if(t.getName().equals(trainingName) && t.getPersonalTrainer().equals(personalTrainer)) {
                BookingSession bSession = new BookingSession(t);
                SessionManager.getInstance().createBookingSession(bSession);
            }
        }*/
    }

    public void setBookingSessionDate(SelectedDateBean selectedDateBean) {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();

        // Ottengo la DailySchedule per la data (o la creo)
        DailySchedule dailySchedule = getDailySchedule(selectedDateBean);
        // Devo aggiungerla alla lista di DailySchedule per quel Training (se non c'è gia)

        bSession.setDailySchedule(dailySchedule);
    }

    public void setBookingSessionBooking(SelectedSlotAndExtraBean selectedSlotAndExtraBean) {
        // Creazione Booking e salvataggio in BookingSession
        createBooking(selectedSlotAndExtraBean);
    }

//    public SelectedTrainingBean getSelectedTrainingBean(AvailableTrainingBean bean) {
//        String trainingName = bean.getName();
//        String ptUsername =  bean.getPersonalTrainer();
//
//        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
//        PersonalTrainer personalTrainer = (PersonalTrainer) userDAO.getUserByUsername(ptUsername);
//
//        return new SelectedTrainingBean(trainingName, personalTrainer);
//    }


    // OTTENIMENTO ALLENAMENTO SELEZIONATO E INSERIMENTO IN UN BEAN - INUTILE
    public SelectedTrainingBean getSelectedTraining() {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();
        String selectedTraining = bSession.getTraining().getName();
        PersonalTrainer personalTrainer = bSession.getTraining().getPersonalTrainer();
        return new SelectedTrainingBean(selectedTraining, personalTrainer);
    }

    // OTTENIMENTO DATA SELEZIONATA E INSERIEMENTO IN UN BEAN - INUTILE
    /*public SelectedDateBean getSelectedDate() {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();
        LocalDate selectedDate = bSession.getDate();
        return new SelectedDateBean(selectedDate);
    }*/



//    // CHIAMATO ALLA SELEZIONE DI UNA DATA DA SALVARE NELLA BOOKINGSESSION
//    private void setSelectedDate(SelectedDateBean dateBean) {
//        LocalDate selectedDate = dateBean.getSelectedDate();
//
//        BookingSession currentBookingSession =  SessionManager.getInstance().getBookingSession();
//        Training currentTraining = currentBookingSession.getTraining();
//
//        SessionManager.getInstance().freeBookingSession();
//
//        BookingSession newBookingSession = new BookingSession(currentTraining, selectedDate);
//        SessionManager.getInstance().createBookingSession(newBookingSession);
//    }

    public List<String> getAvailableSlots(/*SelectedDateBean dateBean*/) throws AttemptsException {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();

        DailySchedule dailySchedule = bSession.getDailySchedule();
        StringBuilder timeSlots = dailySchedule.getTimeSlots();

        List<String> availableSlots = new ArrayList<>();

        // Turno mattina
        LocalTime morningStart = ScheduleConfig.getTime("schedule.morning.start", "09:00");
        LocalTime morningEnd = ScheduleConfig.getTime("schedule.morning.end", "13:00");

        // Turno pomeriggio
        LocalTime afternoonStart = ScheduleConfig.getTime("schedule.afternoon.start", "15:00");
        LocalTime afternoonEnd = ScheduleConfig.getTime("schedule.afternoon.end", "20:00");

        // Numero di slot mattina e pomeriggio
        int mSlot = (int) ChronoUnit.HOURS.between(morningStart, morningEnd);
        int aSlot = (int) ChronoUnit.HOURS.between(afternoonStart, afternoonEnd);

        // Trasformo orario di inizio (mattina e pomeriggio) in indici
        int morningStartIndex = Integer.parseInt(morningStart.toString().split(":")[0]);
        int afternoonStartIndex = Integer.parseInt(afternoonStart.toString().split(":")[0]);

        // Uso indici per popolare Lista quando slot sono liberi
        for(int i = morningStartIndex; i < (morningStartIndex + mSlot); i++) {
            if(timeSlots.charAt(i) == '0') {
                String hLabel = i + ":00";
                availableSlots.add(hLabel);
            }
        }

        for(int i = afternoonStartIndex; i < (afternoonStartIndex + aSlot); i++) {
            if(timeSlots.charAt(i) == '0') {
                String hLabel = i + ":00";
                availableSlots.add(hLabel);
            }
        }

        if(availableSlots.isEmpty()) {
            throw new DateException(DateErrorType.FULL_SCHEDULE);
        }
        return availableSlots;
    }

    // CHIAMATO ALLA RICHIESTA DI MOSTRARE UN RECAP (una volta inseriti tutti i dati)
    // Ottiene la Booking finale e la restituisce come BookingRecapBean per inviare i dati del Recap al Controller grafico
    public BookingRecapBean getBookingRecap(/*SelectedSlotAndExtraBean lastBookingDataBean*/) {

        BookingSession bSession = SessionManager.getInstance().getBookingSession();

        System.out.println("DEBUG - Sessione per utente " + SessionManager.getInstance().getLoggedUser().getUsername() +
                ": " + System.identityHashCode(SessionManager.getInstance().getBookingSession()));

        // Creazione della Booking
        BookingInterface currentBooking = bSession.getBooking();

        BookingRecapBean bean = new BookingRecapBean();
        bean.setTrainingName(currentBooking.getTraining().getName());
        bean.setPtTraining(currentBooking.getTraining().getPersonalTrainer().getLastName());
        bean.setDate(currentBooking.getDailySchedule().getDate());
        bean.setStartTime(currentBooking.getSelectedSlot());
        bean.setDescription(currentBooking.getDescription());
        bean.setPrice(currentBooking.getFinalPrice());

        return bean;
    }


    // CHIAMATO ALLA CONFERMA DEL RECAP
    // Si occupa di memorizzare la Booking confermata
    public boolean saveBooking(/*BookingSession bSession, BookingInterface finalBooking*/) {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();
        BookingInterface finalBooking = bSession.getBooking();

        updateDailySchedule();

//        Training currentTraining = bSession.getTraining();
//
//        //Dalla Booking ricavo la LocalDate e la String (relativa al TimeSlot selezionato)
//        LocalDate selectedDate = finalBooking.getDailySchedule().getDate();
//        String selectedSlot = finalBooking.getSelectedSlot().toString();

        BookingDAO bDAO = FactoryDAO.getInstance().createBookingDAO();

        try {
            bDAO.saveBooking(finalBooking);
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
        }

        // Aggiungendo la prenotazione alle prenotazioni dell'Atleta
        User loggedUser = SessionManager.getInstance().getLoggedUser();
        ((Athlete) loggedUser).addBooking(finalBooking);

        return true;
    }

    public void checkAttempts(int currAttempt, int maxAttempts) throws RuntimeException{
        if(currAttempt < maxAttempts) {
//            String msg = AttemptsErrorType.REMAINING.getMSG(maxAttempts -  currAttempt);
            throw new AttemptsException(AttemptsErrorType.REMAINING, (maxAttempts - currAttempt));
        }
        throw new AttemptsException(AttemptsErrorType.MAX_ATTEMPTS_REACHED);
    }


    // CONTROLLI SULLE DATE
    public boolean isPastDate(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    public boolean isHoliday(LocalDate date) {
        HolidayChecker hc = new  HolidayChecker();
        return hc.isClosedForHoliday(date);
    }










    // OPERAZIONI PRIVATE
    // CHIAMATO ALLA SELEZIONE DI UNA DATA DA SALVARE NELLA BOOKINGSESSION
    private void setSelectedDate(SelectedDateBean dateBean) {
        LocalDate selectedDate = dateBean.getSelectedDate();
        BookingSession currentBookingSession =  SessionManager.getInstance().getBookingSession();

        if(currentBookingSession == null) {
            throw new DataLoadException("Errore: Sessione di prenotazione non inizializzata. Tornare alla selezione dell'allenamento.");
        }

        //TODO currentBookingSession = null (NON DOVREBBE ESSERE COSI')
        currentBookingSession.setDate(selectedDate);
    }


    // Ricevuto il TimeSlot selezionato e le eventuali ExtraOptions, crea la Booking finale e la aggiunge alla BookingSession
    // Restituisce la Booking finale
    private void createBooking(SelectedSlotAndExtraBean slotAndExtra) {
        BookingInterface booking = new ConcreteBooking();
        BookingSession bSession = SessionManager.getInstance().getBookingSession();

        // ATLETA
        Athlete athlete = (Athlete) SessionManager.getInstance().getLoggedUser();
        // ALLENAMENTO
        Training training = bSession.getTraining();
        // DATA
        LocalDate selectedDate = bSession.getDate();
        // DAILYSCHEDULE
        DailySchedule dailySchedule = training.getSchedules().get(selectedDate);
        // ORARIO
        String selectedSlot = slotAndExtra.getSelectedSlot();

        // Normalizzazione orario
        if(!selectedSlot.contains(":")) {
            selectedSlot = selectedSlot + ":00";
        }
        if(selectedSlot.length() == 4) {
            selectedSlot = "0" +  selectedSlot;
        }

        BigDecimal basePrice = training.getBasePrice();

        ConcreteBooking concreteBooking = (ConcreteBooking) booking;
        concreteBooking.setAthlete(athlete);
//        if (concreteBooking.getAthlete() != null)
//            System.out.println("DEBUG - Atleta inserito nella Booking" + concreteBooking.getAthlete().getUsername());
        concreteBooking.setTraining(training);
//        if (concreteBooking.getTraining() != null)
//            System.out.println("DEBUG - Training inserito nella Booking"  + concreteBooking.getTraining().getName());
        concreteBooking.setDailySchedule(dailySchedule);
//        if (concreteBooking.getDailySchedule() != null)
//            System.out.println("DEBUG - DailySchedule inserito nella Booking" + concreteBooking.getDailySchedule().getTimeSlots());
        concreteBooking.setSelectedSlot(LocalTime.parse(selectedSlot));
//        if (concreteBooking.getSelectedSlot() != null)
//            System.out.println("DEBUG - Slot inserito nella Booking" +  concreteBooking.getSelectedSlot());
        concreteBooking.setFinalPrice(basePrice);
//        if (concreteBooking.getFinalPrice() != null)
//            System.out.println("DEBUG - Price inserito nella Booking" + concreteBooking.getFinalPrice());
//        concreteBooking.setStartTime(LocalTime.parse(selectedSlot));

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
        // La Booking sembra memorizzata nella BookingSession!!!


//        // currentBooking viene memorizzata nella BookingSession
//        BookingSession currentBookingSession =  SessionManager.getInstance().getBookingSession();
//        currentBookingSession.setBooking(booking);
//        return booking;
    }

    // CHIAMATO PER OTTENERE LA DAILYSCHEDULE ASSOCIATA AL GIORNO
    private DailySchedule getDailySchedule(SelectedDateBean dateBean) {

        // Aggiorna BookingSession con la LocalDate
        setSelectedDate(dateBean);

        LocalDate date;
        Training training;

        // Ricava LocalDate e Training dalla BookingSession
        BookingSession bSession = SessionManager.getInstance().getBookingSession();

        try {
            date = dateBean.getSelectedDate();
            training = bSession.getTraining();
        } catch (Exception e) {
            throw new DataLoadException("Errore nel recupero Allenamento e/o data selezionati ", e);
        }

        // Cerca/Crea la DailySchedule associata alla coppia (Training, LocalDate)
        if(!training.getSchedules().containsKey(date)) {
            // Ricavo DailySchedule (o creo)
            DailySchedule newDS = new DailySchedule(training, date, null);

            DailyScheduleDAO dsDAO = FactoryDAO.getInstance().createDailyScheduleDAO();
            dsDAO.updateDailySchedule(training, newDS);
        }
        return training.getSchedules().get(date);
    }

    private void updateDailySchedule(/*BookingSession bSession*/) {
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

    public void initializeDemoData() {
        UserDAO userDAO = FactoryDAO.getInstance().createUserDAO();
        userDAO.initializeDemoData();
    }
}