package lk.ijse.posm.dto;

public class PostManEmployee {

    private String post_area;

    private String employee_name;

    private String img;


    public PostManEmployee(String post_area, String employee_name, String img) {
        this.post_area = post_area;
        this.employee_name = employee_name;
        this.img = img;
    }

    public PostManEmployee() {
    }

    public String getPost_area() {
        return post_area;
    }

    public void setPost_area(String post_area) {
        this.post_area = post_area;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
