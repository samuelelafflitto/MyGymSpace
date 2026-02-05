package models.booking;

import utils.PriceConfig;

import java.math.BigDecimal;

public class SaunaDecorator extends BookingDecorator {
    private final BigDecimal saunaCost;
    private final String saunaName;
    public SaunaDecorator(BookingInterface booking) {
        super(booking);
        saunaCost = PriceConfig.getExtraPrice("sauna");
        saunaName = PriceConfig.getExtraName("sauna");
    }

    @Override
    public String getDescription() {
        String addedExtraOption = "";
        if(!super.getDescription().isEmpty()) {
            addedExtraOption = " + ";
        }
        return super.getDescription() + addedExtraOption + saunaName;
    }

    @Override
    public BigDecimal getFinalPrice() {
        return super.getFinalPrice().add(saunaCost);
    }
}
