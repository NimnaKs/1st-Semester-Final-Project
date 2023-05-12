package lk.ijse.posm.model;

import javafx.scene.control.Button;
import lk.ijse.posm.dto.CartDTO;
import lk.ijse.posm.dto.ItemRemove;
import lk.ijse.posm.dto.Tm.OrderTM;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailModel {

    public static boolean save(String oId, List<CartDTO> cartDTOList) throws SQLException {
        for(CartDTO dto :  cartDTOList) {
            if(!save(oId, dto)) {
                return false;
            }
        }
        return true;
    }

    private static boolean save(String oId, CartDTO dto) throws SQLException {

        String sql = "INSERT INTO order_details VALUES (?, ?, ?, ?)";

        boolean isSaved=CrudUtil.execute(sql,dto.getQty(),dto.getUnitPrice(),dto.getItem_code(),oId);

        return isSaved;

    }

    public static ArrayList<OrderTM> getDetails(String oId) throws SQLException {

        String sql="SELECT item.item_code,item_type,qty,item_price FROM order_details INNER JOIN item ON order_details.item_code=item.item_code WHERE order_id=?";

        ResultSet resultSet=CrudUtil.execute(sql,oId);

        ArrayList<OrderTM> list=new ArrayList<>();

        while (resultSet.next()){
            String code=resultSet.getString(1);
            String description=resultSet.getString(2);
            int qty=resultSet.getInt(3);
            double unitPriced=resultSet.getDouble(4);

            OrderTM orderTm=new OrderTM(code,description,qty,unitPriced,qty*unitPriced,new Button("Remove"));

            list.add(orderTm);
        }

        return list;
    }

    public static boolean remove(String oId) throws SQLException {

        String sql="DELETE FROM order_details WHERE order_id=?";
        boolean isRemove=CrudUtil.execute(sql,oId);

        return isRemove;
    }

    public static ArrayList<ItemRemove> getQty(String oId) throws SQLException {

        ResultSet resultSet=CrudUtil.execute("SELECT item_code,qty FROM order_details WHERE order_id=?",oId);

        ArrayList<ItemRemove> itemRemovesList=new ArrayList<>();

        while (resultSet.next()){
            ItemRemove itemRemove = new ItemRemove(resultSet.getString(1), resultSet.getInt(2));
            itemRemovesList.add(itemRemove);
        }

        return itemRemovesList;
    }
}
