package models.booking;

import utils.PriceConfig;

import static utils.PriceConfig.*;

public class TowelDecorator extends BookingDecorator {
    private final double towelCost;
    public TowelDecorator(BookingInterface booking) {
        super(booking);
        towelCost = PriceConfig.getExtraPrice("towel");
    }

    @Override
    public String getDescription() {
        String addedExtraOption = "";
        if(!super.getDescription().isEmpty()) {
            addedExtraOption = " + ";
        }
        return super.getDescription() + addedExtraOption + "Towel";
    }

    @Override
    public double getCost() {
        return super.getCost() + towelCost;
    }
}
