package lk.ijse.posm.dto;

public class NewUser {

    private String type;

    private String userId;

    private String userName;

    private String employeeName;

    private String employeeEmailAddress;

    private String employeeContactNo;

    private String password;

    public NewUser(String type, String userId, String userName, String employeeName, String employeeEmailAddress, String employeeContactNo) {
        this.type = type;
        this.userId = userId;
        this.userName = userName;
        this.employeeName = employeeName;
        this.employeeEmailAddress = employeeEmailAddress;
        this.employeeContactNo = employeeContactNo;
    }

    public NewUser(String type, String userId, String userName, String employeeName, String employeeEmailAddress, String employeeContactNo, String password) {
        this.type = type;
        this.userId = userId;
        this.userName = userName;
        this.employeeName = employeeName;
        this.employeeEmailAddress = employeeEmailAddress;
        this.employeeContactNo = employeeContactNo;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public NewUser() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeEmailAddress() {
        return employeeEmailAddress;
    }

    public void setEmployeeEmailAddress(String employeeEmailAddress) {
        this.employeeEmailAddress = employeeEmailAddress;
    }

    public String getEmployeeContactNo() {
        return employeeContactNo;
    }

    public void setEmployeeContactNo(String employeeContactNo) {
        this.employeeContactNo = employeeContactNo;
    }
}
