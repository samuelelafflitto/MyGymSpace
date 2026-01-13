package models.booking;

import utils.PriceConfig;

import java.math.BigDecimal;

public class SaunaDecorator extends BookingDecorator {
    private final BigDecimal saunaCost;
    public SaunaDecorator(BookingInterface booking) {
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
    public BigDecimal getFinalPrice() {
        return super.getFinalPrice().add(saunaCost);
    }
}
