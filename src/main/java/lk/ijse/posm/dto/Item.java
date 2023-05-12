package lk.ijse.posm.dto;

public class Item {

    private String item_code;

    private String item_type;

    private int qty_on_hand;

    private double unit_price;

    public Item(String item_code, String item_type, int qty_on_hand, double unit_price) {
        this.item_code = item_code;
        this.item_type = item_type;
        this.qty_on_hand = qty_on_hand;
        this.unit_price = unit_price;
    }

    public Item() {
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public int getQty_on_hand() {
        return qty_on_hand;
    }

    public void setQty_on_hand(int qty_on_hand) {
        this.qty_on_hand = qty_on_hand;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }
}
