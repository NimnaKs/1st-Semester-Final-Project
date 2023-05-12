package lk.ijse.posm.model;

import lk.ijse.posm.dto.Change;
import lk.ijse.posm.dto.Money;
import lk.ijse.posm.dto.Tm.CurrencyTM;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CurrencyModel {
    public static ArrayList<CurrencyTM> getAll() throws SQLException {

        ArrayList<CurrencyTM> currencyTMArrayList = new ArrayList<>();
        CurrencyTM currencyTM=null;

        ResultSet resultSet=CrudUtil.execute("SELECT money_type,unit_selling_price,unit_getting_price,qty_on_hand FROM money");

        while(resultSet.next()){

            String moneyType=resultSet.getString(1);
            double unitSellingPrice=resultSet.getDouble(2);
            double unitGettingPrice= resultSet.getDouble(3);
            double qty=resultSet.getDouble(4);

            currencyTMArrayList.add(new CurrencyTM(moneyType,unitSellingPrice,unitGettingPrice,qty));

        }

        return currencyTMArrayList;
    }

    public static boolean save(CurrencyTM currencyTM) throws SQLException {

        boolean isSaved=CrudUtil.execute("INSERT INTO money VALUES (?,?,?,?,?)",
                generateNextMoneyId(),
                currencyTM.getMoneyType(),
                currencyTM.getUnitSellingPrice(),
                currencyTM.getUnitGettingPrice(),
                currencyTM.getQtyOnHand());

        return isSaved;

    }

    public static String generateNextMoneyId() throws SQLException {

        String sql = "SELECT money_id FROM money ORDER BY money_id DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);

        if(resultSet.next()) {
            return splitMoneyId(resultSet.getString(1));
        }
        return splitMoneyId(null);
    }

    public static String splitMoneyId(String currentMoneyId) {
        if(currentMoneyId != null) {
            String[] strings = currentMoneyId.split("M");
            int id = Integer.parseInt(strings[1]);
            id++;

            return (id<10)?"M00"+id:(id>10)?"M0"+id:"M"+id;
        }
        return "M001";
    }

    public static boolean update(CurrencyTM currencyTM) throws SQLException {

        boolean isUpdate=CrudUtil.execute("UPDATE money SET unit_selling_price=?,unit_getting_price=? WHERE money_type=?",
                        currencyTM.getUnitSellingPrice(),currencyTM.getUnitGettingPrice(),currencyTM.getMoneyType());

        return isUpdate;

    }

    public static Money getSellingMoneyDetails(String sellingMoneyType) throws SQLException {

        ResultSet resultSet=CrudUtil.execute("SELECT money_id,unit_selling_price FROM money WHERE money_type=?",sellingMoneyType);

        if (resultSet.next()){
            return new Money(resultSet.getString(1),resultSet.getDouble(2));
        }

        return new Money();
    }

    public static Money getReceivingMoneyDetails(String receivingMoneyType) throws SQLException {

        ResultSet resultSet=CrudUtil.execute("SELECT money_id,unit_getting_price FROM money WHERE money_type=?",receivingMoneyType);

        if (resultSet.next()){
            return new Money(resultSet.getString(1),resultSet.getDouble(2));
        }

        return new Money();
    }

    public static boolean updateQty(Change change, Money sellingMoney, Money receivingMoney) throws SQLException {

        boolean isUpdateSellingQty=CrudUtil.execute("UPDATE money SET qty_on_hand = (qty_on_hand - ?) WHERE money_id = ?",change.getSellingMoneyAmount(),sellingMoney.getMoneyId());

        boolean isUpdateReceivingQty=false;

        if (isUpdateSellingQty){
            isUpdateReceivingQty=CrudUtil.execute("UPDATE money SET qty_on_hand = (qty_on_hand + ?) WHERE money_id = ?",change.getReceivingMoneyAmount(),receivingMoney.getMoneyId());
        }

        return isUpdateReceivingQty;
    }

    public static boolean removeSellingMoney(Change change,Money sellingMoney) throws SQLException {
        return CrudUtil.execute("UPDATE money SET qty_on_hand = (qty_on_hand + ?) WHERE money_id = ?",change.getSellingMoneyAmount(),sellingMoney.getMoneyId());
    }

    public static boolean removeReceivingMoney(Change change,Money receivingMoney) throws SQLException {
        return CrudUtil.execute("UPDATE money SET qty_on_hand = (qty_on_hand - ?) WHERE money_id = ?",change.getReceivingMoneyAmount(),receivingMoney.getMoneyId());
    }

    public static boolean remove(String paymentId) throws SQLException {
        return CrudUtil.execute("DELETE FROM changes WHERE payment_id=?",paymentId);
    }

    public static Money getSellingMoneyQty(String paymentId) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT money_id,qty FROM changes WHERE payment_id=? AND status='Selling Money'",paymentId);
        if (resultSet.next()){
            return new Money(resultSet.getString(1),resultSet.getDouble(2));
        }

        return new Money();
    }

    public static Money getReceivingMoneyQty(String paymentId) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT money_id,qty FROM changes WHERE payment_id=? AND status='Getting Money'",paymentId);
        if (resultSet.next()){
            return new Money(resultSet.getString(1),resultSet.getDouble(2));
        }

        return new Money();
    }

    public static boolean updateChanges(Change change, Money sellingMoney, Money receivingMoney) throws SQLException {
        boolean isSellingMoneyUpdate= CrudUtil.execute("UPDATE changes SET qty=? WHERE payment_id=? AND money_id=?",change.getSellingMoneyAmount(),change.getPaymentId(),sellingMoney.getMoneyId());
        boolean isReceivingMoneyUpdate=false;
        if (isSellingMoneyUpdate){
            isReceivingMoneyUpdate=CrudUtil.execute("UPDATE changes SET qty=? WHERE payment_id=? AND money_id=?",change.getReceivingMoneyAmount(),change.getPaymentId(),receivingMoney.getMoneyId());
        }
        return  isReceivingMoneyUpdate;
    }

    public static boolean updateMoneyQty(Change change, Money sellingMoney, Money receivingMoney) throws SQLException {
        boolean isUpdateSellingMoney=CrudUtil.execute("UPDATE money SET qty_on_hand=(qty_on_hand+?-?) WHERE money_id=?",sellingMoney.getPrice(),change.getSellingMoneyAmount(),sellingMoney.getMoneyId());
        boolean isUpdateReceivingMoney=false;
        if (isUpdateSellingMoney){
            isUpdateReceivingMoney=CrudUtil.execute("UPDATE money SET qty_on_hand=(qty_on_hand-?+?) WHERE money_id=?",receivingMoney.getPrice(),change.getReceivingMoneyAmount(),receivingMoney.getMoneyId());
        }
        return isUpdateReceivingMoney;
    }
}
