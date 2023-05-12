package lk.ijse.posm.model;

import lk.ijse.posm.db.DbConnection;
import lk.ijse.posm.dto.Employee;
import lk.ijse.posm.dto.PostmanSave;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostmanModel {

    public static String generateNextPostId() throws SQLException {
        String sql = "SELECT postman_id FROM postMan ORDER BY postman_id DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);

        if(resultSet.next()) {
            return splitPostmanId(resultSet.getString(1));
        }
        return splitPostmanId(null);
    }

    public static String splitPostmanId(String currentPostmanId) {
        if(currentPostmanId != null) {
            String[] strings = currentPostmanId.split("P");
            int id = Integer.parseInt(strings[1]);
            id++;

            return (id<10)?"P00"+id:(id>10)?"P0"+id:"P"+id;

        }
        return "P001";
    }

    public static boolean save(Employee employee, PostmanSave postmanSave) throws SQLException {

        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isEmployeeSave=EmployeeModel.save(employee);
            if (isEmployeeSave) {
                boolean isPostManSaved = save(postmanSave);
                if (isPostManSaved) {
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

    private static boolean save(PostmanSave postmanSave) throws SQLException {

       return CrudUtil.execute("INSERT INTO postMan VALUES (?,?,?,?,?,?)",
               postmanSave.getPostId(),postmanSave.getPostArea(),postmanSave.getVehicleNo(),
               postmanSave.getVehicleType(),postmanSave.getEmployee_Id(),postmanSave.getImage());
    }

    public static PostmanSave getPostmanDetails(String postmanId) throws SQLException {

        ResultSet resultSet=CrudUtil.execute("SELECT * FROM postMan WHERE postman_id=?",postmanId);

        if(resultSet.next()) {
            return new PostmanSave(resultSet.getString(1),
                    resultSet.getString(2), resultSet.getString(3),
                    resultSet.getString(4), resultSet.getString(5),
                    resultSet.getString(6));
        }

        return new PostmanSave();
    }

    public static boolean update(Employee employee, PostmanSave postmanSave) throws SQLException {

        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isEmployeeUpdate=EmployeeModel.update(employee);
            if (isEmployeeUpdate) {
                boolean isPostManUpdate = update(postmanSave);
                if (isPostManUpdate) {
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

    private static boolean update(PostmanSave postmanSave) throws SQLException {

        boolean isUpdated= CrudUtil.execute("UPDATE postman SET post_area=?,vehicle_No=?,vehicle_type=?,image=? WHERE postman_id=?",
                postmanSave.getPostArea(),postmanSave.getVehicleNo(),postmanSave.getVehicleType(),postmanSave.getImage(),
                postmanSave.getPostId());

        return isUpdated;
    }
}
