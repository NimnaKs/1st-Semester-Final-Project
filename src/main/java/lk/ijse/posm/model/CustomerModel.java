package lk.ijse.posm.model;

import lk.ijse.posm.Launcher;
import lk.ijse.posm.db.DbConnection;
import lk.ijse.posm.dto.Customer;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {

    public static List<String> getIds() throws SQLException {

        Connection con = DbConnection.getInstance().getConnection();
        String sql = "SELECT customer_id FROM customer";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            ids.add(resultSet.getString(1));
        }
        return ids;

    }

    public static Customer searchById(String cus_id) throws SQLException {

        String sql = "SELECT customer_name,customer_contact FROM Customer WHERE customer_id = ?";

        ResultSet resultSet = CrudUtil.execute(sql,cus_id);

        if (resultSet.next()) {
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2)
            );
        }
        return null;
    }

    public static String generateNextCustId() throws SQLException {
        String sql = "SELECT customer_id FROM customer ORDER BY customer_id DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);

        if(resultSet.next()) {
            return splitCustId(resultSet.getString(1));
        }
        return splitCustId(null);
    }

    private static String splitCustId(String customerId) {
        if(customerId != null) {
            String[] strings = customerId.split("C");
            int id = Integer.parseInt(strings[1]);
            id++;

            return (id<10)?"C00"+id:(id>10)?"C0"+id:"C"+id;
        }
        return "C001";
    }

    public static boolean newCustomerSave(String cusId, String customerNameText) throws SQLException {

        String sql="INSERT INTO customer VALUES(?,?,?,?)";

        boolean isSaved=CrudUtil.execute(sql,cusId,customerNameText,"000000000", Launcher.userId);

        return isSaved;

    }

    public static String getCustomerDetails(String customerId) throws SQLException {

        String sql="SELECT customer_name FROM customer WHERE customer_id=?";

        ResultSet resultSet=CrudUtil.execute(sql,customerId);

        if (resultSet.next()){
            return resultSet.getString(1);
        }

        return null;
    }

    public static boolean newCustomerSave(Customer customer) throws SQLException {

        boolean isSaved=CrudUtil.execute("INSERT INTO customer VALUES (?,?,?,?)",
                        customer.getCustomer_id(),
                        customer.getCustomer_name(),
                        customer.getCustomer_contact(),
                        Launcher.userId);

        return isSaved;
    }

    public static boolean customerUpdate(Customer customer) throws SQLException {

        boolean isUpdated=CrudUtil.execute("UPDATE customer SET customer_name=?,customer_contact=? WHERE customer_id=?",
                            customer.getCustomer_name(),
                            customer.getCustomer_contact(),
                            customer.getCustomer_id());

        return  isUpdated;
    }
}
