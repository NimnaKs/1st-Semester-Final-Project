package lk.ijse.posm.model;

import lk.ijse.posm.db.DbConnection;
import lk.ijse.posm.dto.CartDTO;
import lk.ijse.posm.dto.Item;
import lk.ijse.posm.dto.ItemRemove;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemModel {

    public static List<String> getCodes() throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();

        List<String> codes = new ArrayList<>();

        String sql = "SELECT item_code FROM Item";
        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while(resultSet.next()) {
            codes.add(resultSet.getString(1));
        }
        return codes;
    }

    public static Item searchById(String code) throws SQLException {
        String sql = "SELECT * FROM Item WHERE item_code = ?";

        ResultSet resultSet = CrudUtil.execute(sql, code);

        if(resultSet.next()) {
            return new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            );
        }
        return null;
    }

    public static int getSelectedItemQtyOnHand(String code) throws SQLException {

        String sql = "SELECT qty_on_hand FROM Item WHERE item_code = ?";

        ResultSet resultSet=CrudUtil.execute(sql,code);

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return 0;
    }

    public static boolean updateQty(List<CartDTO> cartDTOList) throws SQLException {
        for (CartDTO dto : cartDTOList) {
            if(!updateQty(dto)) {
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(CartDTO dto) throws SQLException {

        String sql = "UPDATE Item SET qty_on_hand = (qty_on_hand - ?) WHERE item_code = ?";

        boolean isUpdated = CrudUtil.execute(sql, dto.getQty(), dto.getItem_code());

        return isUpdated;
    }

    public static boolean updateRemoveQty(ArrayList<ItemRemove> itemRemovesList) throws SQLException {
        try {
            for (ItemRemove itemRemove : itemRemovesList) {
                if (!updateRemoveQty(itemRemove)) {
                    return false;
                }
            }
        }catch (NullPointerException exception){
            System.out.println("Null pointer");
        }
        return true;
    }

    private static boolean updateRemoveQty(ItemRemove itemRemove) throws SQLException {

        boolean isUpdateRemoveQty=CrudUtil.execute("UPDATE Item SET qty_on_hand=(qty_on_hand + ?) WHERE item_code=?",itemRemove.getQty(),itemRemove.getItem_code());

        return isUpdateRemoveQty;

    }


}
