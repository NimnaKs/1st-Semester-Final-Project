package lk.ijse.posm.model;

import lk.ijse.posm.dto.MonthIncome;
import lk.ijse.posm.dto.Payment;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class PaymentModel {
    public static String generateNextPaymentId() throws SQLException {
        String sql = "SELECT payment_id FROM bill_payment ORDER BY payment_id DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);

        if(resultSet.next()) {
            return splitPaymentId(resultSet.getString(1));
        }
        return splitPaymentId(null);
    }

    private static String splitPaymentId(String currentOrderId) {
        if(currentOrderId != null) {
            String[] strings = currentOrderId.split("Pay");
            int id = Integer.parseInt(strings[1]);
            id++;

            return (id<10)?"Pay00"+id:(id>=10)?"Pay0"+id:"Pay"+id;
        }
        return "Pay001";
    }

    public static boolean savePayment(Payment payment) throws SQLException {

        boolean isSavedPayment=CrudUtil.execute("INSERT INTO bill_payment VALUES (?,?,?,?,?,?,?,?,?)",
                         payment.getPayment_id(),
                         payment.getBill_owner_name(),
                         payment.getBill_type(),
                         payment.getCompany_name(),
                         payment.getReferenceNo(),
                         payment.getPayment_Date(),
                         payment.getCustomer_id(),
                         payment.getUser_id(),
                         payment.getPaymentAmount());

        return  isSavedPayment;
    }

    public static Payment getData(String paymentId) throws SQLException {

        ResultSet resultSet=CrudUtil.execute("SELECT * FROM bill_payment WHERE payment_id=?",paymentId);

        if (resultSet.next()){
            return new Payment(resultSet.getString(1),
                               resultSet.getString(2),
                               resultSet.getString(3),
                               resultSet.getString(4),
                                resultSet.getString(5),
                                resultSet.getString(6),
                                resultSet.getString(7),
                                resultSet.getString(8),
                                resultSet.getDouble(9));
        }

        return new Payment();
    }

    public static boolean remove(String paymentId) throws SQLException {

       boolean isRemove= CrudUtil.execute("DELETE FROM bill_payment WHERE payment_id=?",paymentId);

       return  isRemove;

    }

    public static boolean updatePayment(Payment payment) throws SQLException {

        boolean isUpdate=CrudUtil.execute("UPDATE bill_payment SET bill_owner_name=?,bill_type=?,company_name=?,reference_no=?,payment_date=?,customer_id=?,payment=? WHERE payment_id=?",
                        payment.getBill_owner_name(),payment.getBill_type(),payment.getCompany_name(),payment.getReferenceNo(),
                        payment.getPayment_Date(),payment.getCustomer_id(),payment.getPaymentAmount(),payment.getPayment_id());
        return isUpdate;
    }

    public static ArrayList<MonthIncome> getBillPaymentIncome() throws SQLException {
        ArrayList<MonthIncome> billPaymentIncome=new ArrayList<>();
        ArrayList<String> monthList=new ArrayList<>(Arrays.asList("January","February","March","April","May","June","July","August","October","November","December"));
        for (int i = 0; i < monthList.size(); i++) {
            ResultSet resultSet=CrudUtil.execute("SELECT SUM(payment) FROM bill_payment WHERE MONTH(payment_date)=?",i+1);
            if (resultSet.next()){
                billPaymentIncome.add(new MonthIncome(monthList.get(i),resultSet.getDouble(1)/100*5));
            }
        }
        return billPaymentIncome;
    }

    public static ArrayList<String> getPaymentIds() throws SQLException {
        ArrayList<String> paymentIds=new ArrayList<>();
        ResultSet resultSet=CrudUtil.execute("SELECT payment_id FROM bill_payment");
        while (resultSet.next()){
            paymentIds.add(resultSet.getString(1));
        }
        return paymentIds;
    }

    public static boolean isTodayPaymentsOk() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT COUNT(payment_id) FROM bill_payment WHERE payment_date=current_date");
        if (resultSet.next()){
            if (resultSet.getInt(1)>0){
                return true;
            }
        }
        return false;
    }
}
