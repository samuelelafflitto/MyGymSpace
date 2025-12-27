package models.booking;

import utils.PriceConfig;

import static utils.PriceConfig.*;

public class SaunaDecorator extends BookingDecorator {
    private final double saunaCost;
    public SaunaDecorator(BookingInterface booking, double additionalPrice) {
        super(booking);
        saunaCost = PriceConfig.getExtraPrice("sauna");
    }

    @Override
    public String getDescription() {
        String addedExtraOption = "";
        if(!super.getDescription().isEmpty()) {
            addedExtraOption = " + ";
        }
        return super.getDescription() + addedExtraOption + "Sauna";
    }

    @Override
    public double getCost() {
        return super.getCost() + saunaCost;
    }
}
