package lk.ijse.posm.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lk.ijse.posm.dto.ProductDTO;
import lk.ijse.posm.util.Interfaces.MyListener;

public class StationaryItemCard {

    @FXML
    private Label itemName;

    @FXML
    private ImageView img;

    private ProductDTO item;

    private MyListener myListener;

    public void setData(ProductDTO item, MyListener myListener) {
        this.item = item;
        this.myListener = myListener;
        itemName.setText(item.getItemName());
        Image image = new Image(item.getImage());
        img.setImage(image);
    }
}
