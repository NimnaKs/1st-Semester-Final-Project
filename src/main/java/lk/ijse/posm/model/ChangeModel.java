package lk.ijse.posm.model;

import lk.ijse.posm.db.DbConnection;
import lk.ijse.posm.dto.*;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ChangeModel {
    public static String generateNextPaymentId() throws SQLException {
        String sql = "SELECT payment_id FROM changes ORDER BY payment_id DESC LIMIT 1";

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

    public static double getReceivingMoneyAmount(String moneyType, double moneyAmount) throws SQLException {

        ResultSet resultSet=CrudUtil.execute("SELECT unit_selling_price FROM money WHERE money_type=?",moneyType);

        if (resultSet.next()){
            return moneyAmount*resultSet.getDouble(1);
        }

        return 0.0;
    }

    public static double getSellingMoneyAmount(String moneyType) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT qty_on_hand FROM money WHERE money_type=?",moneyType);
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    public static double getReceivingMoneyAmountFromAnotherCurrencyType(String moneyType, double moneyAmount) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT unit_getting_price FROM money WHERE money_type=?",moneyType);

        if (resultSet.next()){
            return moneyAmount/resultSet.getDouble(1);
        }

        return 0.0;
    }

    public static boolean save(Customer customer, Change change) throws SQLException{

        Money sellingMoney=CurrencyModel.getSellingMoneyDetails(change.getSellingMoneyType());

        Money receivingMoney=CurrencyModel.getReceivingMoneyDetails(change.getReceivingMoneyType());

        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isSavedCustomer = false;

            if (customer.getCustomer_id().equals(CustomerModel.generateNextCustId())) {
                isSavedCustomer = CustomerModel.newCustomerSave(customer);
            } else {
                isSavedCustomer = CustomerModel.customerUpdate(customer);
            }

            if (isSavedCustomer) {
                boolean isSavedPayment =savePayment(change,customer.getCustomer_id(),sellingMoney,receivingMoney);

                if (isSavedPayment) {

                    boolean isUpdate=CurrencyModel.updateQty(change,sellingMoney,receivingMoney);

                    if (isUpdate) {
                        con.commit();
                        return true;
                    }
                }
            }
            return false;
        }catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }
    }

    private static boolean savePayment(Change change,String customerId,Money sellingMoney,Money receivingMoney) throws SQLException {

        boolean isSaveSellingItem=CrudUtil.execute("INSERT INTO changes VALUES (?,?,?,?,?,?,?)",
                change.getPaymentId(),customerId,sellingMoney.getMoneyId(),change.getSellingMoneyAmount(),
                sellingMoney.getPrice(), LocalDate.now(),2);

        boolean isSaveReceivingItem=false;
        if (isSaveSellingItem){
            isSaveReceivingItem=CrudUtil.execute("INSERT INTO changes VALUES (?,?,?,?,?,?,?)",
                    change.getPaymentId(),customerId,receivingMoney.getMoneyId(),change.getReceivingMoneyAmount(),
                    receivingMoney.getPrice(), LocalDate.now(),1);
        }

        return  isSaveReceivingItem;
    }

    public static Change getMoneyTransferDetails(String paymentId) throws SQLException {

        Change change=new Change();

        change.setPaymentId(paymentId);

        ResultSet resultSet=CrudUtil.execute("SELECT customer_id,money_type,qty,date FROM changes INNER JOIN money ON changes.money_id=money.money_id WHERE payment_id=? AND status=?",
                paymentId,"Selling Money");

        if (resultSet.next()){
            change.setCustomerId(resultSet.getString(1));
            change.setSellingMoneyType(resultSet.getString(2));
            change.setSellingMoneyAmount(resultSet.getDouble(3));
            change.setPaymentDate(resultSet.getString(4));
        }

        ResultSet resultSet1=CrudUtil.execute("SELECT money_type,qty FROM changes INNER JOIN money ON changes.money_id=money.money_id WHERE payment_id=? AND status=?",
                paymentId,"Getting Money");
        if (resultSet1.next()){
            change.setReceivingMoneyType(resultSet1.getString(1));
            change.setReceivingMoneyAmount(resultSet1.getDouble(2));
        }

        return change;
    }

    public static boolean remove(Change change) throws SQLException {

        Money sellingMoney=CurrencyModel.getSellingMoneyDetails(change.getSellingMoneyType());

        Money receivingMoney=CurrencyModel.getReceivingMoneyDetails(change.getReceivingMoneyType());

        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isRemoveMoney=CurrencyModel.remove(change.getPaymentId());

            System.out.println("Is Remove Money "+isRemoveMoney);

            if (isRemoveMoney) {

                boolean isRemoveSellingMoney = CurrencyModel.removeSellingMoney(change,sellingMoney);

                System.out.println("Is Remove Selling Money "+isRemoveSellingMoney);

                if (isRemoveSellingMoney) {

                    boolean isRemoveReceivingMoney = CurrencyModel.removeReceivingMoney(change,receivingMoney);

                    System.out.println("Is Remove Receiving Money "+isRemoveReceivingMoney);

                    if (isRemoveReceivingMoney) {
                        con.commit();
                        return true;
                    }
                }
            }
            return false;
        }catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }

    }

    public static boolean update(Customer customer, Change change) throws SQLException {

        Money sellingMoney=CurrencyModel.getSellingMoneyQty(change.getPaymentId());

        Money receivingMoney=CurrencyModel.getReceivingMoneyQty(change.getPaymentId());


        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isSavedCustomer = false;

            if (customer.getCustomer_id().equals(CustomerModel.generateNextCustId())) {
                isSavedCustomer = CustomerModel.newCustomerSave(customer);
            } else {
                isSavedCustomer = CustomerModel.customerUpdate(customer);
            }
            if (isSavedCustomer) {

                boolean isUpdateChange=CurrencyModel.updateChanges(change,sellingMoney,receivingMoney);

                if (isUpdateChange){
                    boolean isUpdateMoney=CurrencyModel.updateMoneyQty(change,sellingMoney,receivingMoney);
                    if (isUpdateChange){
                        con.commit();
                        return true;
                    }
                }

            }
            return false;
        }catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }

    }

    public static ArrayList<MonthIncome> getChangeIncome() throws SQLException {
        ArrayList<MonthIncome> changeDetailsIncome=new ArrayList<>();
        ArrayList<String> monthList=new ArrayList<>(Arrays.asList("January","February","March","April","May","June","July","August","October","November","December"));
        for (int i = 0; i < monthList.size(); i++) {
            ResultSet resultSet=CrudUtil.execute("SELECT SUM(qty) FROM changes WHERE MONTH(date)=? AND status='Getting Money'",i+1);
            if (resultSet.next()){
                changeDetailsIncome.add(new MonthIncome(monthList.get(i),resultSet.getDouble(1)/100*5));
            }
        }
        return changeDetailsIncome;
    }

    public static ArrayList<String> getPaymentIds() throws SQLException {
        ArrayList<String> paymentIds=new ArrayList<>();
        ResultSet resultSet=CrudUtil.execute("SELECT payment_id FROM changes");
        while (resultSet.next()){
            paymentIds.add(resultSet.getString(1));
        }
        return paymentIds;
    }

    public static boolean isTodayChangesOk() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT COUNT(payment_id) FROM changes WHERE date=current_date");
        if (resultSet.next()){
            if (resultSet.getInt(1)>0){
                return true;
            }
        }
        return false;
    }
}

