package lk.ijse.posm.dto.Tm;

public class CuriorTM {

    private String weightId;

    private String type;

    private String description;

    private double price;

    public CuriorTM(String weightId, String description, String type, double price) {
        this.weightId = weightId;
        this.type = type;
        this.description = description;
        this.price = price;
    }

    public CuriorTM() {
    }

    public String getWeightId() {
        return weightId;
    }

    public void setWeightId(String weightId) {
        this.weightId = weightId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
