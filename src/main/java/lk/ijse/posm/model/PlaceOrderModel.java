package lk.ijse.posm.model;

import lk.ijse.posm.db.DbConnection;
import lk.ijse.posm.dto.CartDTO;
import lk.ijse.posm.dto.ItemRemove;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderModel {

    public static boolean placeOrder(String oId, String cusId, String customerNameText, List<CartDTO> cartDTOList) throws SQLException {

        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isCustomerSaved=CustomerModel.newCustomerSave(cusId,customerNameText);
            if (isCustomerSaved) {
                boolean isSaved = OrderModel.save(oId, cusId, LocalDate.now());
                if (isSaved) {
                    boolean isUpdated = ItemModel.updateQty(cartDTOList);
                    if (isUpdated) {
                        boolean isOrderDetailSaved = OrderDetailModel.save(oId, cartDTOList);
                        if (isOrderDetailSaved) {
                            con.commit();
                            return true;
                        }
                    }
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

    public static boolean placeOrder(String oId, String cusId, List<CartDTO> cartDTOList) throws SQLException {
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isSaved = OrderModel.save(oId, cusId, LocalDate.now());
            if (isSaved) {
                boolean isUpdated = ItemModel.updateQty(cartDTOList);
                if (isUpdated) {
                    boolean isOrderDetailSaved = OrderDetailModel.save(oId, cartDTOList);
                    if (isOrderDetailSaved) {
                        con.commit();
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            System.out.println("finally");
            con.setAutoCommit(true);
        }
    }

    public static boolean removeOrder(String oId) throws SQLException {
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            ArrayList<ItemRemove> itemRemovesList=new ArrayList<>();

            itemRemovesList=OrderDetailModel.getQty(oId);

            boolean isUpdateRemoveQty=ItemModel.updateRemoveQty(itemRemovesList);

            if (isUpdateRemoveQty){

                boolean isRemoveOrderId=OrderDetailModel.remove(oId);

                if (isRemoveOrderId){

                    boolean isRemoveOrder=OrderModel.remove(oId);

                    if (isRemoveOrder){
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

    public static boolean UpdateOrder(String oId, String cusId, String customerNameText, List<CartDTO> cartDTOList) throws SQLException {

        boolean isRemove=removeOrder(oId);

        boolean isUpdate=false;
        if (isRemove){
            isUpdate=placeOrder(oId,cusId,customerNameText,cartDTOList);
        }
        return isUpdate;

    }

    public static boolean UpdateOrder(String oId, String cusId, List<CartDTO> cartDTOList) throws SQLException {
        boolean isRemove=removeOrder(oId);

        boolean isUpdate=false;
        if (isRemove){
            isUpdate=placeOrder(oId,cusId,cartDTOList);
        }
        return isUpdate;
    }
}
