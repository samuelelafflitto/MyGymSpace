package beans;

import java.time.LocalDate;
import java.time.LocalTime;

public class ProfileStatsBean {
    private int stats1;
    private int stats2;
    private LocalDate nextDate;
    private LocalTime nextTime;

    public int getStats1() {
        return stats1;
    }

    public int getStats2() {
        return stats2;
    }

    public LocalDate getNextDate() {
        return nextDate;
    }

    public LocalTime getNextTime() {
        return nextTime;
    }

    public void setStats1(int stats1) {
        this.stats1 = stats1;
    }

    public void setStats2(int stats2) {
        this.stats2 = stats2;
    }

    public void setNextDate(LocalDate nextDate) {
        this.nextDate = nextDate;
    }

    public void setNextTime(LocalTime nextTime) {
        this.nextTime = nextTime;
    }
}
