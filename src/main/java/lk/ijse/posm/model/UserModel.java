package lk.ijse.posm.model;

import lk.ijse.posm.Launcher;
import lk.ijse.posm.db.DbConnection;
import lk.ijse.posm.dto.Employee;
import lk.ijse.posm.dto.NewUser;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserModel {

    public static String getCredentials(String userName) throws SQLException {

        ResultSet resultSet = CrudUtil.execute("SELECT user_id,password FROM user WHERE user_name=?",userName);

        if (resultSet.next()){
            Launcher.userId=resultSet.getString(1);
            return resultSet.getString(2);
        }

        return null;
    }

    public static boolean isMatch(String currentPW) throws SQLException {

        ResultSet resultSet=CrudUtil.execute("SELECT password FROM user WHERE user_id=?",Launcher.userId);

        if (resultSet.next()){
            return resultSet.getString(1).equals(currentPW);
        }

        return false;
    }

    public static boolean update(String newPW) throws SQLException {

        return CrudUtil.execute("UPDATE user SET password=? WHERE user_id=?",newPW,Launcher.userId);
    }

    public static NewUser getAll() throws SQLException {

        ResultSet resultSet=CrudUtil.execute("SELECT Type,user_id,user_name,employee_name,employee_email,employee_contact_no FROM user INNER JOIN employee ON employee.employee_id=user.employee_id WHERE user_id=?",
                Launcher.userId);

        if (resultSet.next()){
            return new NewUser(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6));
        }

        return new NewUser();
    }

    public static boolean update(NewUser newUser) throws SQLException {

        String employee_id=getEmployeeId(newUser.getUserId());

        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isUpdateUserDetails=update(newUser.getUserName(),newUser.getUserId());

            if (isUpdateUserDetails) {
                boolean isSavedEmployeeDetails =EmployeeModel.update(newUser,employee_id);
                if (isSavedEmployeeDetails) {
                    con.commit();
                    return true;
                }
            }
            return false;
        } catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }

    }

    private static String getEmployeeId(String userId) throws SQLException {

        ResultSet resultSet=CrudUtil.execute("SELECT employee_id FROM user WHERE user_id=?",userId);

        if (resultSet.next()){
            return resultSet.getString(1);
        }

        return null;
    }

    private static boolean update(String userName, String userId) throws SQLException {
        return CrudUtil.execute("UPDATE user SET user_name=? WHERE user_id=?",userName,userId);

    }

    public static String generateNextUserId() throws SQLException {
        String sql = "SELECT user_id FROM user ORDER BY user_id DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);

        if(resultSet.next()) {
            return splitUserId(resultSet.getString(1));
        }
        return splitUserId(null);
    }

    public static String splitUserId(String currentUserId) {
        if(currentUserId != null) {
            String[] strings = currentUserId.split("User");
            int id = Integer.parseInt(strings[1]);
            id++;

            return (id<10)?"User000"+id:(id>10)?"User00"+id:(id>100)?"User0"+id:"User"+id;

        }
        return "User0001";
    }

    public static boolean save(NewUser newUser) throws SQLException {

        String employeeId=EmployeeModel.generateNextEmployeeId();
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isSavedEmployee=EmployeeModel.save(new Employee(employeeId,
                    newUser.getEmployeeName(),newUser.getEmployeeContactNo(),newUser.getEmployeeEmailAddress(),"User"));

            if (isSavedEmployee) {
                boolean isSavedUserDetails =UserModel.save(newUser,employeeId);
                if (isSavedUserDetails) {
                    con.commit();
                    return true;
                }
            }
            return false;
        } catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }

    }

    private static boolean save(NewUser newUser, String employeeId) throws SQLException {

        return CrudUtil.execute("INSERT INTO user VALUES (?,?,?,?,?)",
               newUser.getUserId(),newUser.getUserName(),newUser.getPassword(),newUser.getType(),employeeId );
    }

    public static String getUserName(String userName) throws SQLException {
       ResultSet resultSet= CrudUtil.execute("SELECT employee_name FROM employee INNER JOIN user ON user.employee_id=employee.employee_id WHERE user_name=?",userName);
       if (resultSet.next()){
           return resultSet.getString(1);
       }
       return "N.K Sekara";
    }

    public static ArrayList<String> getUserPasswords() throws SQLException {
        ArrayList<String> passwordList=new ArrayList<>();
        ResultSet resultSet=CrudUtil.execute("SELECT password FROM user");
        while (resultSet.next()){
            passwordList.add(resultSet.getString(1));
        }
        return passwordList;
    }

    public static ArrayList<String> getAllUsername() throws SQLException {
        ArrayList<String> userNameList=new ArrayList<>();
        ResultSet resultSet=CrudUtil.execute("SELECT user_name FROM user");
        while (resultSet.next()){
            userNameList.add(resultSet.getString(1));
        }
        return userNameList;
    }

    public static String getUsreEmail(String userNameAvailable) throws SQLException {
       ResultSet resultSet= (CrudUtil.execute("SELECT employee_email FROM employee INNER JOIN user ON user.employee_id=employee.employee_id WHERE user_name=?",userNameAvailable));
       if (resultSet.next()){
           return resultSet.getString(1);
       }
       return null;
    }
}
