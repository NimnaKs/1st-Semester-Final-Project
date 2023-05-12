package lk.ijse.posm.dto;

public class Mail {

    private String postman_id;
    private String mails_id;
    private String senders_name;
    private String senders_address;
    private String send_date;
    private String receivers_name;
    private String receivers_address;
    private String receivers_date;
    private String type;

    public Mail() {
    }

    public String getReceivers_date() {
        return receivers_date;
    }

    public void setReceivers_date(String receivers_date) {
        this.receivers_date = receivers_date;
    }

    public Mail( String postman_id,String mails_id,String senders_name, String senders_address, String send_date, String receivers_name, String receivers_address,  String receivers_date,String type) {
        this.senders_name = senders_name;
        this.senders_address = senders_address;
        this.send_date = send_date;
        this.receivers_name = receivers_name;
        this.receivers_address = receivers_address;
        this.type = type;
        this.mails_id = mails_id;
        this.postman_id = postman_id;
        this.receivers_date=receivers_date;
    }

    public String getSenders_name() {
        return senders_name;
    }

    public void setSenders_name(String senders_name) {
        this.senders_name = senders_name;
    }

    public String getSenders_address() {
        return senders_address;
    }

    public void setSenders_address(String senders_address) {
        this.senders_address = senders_address;
    }

    public String getSend_date() {
        return send_date;
    }

    public void setSend_date(String send_date) {
        this.send_date = send_date;
    }

    public String getReceivers_name() {
        return receivers_name;
    }

    public void setReceivers_name(String receivers_name) {
        this.receivers_name = receivers_name;
    }

    public String getReceivers_address() {
        return receivers_address;
    }

    public void setReceivers_address(String receivers_address) {
        this.receivers_address = receivers_address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMails_id() {
        return mails_id;
    }

    public void setMails_id(String mails_id) {
        this.mails_id = mails_id;
    }

    public String getPostman_id() {
        return postman_id;
    }

    public void setPostman_id(String postman_id) {
        this.postman_id = postman_id;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "senders_name='" + senders_name + '\'' +
                ", senders_address='" + senders_address + '\'' +
                ", send_date='" + send_date + '\'' +
                ", receivers_name='" + receivers_name + '\'' +
                ", receivers_address='" + receivers_address + '\'' +
                ", type='" + type + '\'' +
                ", mails_id='" + mails_id + '\'' +
                ", postman_id='" + postman_id + '\'' +
                ", receivers_date='" + receivers_date + '\'' +
                '}';
    }
}
