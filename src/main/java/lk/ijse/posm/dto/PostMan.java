package lk.ijse.posm.dto;

public class PostMan {

    private String postman_id;

    private String employee_name;

    public PostMan(String postman_id, String employee_name) {
        this.postman_id = postman_id;
        this.employee_name = employee_name;
    }

    public PostMan() {
    }

    public String getPostman_id() {
        return postman_id;
    }

    public void setPostman_id(String postman_id) {
        this.postman_id = postman_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }
}
