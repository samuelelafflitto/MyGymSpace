package models.booking;

import utils.PriceConfig;

public class EnergizerDecorator extends BookingDecorator {
    private final double energizerCost;
    public EnergizerDecorator(BookingInterface booking) {
        super(booking);
        energizerCost = PriceConfig.getExtraPrice("energizer");
    }

    @Override
    public String getDescription() {
        String addedExtraOption = "";
        if(!super.getDescription().isEmpty()) {
            addedExtraOption = " + ";
        }
        return super.getDescription() + addedExtraOption + "Shake/Snack";
    }

    @Override
    public double getCost() {
        return super.getCost() + energizerCost;
    }
}
