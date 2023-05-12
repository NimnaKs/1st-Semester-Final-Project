package lk.ijse.posm.dto;

public class Customer {

    private String customer_id;

    private String customer_name;

    private String customer_contact;

    public Customer() {
    }

    public Customer(String customer_id, String customer_name, String customer_contact) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_contact = customer_contact;
    }

    public Customer(String customer_name, String customer_contact) {
        this.customer_name = customer_name;
        this.customer_contact = customer_contact;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_contact() {
        return customer_contact;
    }

    public void setCustomer_contact(String customer_contact) {
        this.customer_contact = customer_contact;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customer_id='" + customer_id + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", customer_contact='" + customer_contact + '\'' +
                '}';
    }
}
