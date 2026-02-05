package models.booking;

import utils.PriceConfig;

import java.math.BigDecimal;

public class EnergizerDecorator extends BookingDecorator {
    private final BigDecimal energizerCost;
    private final String energizerName;
    public EnergizerDecorator(BookingInterface booking) {
        super(booking);
        energizerCost = PriceConfig.getExtraPrice("energizer");
        energizerName = PriceConfig.getExtraName("energizer");
    }

    @Override
    public String getDescription() {
        String addedExtraOption = "";
        if(!super.getDescription().isEmpty()) {
            addedExtraOption = " + ";
        }
        return super.getDescription() + addedExtraOption + energizerName;
    }

    @Override
    public BigDecimal getFinalPrice() {
        return super.getFinalPrice().add(energizerCost);
    }
}
