package lk.ijse.posm.dto;

public class Parcel_Order {

    private String weight_Id;

    private double price_per_Weight_range;

    private double weight;

    public Parcel_Order(String weight_Id, double price_per_Weight_range, double weight) {
        this.weight_Id = weight_Id;
        this.price_per_Weight_range = price_per_Weight_range;
        this.weight = weight;
    }

    public Parcel_Order() {
    }

    public String getWeight_Id() {
        return weight_Id;
    }

    public void setWeight_Id(String weight_Id) {
        this.weight_Id = weight_Id;
    }

    public double getPrice_per_Weight_range() {
        return price_per_Weight_range;
    }

    public void setPrice_per_Weight_range(double price_per_Weight_range) {
        this.price_per_Weight_range = price_per_Weight_range;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
