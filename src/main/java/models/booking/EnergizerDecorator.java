package models.booking;

import utils.PriceConfig;

import java.math.BigDecimal;

public class EnergizerDecorator extends BookingDecorator {
    private final BigDecimal energizerCost;
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
    public BigDecimal getFinalPrice() {
        return super.getFinalPrice().add(energizerCost);
    }
}
