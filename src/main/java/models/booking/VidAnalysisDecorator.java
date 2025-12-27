package models.booking;

import utils.PriceConfig;

import static utils.PriceConfig.*;

public class VidAnalysisDecorator extends BookingDecorator {
    private final double videoCost;
    public VidAnalysisDecorator(BookingInterface booking, double additionalPrice) {
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
    public double getCost() {
        return super.getCost() + videoCost;
    }
}
