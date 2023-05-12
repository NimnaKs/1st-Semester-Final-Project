package lk.ijse.posm.dto;

public class PostmanSave {

    private String postId;

    private String postArea;

    private String vehicleNo;

    private String vehicleType;

    private String employee_Id;

    private String image;

    public PostmanSave(String postId, String postArea, String vehicleNo, String vehicleType, String employee_Id, String image) {
        this.postId = postId;
        this.postArea = postArea;
        this.vehicleNo = vehicleNo;
        this.vehicleType = vehicleType;
        this.employee_Id = employee_Id;
        this.image = image;
    }

    public PostmanSave() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostArea() {
        return postArea;
    }

    public void setPostArea(String postArea) {
        this.postArea = postArea;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getEmployee_Id() {
        return employee_Id;
    }

    public void setEmployee_Id(String employee_Id) {
        this.employee_Id = employee_Id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
