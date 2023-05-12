package lk.ijse.posm.dto;

public class Change {

    private String paymentId;

    private String paymentDate;

    private String sellingMoneyType;

    private double sellingMoneyAmount;

    private String receivingMoneyType;

    private double receivingMoneyAmount;

    private String customerId;

    public Change() {
    }

    public Change(String paymentId, String paymentDate, String sellingMoneyType, double sellingMoneyAmount, String receivingMoneyType, double receivingMoneyAmount, String customerId) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.sellingMoneyType = sellingMoneyType;
        this.sellingMoneyAmount = sellingMoneyAmount;
        this.receivingMoneyType = receivingMoneyType;
        this.receivingMoneyAmount = receivingMoneyAmount;
        this.customerId = customerId;
    }

    public Change(String paymentId, String paymentDate, String sellingMoneyType, double sellingMoneyAmount, String receivingMoneyType, double receivingMoneyAmount) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.sellingMoneyType = sellingMoneyType;
        this.sellingMoneyAmount = sellingMoneyAmount;
        this.receivingMoneyType = receivingMoneyType;
        this.receivingMoneyAmount = receivingMoneyAmount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getSellingMoneyType() {
        return sellingMoneyType;
    }

    public void setSellingMoneyType(String sellingMoneyType) {
        this.sellingMoneyType = sellingMoneyType;
    }

    public double getSellingMoneyAmount() {
        return sellingMoneyAmount;
    }

    public void setSellingMoneyAmount(double sellingMoneyAmount) {
        this.sellingMoneyAmount = sellingMoneyAmount;
    }

    public String getReceivingMoneyType() {
        return receivingMoneyType;
    }

    public void setReceivingMoneyType(String receivingMoneyType) {
        this.receivingMoneyType = receivingMoneyType;
    }

    public double getReceivingMoneyAmount() {
        return receivingMoneyAmount;
    }

    public void setReceivingMoneyAmount(double receivingMoneyAmount) {
        this.receivingMoneyAmount = receivingMoneyAmount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
