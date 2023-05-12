package lk.ijse.posm.dto;

public class Employee {

    private String employee_id;
    private String employee_name;
    private String employee_contactNo;
    private String employee_email;
    private String employee_type;

    public Employee() {
    }

    public Employee(String employee_id, String employee_name, String employee_contactNo, String employee_email, String employee_type) {
        this.setEmployee_id(employee_id);
        this.setEmployee_name(employee_name);
        this.setEmployee_contactNo(employee_contactNo);
        this.setEmployee_email(employee_email);
        this.setEmployee_type(employee_type);
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getEmployee_contactNo() {
        return employee_contactNo;
    }

    public void setEmployee_contactNo(String employee_contactNo) {
        this.employee_contactNo = employee_contactNo;
    }

    public String getEmployee_email() {
        return employee_email;
    }

    public void setEmployee_email(String employee_email) {
        this.employee_email = employee_email;
    }

    public String getEmployee_type() {
        return employee_type;
    }

    public void setEmployee_type(String employee_type) {
        this.employee_type = employee_type;
    }
}
