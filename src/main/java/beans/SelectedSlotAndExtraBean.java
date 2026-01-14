package beans;

import exceptions.InvalidSelectionException;
import exceptions.InvalidTimeSlotException;

public class SelectedSlotAndExtraBean {
    private String selectedSlot;
    private boolean towel;
    private boolean sauna;
    private boolean energizer;
    private boolean video;

    public SelectedSlotAndExtraBean() {// Il costruttore non ha bisogno di parametri
    }

    public String getSelectedSlot() {
        return selectedSlot;
    }

    public boolean  isTowel() {
        return towel;
    }

    public boolean isEnergizer() {
        return energizer;
    }

    public boolean isSauna() {
        return sauna;
    }

    public boolean isVideo() {
        return video;
    }

    public void setSelectedSlot(int selectedSlot) {
        if(!checkSlot(selectedSlot)) {
            throw new InvalidTimeSlotException();
        }
        this.selectedSlot = Integer.toString(selectedSlot);
    }

    public void setTowel(String input) {
        if(checkExtraSelector(input))
            this.towel = input.equals("y");
        else throw new InvalidSelectionException();
    }

    public void setSauna(String input) {
        if(checkExtraSelector(input))
            this.sauna = input.equals("y");
        else throw new InvalidSelectionException();
    }

    public void setEnergizer(String input) {
        if(checkExtraSelector(input))
            this.energizer = input.equals("y");
        else throw new InvalidSelectionException();
    }

    public void setVideo(String input) {
        if(checkExtraSelector(input))
            this.video = input.equals("y");
        else throw new InvalidSelectionException();
    }



    private boolean checkSlot(int enteredSlot) {
        return enteredSlot >= 0 && enteredSlot <= 23;
    }

    private boolean checkExtraSelector(String selector) {
        return selector.equals("y") || selector.equals("n");
    }
}
