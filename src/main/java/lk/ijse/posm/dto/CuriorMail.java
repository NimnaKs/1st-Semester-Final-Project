package lk.ijse.posm.dto;

public class CuriorMail {

    private String mail_id;

    private String mail_type;

    private String send_date;

    private String send_Name;

    private String send_Address;

    private String receiver_Name;

    private String receiver_Telephone_No;

    private String receiver_Address;

    private double weight;

    private double price;

    private String delivery_postman_id;

    private String customerId;

    public CuriorMail(String mail_id, String mail_type, String send_date, String send_Name, String send_Address, String receiver_Name, String receiver_Telephone_No, String receiver_Address, double weight, double price, String delivery_postman_id, String customerId) {
        this.mail_id = mail_id;
        this.mail_type = mail_type;
        this.send_date = send_date;
        this.send_Name = send_Name;
        this.send_Address = send_Address;
        this.receiver_Name = receiver_Name;
        this.receiver_Telephone_No = receiver_Telephone_No;
        this.receiver_Address = receiver_Address;
        this.weight = weight;
        this.price = price;
        this.delivery_postman_id = delivery_postman_id;
        this.customerId = customerId;
    }

    public CuriorMail(String mail_id, String mail_type, String send_date, String send_Name, String send_Address, String receiver_Name, String receiver_Telephone_No, String receiver_Address, double weight, double price, String delivery_postman_id) {
        this.mail_id = mail_id;
        this.mail_type = mail_type;
        this.send_date = send_date;
        this.send_Name = send_Name;
        this.send_Address = send_Address;
        this.receiver_Name = receiver_Name;
        this.receiver_Telephone_No = receiver_Telephone_No;
        this.receiver_Address = receiver_Address;
        this.weight = weight;
        this.price = price;
        this.delivery_postman_id = delivery_postman_id;
    }

    public CuriorMail() {
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMail_id() {
        return mail_id;
    }

    public void setMail_id(String mail_id) {
        this.mail_id = mail_id;
    }

    public String getMail_type() {
        return mail_type;
    }

    public void setMail_type(String mail_type) {
        this.mail_type = mail_type;
    }

    public String getSend_date() {
        return send_date;
    }

    public void setSend_date(String send_date) {
        this.send_date = send_date;
    }

    public String getSend_Name() {
        return send_Name;
    }

    public void setSend_Name(String send_Name) {
        this.send_Name = send_Name;
    }

    public String getSend_Address() {
        return send_Address;
    }

    public void setSend_Address(String send_Address) {
        this.send_Address = send_Address;
    }

    public String getReceiver_Name() {
        return receiver_Name;
    }

    public void setReceiver_Name(String receiver_Name) {
        this.receiver_Name = receiver_Name;
    }

    public String getReceiver_Telephone_No() {
        return receiver_Telephone_No;
    }

    public void setReceiver_Telephone_No(String receiver_Telephone_No) {
        this.receiver_Telephone_No = receiver_Telephone_No;
    }

    public String getReceiver_Address() {
        return receiver_Address;
    }

    public void setReceiver_Address(String receiver_Address) {
        this.receiver_Address = receiver_Address;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDelivery_postman_id() {
        return delivery_postman_id;
    }

    public void setDelivery_postman_id(String delivery_postman_id) {
        this.delivery_postman_id = delivery_postman_id;
    }
}
