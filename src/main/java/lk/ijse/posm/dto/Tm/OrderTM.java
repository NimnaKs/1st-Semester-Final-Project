package lk.ijse.posm.dto.Tm;


import javafx.scene.control.Button;

public class OrderTM {

    private String code;

    private String description;

    private int qty;

    private double unitPriced;

    private double total;

    private Button btn;

    public OrderTM(String code, String description, int qty, double unitPriced, double total, Button btn) {
        this.code = code;
        this.description = description;
        this.qty = qty;
        this.unitPriced = unitPriced;
        this.total = total;
        this.btn = btn;
    }

    public OrderTM() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPriced() {
        return unitPriced;
    }

    public void setUnitPriced(double unitPriced) {
        this.unitPriced = unitPriced;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
