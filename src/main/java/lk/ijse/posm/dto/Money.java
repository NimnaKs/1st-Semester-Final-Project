package lk.ijse.posm.dto;

public class Money {

    private String moneyId;

    private double price;

    private double qty;

    public Money(String moneyId, double price, double qty) {
        this.moneyId = moneyId;
        this.price = price;
        this.qty = qty;
    }

    public Money(String moneyId, double price) {
        this.moneyId = moneyId;
        this.price = price;
    }

    public Money() {
    }

    public String getMoneyId() {
        return moneyId;
    }

    public void setMoneyId(String moneyId) {
        this.moneyId = moneyId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
