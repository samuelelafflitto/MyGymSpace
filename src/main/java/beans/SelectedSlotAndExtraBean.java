package beans;

import java.time.LocalTime;

public class SelectedSlotAndExtraBean {
    private String selectedSlot;
    private boolean towel;
    private boolean sauna;
    private boolean energizer;
    private boolean video;

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

    public void setSelectedSlot(String selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public void setTowel(boolean towel) {
        this.towel = towel;
    }

    public void setSauna(boolean sauna) {
        this.sauna = sauna;
    }

    public void setEnergizer(boolean energizer) {
        this.energizer = energizer;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }
}
