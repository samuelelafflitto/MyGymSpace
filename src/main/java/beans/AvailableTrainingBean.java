package beans;

import java.math.BigDecimal;

public class AvailableTrainingBean {
    private String ptUsername;
    private String ptLastName;
    private String name;
    private String description;
    private BigDecimal basePrice;

    public String getPersonalTrainer() {
        return ptUsername;
    }

    public String getPtLastName() {
        return ptLastName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setPersonalTrainer(String ptUsername) {
        this.ptUsername = ptUsername;
    }

    public void setPtLastName(String ptLastName) {
        this.ptLastName = ptLastName;
    }

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
