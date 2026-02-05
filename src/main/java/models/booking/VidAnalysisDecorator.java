package models.booking;

import utils.PriceConfig;

import java.math.BigDecimal;

public class VidAnalysisDecorator extends BookingDecorator {
    private final BigDecimal videoCost;
    private final String videoName;
    public VidAnalysisDecorator(BookingInterface booking) {
        super(booking);
        videoCost = PriceConfig.getExtraPrice("video");
        videoName = PriceConfig.getExtraName("video");
    }

    @Override
    public String getDescription() {
        String addedExtraOption = "";
        if(!super.getDescription().isEmpty()) {
            addedExtraOption = " + ";
        }
        return super.getDescription() + addedExtraOption + videoName;
    }

    @Override
    public BigDecimal getFinalPrice() {
        return super.getFinalPrice().add(videoCost);
    }
}
