package lk.ijse.posm.model;

import lk.ijse.posm.dto.*;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModel {

    public static ArrayList<String> getAllIds() throws SQLException {

        ArrayList<String> postmanIds=new ArrayList<>();

        ResultSet resultSet=CrudUtil.execute("SELECT postman_id FROM postMan");

        while (resultSet.next()){
            postmanIds.add(resultSet.getString(1));
        }

        return postmanIds;
    }

    public static PostManEmployee getPostmanDetails(String postman_id) throws SQLException {

        ResultSet resultSet=CrudUtil.execute("SELECT post_area,employee_name,image FROM postMan INNER JOIN employee ON employee.employee_id = postMan.employee_id WHERE postman_id=?",postman_id);

        if (resultSet.next()){
            return new PostManEmployee(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3));
        }

        return new PostManEmployee();
    }

    public static int getMailCount(String postmanId) throws SQLException {

        int count=0;

        ResultSet resultSet=CrudUtil.execute("SELECT count(mail_id) FROM mails WHERE postman_id=? AND send_date=CURRENT_DATE()",postmanId);

        if (resultSet.next()){

            count+=resultSet.getInt(1);
        }

        ResultSet resultSet1=CrudUtil.execute("SELECT count(mail_id) FROM ems_parcel WHERE postman_id=? AND send_date=CURRENT_DATE()",postmanId);

        if (resultSet1.next()){
            count+=resultSet1.getInt(1);
        }

        return count;

    }

    public static ArrayList<PostManMailDetails> getMailDetails(String postmanId) throws SQLException {

        ArrayList<PostManMailDetails> mailDetails=new ArrayList<>();

        ResultSet resultSet=CrudUtil.execute("SELECT mail_id,receiver_name,receivers_address FROM mails WHERE postman_id=? AND send_date=CURRENT_DATE()",postmanId);

        while (resultSet.next()){
            mailDetails.add(new PostManMailDetails(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)));
        }

        ResultSet resultSets=CrudUtil.execute("SELECT mail_id,receiver_name,receivers_address FROM ems_parcel WHERE postman_id=? AND send_date=CURRENT_DATE()",postmanId);

        while (resultSets.next()){
            mailDetails.add(new PostManMailDetails(resultSets.getString(1),resultSets.getString(2),resultSets.getString(3)));
        }

        return mailDetails;
    }

    public static String generateNextEmployeeId() throws SQLException {
        String sql = "SELECT employee_id FROM employee ORDER BY employee_id DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);

        if(resultSet.next()) {
            return splitEmployeeId(resultSet.getString(1));
        }
        return splitEmployeeId(null);
    }

    private static String splitEmployeeId(String currentEmployeeId) {
        if(currentEmployeeId != null) {
            String[] strings = currentEmployeeId.split("E");
            int id = Integer.parseInt(strings[1]);
            id++;

            return (id<10)?"E00"+id:(id>10)?"E0"+id:"E"+id;

        }
        return "E001";
    }

    public static boolean save(Employee employee) throws SQLException {

        return CrudUtil.execute("INSERT INTO employee VALUES (?,?,?,?,?)",
                employee.getEmployee_id(),employee.getEmployee_name(),employee.getEmployee_contactNo(),
                employee.getEmployee_email(),employee.getEmployee_type());

    }

    public static Employee getEmployeeDetails(String employee_id) throws SQLException {

        ResultSet resultSet=CrudUtil.execute("SELECT * FROM employee WHERE employee_id=?",employee_id);

        if (resultSet.next()) {
            return new Employee(resultSet.getString(1),resultSet.getString(2),
                    resultSet.getString(3),resultSet.getString(4),resultSet.getString(5));
        }

        return new Employee();
    }

    public static boolean update(Employee employee) throws SQLException {

        boolean isUpdated=CrudUtil.execute("UPDATE employee SET employee_name=?,employee_contact_no=?,employee_email=? WHERE employee_id=?",
                employee.getEmployee_name(),employee.getEmployee_contactNo(),employee.getEmployee_email(),employee.getEmployee_id());

        return isUpdated;
    }

    public static boolean update(NewUser newUser, String employee_id) throws SQLException {
        return CrudUtil.execute("UPDATE employee SET employee_name=?,employee_contact_no=?,employee_email=? WHERE employee_id=?",
                newUser.getEmployeeName(),newUser.getEmployeeContactNo(),newUser.getEmployeeEmailAddress(),employee_id);

    }

    public static ArrayList<String> getPostAreas() throws SQLException {
        ArrayList<String> postAreaList=new ArrayList<>();
        ResultSet resultSet=CrudUtil.execute("SELECT post_area FROM postMan");
        while (resultSet.next()){
            postAreaList.add(resultSet.getString(1));
        }
        return postAreaList;
    }
}
