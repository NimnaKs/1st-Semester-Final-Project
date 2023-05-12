package lk.ijse.posm.model;

import lk.ijse.posm.dto.ProductDTO;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductModel {

    public static ArrayList<ProductDTO> getData() throws SQLException {

        ArrayList<ProductDTO> itemList=new ArrayList<>();

        ResultSet resultSet=CrudUtil.execute("SELECT * FROM item");

        while (resultSet.next()){
            itemList.add(new ProductDTO(resultSet.getString(1),resultSet.getString(2),
                    resultSet.getInt(3),resultSet.getDouble(4),
                    resultSet.getString(5)));
        }

        return itemList;
    }

    public static String generateNextItemId() throws SQLException {
        String sql = "SELECT item_code FROM item ORDER BY item_code DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);

        if(resultSet.next()) {
            return splitItemId(resultSet.getString(1));
        }
        return splitItemId(null);
    }

    public static String splitItemId(String currentItemId) {
        if(currentItemId != null) {
            String[] strings = currentItemId.split("I");
            int id = Integer.parseInt(strings[1]);
            id++;

            return (id<10)?"I00"+id:(id>10)?"I0"+id:"I"+id;
        }
        return "I001";
    }

    public static boolean save(ProductDTO productDTO) throws SQLException {
        return CrudUtil.execute("INSERT INTO item VALUES (?,?,?,?,?)",
                productDTO.getItemCode(),productDTO.getItemName(),productDTO.getQtyAvailable(),
                productDTO.getPrice(),productDTO.getImage());
    }

    public static boolean update(ProductDTO productDTO) throws SQLException {
        return CrudUtil.execute("UPDATE item SET item_type=?,qty_on_hand=?,unit_price=?,image=? WHERE item_code=?",
                productDTO.getItemName(),productDTO.getQtyAvailable(),
                productDTO.getPrice(),productDTO.getImage(),productDTO.getItemCode());
    }
}
