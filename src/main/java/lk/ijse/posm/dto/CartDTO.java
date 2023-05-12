package lk.ijse.posm.dto;

public class CartDTO {

    private String item_code;

    private int qty;

    private double unitPrice;

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public CartDTO(String item_code, int qty, double unitPrice) {
        this.item_code = item_code;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public CartDTO() {
    }
}
