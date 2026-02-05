package beans;

import java.math.BigDecimal;

public class TrainingToEditBean {
    private String name;
    private String description;
    private BigDecimal basePrice;

    public TrainingToEditBean() {// The constructor does not need parameters
    }

    // GETTER
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    // SETTER
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
}
