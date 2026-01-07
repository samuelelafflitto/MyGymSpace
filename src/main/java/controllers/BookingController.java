package controllers;

import beans.*;
import exceptions.DataLoadException;
import models.booking.*;
import models.dailyschedule.DailySchedule;
import models.dao.factory.FactoryDAO;
import models.training.Training;
import models.user.Athlete;
import models.user.PersonalTrainer;
import models.user.User;
import utils.session.BookingSession;
import utils.session.SessionManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingController {

    private boolean isSessionOpen() {
        return SessionManager.getInstance().getBookingSession() != null;
    }

    private void closeSession() {
        SessionManager.getInstance().freeBookingSession();
    }

    // Ricava la lista degli allenamenti disponibili
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
            tBean.setPersonalTrainer(t.getPersonalTrainer());

            trainingBeans.add(tBean);
        }
        return trainingBeans;
    }

    // Ricava il Training selezionato dalla BookingSession che è già aperta
    public SelectedTrainingBean getSelectedTraining() {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();
        String selectedTraining = bSession.getTraining().getName();
        PersonalTrainer personalTrainer = bSession.getTraining().getPersonalTrainer();
        return new SelectedTrainingBean(selectedTraining, personalTrainer);
    }

    // Ricava la LocalDate selezionata dalla BookingSession che è già aperta
    public SelectedDateBean getSelectedDate() {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();
        LocalDate selectedDate = bSession.getDate();
        return new SelectedDateBean(selectedDate);
    }

    // CHIAMATO ALLA SELEZIONE DELL'ALLENAMENTO DA SALVARE NELLA BOOKINGSESSION
    // Dal Bean relativo al Training selezionato verifica che esista e lo aggiunge alla BookingSession
    public void setSelectedTraining(SelectedTrainingBean selectedTrainingBean) {
        String trainingName = selectedTrainingBean.getName();
        PersonalTrainer personalTrainer = selectedTrainingBean.getPersonalTrainer();

        List<Training> allTrainings = new ArrayList<>();

        try {
            allTrainings = FactoryDAO.getInstance().createTrainingDAO().getAvailableTrainings();
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
        }

        // Cerca allenamento con dati come quelli ricevuti
        for(Training t: allTrainings) {
            // Se lo trova crea una nuova BookingSession senza inserire alcuna data
            if(t.getName().equals(trainingName) && t.getPersonalTrainer().equals(personalTrainer)) {
                BookingSession bSession = new BookingSession(t, null);
                SessionManager.getInstance().createBookingSession(bSession);
            }
        }
    }

    // CHIAMATO ALLA SELEZIONE DI UNA DATA DA SALVARE NELLA BOOKINGSESSION
    // Aggiunta della LocalDate selezionata alla BookingSession
    // Esistendo già una BookingSession contenente il Training selezionato, ne creo una nuova con anche la LocalDate e la sostituisco a quella esistente
    private void setSelectedDate(SelectedDateBean selectedDateBean) {
        LocalDate selectedDate = selectedDateBean.getSelectedDate();

        BookingSession currentBookingSession =  SessionManager.getInstance().getBookingSession();
        Training currentTraining = currentBookingSession.getTraining();
        BookingSession bSession = new BookingSession(currentTraining, selectedDate);
        SessionManager.getInstance().createBookingSession(bSession);
    }



    // CHIAMATO PER OTTENERE GLI SLOT DA MOSTRARE NEL MENU A TENDINA
    // Ricevuta la LocalDate tramite SelectedDateBean, aggiorna la BookingSession e ricava gli AvailableSlots dalla DailySchedule corretta
    public List<String> getDailySchedule(SelectedDateBean dateBean) {

        // Aggiorna BookingSession con la LocalDate
        setSelectedDate(dateBean);

        LocalDate date = null;
        Training training = null;

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
            training.getSchedules().put(date, new DailySchedule(date));
        }

        // Restituisce gli slot liberi come Lista di String
        DailySchedule selectedSchedule = training.getSchedules().get(date);
        return selectedSchedule.getAvailableSlots();
    }



    // CHIAMATO ALLA RICHIESTA DI MOSTRARE UN RECAP (una volta inseriti tutti i dati)
    // Ottiene la Booking finale e la restituisce come BookingRecapBean per inviare i dati del Recap al Controller grafico
    public BookingRecapBean getBookingRecap(SelectedSlotAndExtraBean lastBookingDataBean) {
        // Creazione della Booking
        BookingInterface currentBooking = createBooking(lastBookingDataBean);

        return new BookingRecapBean(currentBooking);
    }



    // CHIAMATO ALLA CONFERMA DEL RECAP
    // Si occupa di memorizzare la Booking confermata
    public void saveBooking() {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();

        // Dalla BookingSession ricavo la Booking e il Training veri e propri
        BookingInterface currentBooking = bSession.getBooking();
        Training currentTraining = bSession.getTraining();

        //Dalla Booking ricavo la LocalDate e la String (relativa al TimeSlot selezionato)
        LocalDate selectedDate = currentBooking.getDate();
        String selectedSlot = currentBooking.getStartTime();

        BookingDAO bDAO = FactoryDAO.getInstance().createBookingDAO();

        try {
            bDAO.saveBooking(currentBooking);
        } catch (DataLoadException e) {
            System.out.println(e.getMessage());
        }

        // Aggiungendo la prenotazione alle prenotazioni dell'Atleta
        User loggedUser = SessionManager.getInstance().getLoggedUser();
        ((Athlete) loggedUser).addBooking(currentBooking);

        updateDailySchedule();
        bSession.clearBookingSession();
    }





    // OPERAZIONI PRIVATE
    // Ricevuto il TimeSlot selezionato e le eventuali ExtraOptions, crea la Booking finale e la aggiunge alla BookingSession
    // Restituisce la Booking finale
    private BookingInterface createBooking(SelectedSlotAndExtraBean slotAndExtra) {
        BookingInterface booking = new ConcreteBooking();
        BookingSession bSession = SessionManager.getInstance().getBookingSession();
        Training training = bSession.getTraining();
        LocalDate selectedDate = bSession.getDate();
        String selectedSlot = slotAndExtra.getSelectedSlot();

        ConcreteBooking concreteBooking = (ConcreteBooking) booking;
        concreteBooking.setAthlete(SessionManager.getInstance().getLoggedUser().getUsername());
        concreteBooking.setTrainingName(training.getName());
        concreteBooking.setDate(selectedDate);

        // Convertire la stringa parametro in LocalTime
        concreteBooking.setStartTime(selectedSlot);

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

        // currentBooking viene memorizzata nella BookingSession
        BookingSession currentBookingSession =  SessionManager.getInstance().getBookingSession();
        currentBookingSession.setBooking(booking);
        return booking;
    }

    private void updateDailySchedule() {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();

        BookingInterface currentBooking = bSession.getBooking();
        Training currentTraining = bSession.getTraining();

        LocalDate selectedDate = currentBooking.getDate();
        String selectedSlot = currentBooking.getStartTime();

        currentTraining.getSchedules().get(selectedDate).setSlotOccupied(selectedSlot);
    }
}