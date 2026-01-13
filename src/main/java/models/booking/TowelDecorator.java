package models.booking;

import utils.PriceConfig;

import java.math.BigDecimal;

public class TowelDecorator extends BookingDecorator {
    private final BigDecimal towelCost;
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
    public BigDecimal getFinalPrice() {
        return super.getFinalPrice().add(towelCost);
    }
}
