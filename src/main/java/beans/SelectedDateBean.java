package beans;

import models.user.PersonalTrainer;

import java.time.LocalDate;

public class SelectedDateBean {
    private LocalDate selectedDate;

    public SelectedDateBean(LocalDate date) {
        this.selectedDate = date;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }
}
