package lk.ijse.posm.dto;

public class Curior {

    private String weight_id;

    private double unit_price;

    private String range;

    public Curior(String weight_id, double unit_price, String range) {
        this.weight_id = weight_id;
        this.unit_price = unit_price;
        this.range = range;
    }

    public Curior() {
    }

    public String getWeight_id() {
        return weight_id;
    }

    public void setWeight_id(String weight_id) {
        this.weight_id = weight_id;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }
}
