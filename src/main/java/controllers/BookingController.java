package controllers;

import beans.AvailableTrainingBean;
import beans.SelectedDateBean;
import beans.SelectedTrainingBean;
import exceptions.DataLoadException;
import models.dailyschedule.DailySchedule;
import models.dao.factory.FactoryDAO;
import models.training.Training;
import models.user.PersonalTrainer;
import utils.session.BookingSession;
import utils.session.SessionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingController {

    public boolean isSessionOpen() {
        return SessionManager.getInstance().getBookingSession() != null;
    }

    public void closeSession() {
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

    // Dal Bean relativo al Training selezionato verifica che esista e lo aggiunge alla BookingSession
    public void setBookingSessionTraining(SelectedTrainingBean selectedTrainingBean) {
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

    // Aggiunta della LocalDate selezionata alla BookingSession
    // Esistendo già una BookingSession contenente il Training selezionato, ne creo una nuova con anche la LocalDate e la sostituisco a quella esistente
    public void setBookingSessionDate(SelectedDateBean selectedDateBean) {
        LocalDate selectedDate = selectedDateBean.getSelectedDate();

        BookingSession currentBookingSession =  SessionManager.getInstance().getBookingSession();
        Training currentTraining = currentBookingSession.getTraining();
        BookingSession bSession = new BookingSession(currentTraining, selectedDate);
        SessionManager.getInstance().createBookingSession(bSession);
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

    // PROBLEMA: I calcoli relativi alle schedule andrebbero fatti qui nel controller
    // DA REIMPLEMENTARE
    /*public DailySchedule getDailySchedule(SelectedDateBean sDBean) {
        BookingSession bSession = SessionManager.getInstance().getBookingSession();
        Training training = bSession.getTraining();

        LocalDate selectedDate = sDBean.getSelectedDate();

        return training.getDailySchedule(selectedDate);
    }*/

    /*public DailySchedule getDailySchedule()*/
}
