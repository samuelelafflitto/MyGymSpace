package models.booking;

import utils.PriceConfig;

import java.math.BigDecimal;

public class VidAnalysisDecorator extends BookingDecorator {
    private final BigDecimal videoCost;
    public VidAnalysisDecorator(BookingInterface booking) {
        super(booking);
        videoCost = PriceConfig.getExtraPrice("video");
    }

    @Override
    public String getDescription() {
        String addedExtraOption = "";
        if(!super.getDescription().isEmpty()) {
            addedExtraOption = " + ";
        }
        return super.getDescription() + addedExtraOption + "Video Analysis";
    }

    @Override
    public BigDecimal getFinalPrice() {
        return super.getFinalPrice().add(videoCost);
    }
}
