package lk.ijse.posm.dto.Tm;

public class CurrencyTM {

    private String moneyType;

    private double unitSellingPrice;

    private double unitGettingPrice;

    private double qtyOnHand;

    public CurrencyTM(String moneyType, double unitSellingPrice, double unitGettingPrice, double qtyOnHand) {
        this.moneyType = moneyType;
        this.unitSellingPrice = unitSellingPrice;
        this.unitGettingPrice = unitGettingPrice;
        this.qtyOnHand = qtyOnHand;
    }

    public CurrencyTM() {
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public double getUnitSellingPrice() {
        return unitSellingPrice;
    }

    public void setUnitSellingPrice(double unitSellingPrice) {
        this.unitSellingPrice = unitSellingPrice;
    }

    public double getUnitGettingPrice() {
        return unitGettingPrice;
    }

    public void setUnitGettingPrice(double unitGettingPrice) {
        this.unitGettingPrice = unitGettingPrice;
    }

    public double getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(double qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }
}
