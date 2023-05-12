package lk.ijse.posm.model;

import lk.ijse.posm.dto.MonthIncome;
import lk.ijse.posm.dto.Order;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderModel {
    public static String generateNexOrderId() throws SQLException {
        String sql = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);

        if(resultSet.next()) {
            return splitOrderId(resultSet.getString(1));
        }
        return splitOrderId(null);
    }

    private static String splitOrderId(String currentOrderId) {
        if(currentOrderId != null) {
            String[] strings = currentOrderId.split("Order");
            int id = Integer.parseInt(strings[1]);
            id++;

            return (id<10)?"Order00"+id:(id>=10)?"Order0"+id:"Order"+id;
        }
        return "Order001";
    }

    public static boolean save(String oId, String cusId, LocalDate now) throws SQLException {
        String sql = "INSERT INTO Orders VALUES (?, ?, ?)";

        boolean isSaved = CrudUtil.execute(sql, oId, now, cusId);

        return isSaved;

    }

    public static Order getOrderDetails(String oId) throws SQLException {

        String sql="SELECT * FROM orders WHERE order_id=?";

        ResultSet resultSet=CrudUtil.execute(sql,oId);

        if (resultSet.next()) {
            return new Order(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
        }

        return new Order();
    }

    public static boolean remove(String oId) throws SQLException {

        boolean isRemove=CrudUtil.execute("DELETE FROM orders WHERE order_id=?",oId);

        return isRemove;

    }

    public static Double getTotalSalesIncome() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT SUM(qty*item_price) FROM order_details INNER JOIN orders ON order_details.order_id=orders.order_id WHERE order_date=CURRENT_DATE()");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    public static int getTotalOrders() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT COUNT(order_id) FROM orders WHERE order_date=CURRENT_DATE()");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static int getTotalItemSales() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT SUM(qty) FROM order_details INNER JOIN orders ON orders.order_id=order_details.order_id WHERE order_date=CURRENT_DATE()");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static ArrayList<MonthIncome> getOrderTotalInEachMonth() throws SQLException {

        ArrayList<MonthIncome> orderIncomeList=new ArrayList<>();
        ArrayList<String> monthList=new ArrayList<>(Arrays.asList("January","February","March","April","May","June","July","August","October","November","December"));
        for (int i = 0; i < monthList.size(); i++) {
            ResultSet resultSet=CrudUtil.execute("SELECT SUM(qty*item_price) FROM order_details INNER JOIN orders ON order_details.order_id=orders.order_id WHERE MONTH(order_date)=?",i+1);
            if (resultSet.next()){
                orderIncomeList.add(new MonthIncome(monthList.get(i),resultSet.getDouble(1)));
            }
        }
        return orderIncomeList;

    }

    public static ArrayList<String> getOrderIds() throws SQLException {
        ArrayList<String> orderIds=new ArrayList<>();
        ResultSet resultSet=CrudUtil.execute("SELECT order_id FROM orders");
        while (resultSet.next()){
            orderIds.add(resultSet.getString(1));
        }
        return orderIds;
    }
}
