package lk.ijse.posm.dto;

public class Payment {

    private String payment_id;

    private String bill_owner_name;

    private String bill_type;

    private String company_name;

    private String referenceNo;

    private String payment_Date;

    private String customer_id;

    private String user_id;

    private double paymentAmount;

    public Payment() {
    }

    public Payment(String payment_id, String bill_owner_name, String bill_type, String company_name, String referenceNo, String payment_Date, String customer_id, String user_id, double paymentAmount) {
        this.payment_id = payment_id;
        this.bill_owner_name = bill_owner_name;
        this.bill_type = bill_type;
        this.company_name = company_name;
        this.referenceNo = referenceNo;
        this.payment_Date = payment_Date;
        this.customer_id = customer_id;
        this.user_id = user_id;
        this.paymentAmount = paymentAmount;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getBill_owner_name() {
        return bill_owner_name;
    }

    public void setBill_owner_name(String bill_owner_name) {
        this.bill_owner_name = bill_owner_name;
    }

    public String getBill_type() {
        return bill_type;
    }

    public void setBill_type(String bill_type) {
        this.bill_type = bill_type;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getPayment_Date() {
        return payment_Date;
    }

    public void setPayment_Date(String payment_Date) {
        this.payment_Date = payment_Date;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
