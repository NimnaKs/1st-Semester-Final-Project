package lk.ijse.posm.dto;

public class PostManMailDetails {

    private String mailId;

    private String receiverName;

    private String receiversAddress;

    public PostManMailDetails(String mailId, String receiverName, String receiversAddress) {
        this.mailId = mailId;
        this.receiverName = receiverName;
        this.receiversAddress = receiversAddress;
    }

    public PostManMailDetails() {
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiversAddress() {
        return receiversAddress;
    }

    public void setReceiversAddress(String receiversAddress) {
        this.receiversAddress = receiversAddress;
    }
}
