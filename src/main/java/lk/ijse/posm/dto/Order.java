package lk.ijse.posm.dto;

import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.SQLException;

public class Order {

    private String orderId;

    private String date;

    private String customerId;

    public Order(String orderId, String date, String customerId) {
        this.orderId = orderId;
        this.date = date;
        this.customerId = customerId;
    }

    public Order() {
    }

    public static void remove(String oId) throws SQLException {

        String sql="DELETE FROM orders WHERE order_id=?";
        CrudUtil.execute(sql,oId);

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
