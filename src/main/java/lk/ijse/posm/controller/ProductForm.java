package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import lk.ijse.posm.db.DbConnection;
import lk.ijse.posm.dto.ProductDTO;
import lk.ijse.posm.model.ProductModel;
import lk.ijse.posm.util.Interfaces.MainController;
import lk.ijse.posm.util.Interfaces.MyListener;
import lk.ijse.posm.util.ValidationPattern.RegExPatterns;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductForm implements Initializable, MainController {

    @FXML
    private ImageView image;

    @FXML
    private JFXButton save;

    @FXML
    private JFXTextField price;

    @FXML
    private JFXTextField imagePath;

    @FXML
    private JFXTextField itemName;

    @FXML
    private JFXTextField QtyAvailable;

    @FXML
    private JFXComboBox<String> itemComboBox;

    @FXML
    private GridPane gridPane;

    @FXML
    private JFXButton oderDetails;

    @FXML
    private JFXButton addItem;

    @FXML
    private Label dateAndTime;

    private volatile boolean stop=false;

    private  ObservableList<String> itemCodeList=null;

    private MyListener myListener;

    private String CURRENCY="Rs";

    private ArrayList<ProductDTO> itemList = new ArrayList<>();

    private Image images;

    private boolean isItemNameOk=false;

    private boolean isPriceOk=false;

    private boolean isQtyAvailableOk=false;

    private boolean isImgPath=false;

    private boolean isItemIdOk=false;

    private void setChosenItem(ProductDTO item) {
        itemName.setText(item.getItemName());
        price.setText(CURRENCY+"." + item.getPrice());
        images = new Image(item.getImage());
        image.setImage(images);
        QtyAvailable.setText(item.getQtyAvailable()+"");
        imagePath.setText(item.getImage());
    }

    @FXML
    void onActionAddItem(ActionEvent event) {
        String itemCode=null;
        try {
            itemCode=ProductModel.generateNextItemId();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"SQL ERROR .Please Try Again Later !").showAndWait();
        }

        itemCodeList.add(itemCode);

        itemComboBox.getSelectionModel().select(itemCode);

        itemName.setEditable(true);

        save.setText("Save");

    }

    @FXML
    void onActionOrderDetails(ActionEvent event) throws FileNotFoundException, JRException, SQLException {
        InputStream input = new FileInputStream(new File("src/main/resources/report/ItemReport.jrxml"));
        JasperDesign jasperDesign = JRXmlLoader.load(input);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null, DbConnection.getInstance().getConnection());

        JasperViewer.viewReport(jasperPrint,false);
    }

    @FXML
    void onActionSave(ActionEvent event) {
        String unit_price=price.getText();
        String[] strings = unit_price.split("Rs.");
        double item_price = Double.parseDouble(strings[1]);

        if (save.getText().equals("Save")){
            try {
                boolean isSave=ProductModel.save(new ProductDTO(itemComboBox.getValue(),itemName.getText(),Integer.parseInt(QtyAvailable.getText()),item_price,
                        imagePath.getText()));
                if (isSave) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved Successfully !!").showAndWait();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Saved UnSuccessful !!").showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"SQL ERROR.Please Try Again Later !").showAndWait();
            }
        }else{
            try {
                boolean isUpdated=ProductModel.update(new ProductDTO(itemComboBox.getValue(),itemName.getText(),Integer.parseInt(QtyAvailable.getText()),item_price,
                        imagePath.getText()));
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Updated Successfully !!").showAndWait();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Updated UnSuccessful !!").showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"SQL ERROR.Please Try Again Later !").showAndWait();
            }
        }

        makeAllNull();
        save.setText("Save");
        setBooleanFalse(false);
        isButtonsEnable();
    }

    private void makeAllNull() {
        itemName.setText(null);
        itemName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        price.setText(CURRENCY);
        price.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        image.setImage(null);
        QtyAvailable.setText(null);
        QtyAvailable.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        imagePath.setText(null);
        imagePath.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        setItem();
        itemComboBox.getSelectionModel().select(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setItem();

        setItemCodeComboBox();

        save.setText("Save");

        setDateAndTime();

        setBooleanFalse(false);
        isButtonsEnable();
    }

    private void setItem() {
        try {
            itemList=ProductModel.getData();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"SQL Error.Please Try Again Later").showAndWait();
        }

        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < itemList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/stationaryItemCard.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                StationaryItemCard stationaryItemCard = fxmlLoader.getController();
                stationaryItemCard.setData(itemList.get(i), myListener);

                if (column == 2) {
                    column = 0;
                    row++;
                }

                gridPane.add(anchorPane, ++column, row); //(child,column,row)
                //set grid width
                gridPane.setMinWidth(540);
                gridPane.setPrefWidth(540);
                gridPane.setMaxWidth(540);

                //set grid height
                gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setItemCodeComboBox() {

        itemCodeList= FXCollections.observableArrayList();
        for (ProductDTO item : itemList){
            itemCodeList.add(item.getItemCode());
        }
        itemComboBox.setItems(itemCodeList);
    }

    @FXML
    void onActionSetItemDetails(ActionEvent event) {
        if (itemComboBox.getValue()==null){
            isItemIdOk=false;
        }else{
            isItemIdOk=true;
        }
        isButtonsEnable();
        try{
            for (ProductDTO item : itemList){
                if (item.getItemCode().equals(itemComboBox.getValue())){
                    setChosenItem(item);
                }
            }
        }catch (NullPointerException exception){
            exception.printStackTrace();
        }
       itemName.setEditable(false);
        save.setText("Update");
    }

    @FXML
    void onActionSetImage(ActionEvent event) {
        try {
            images = new Image(imagePath.getText());
            image.setImage(images);
        }catch (NullPointerException e){
            System.out.println("Null Pointer");
        }
    }

    private Thread setDateAndTime() {
        Thread thread=new Thread(()->{
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM HH:mm");
            while (!stop){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Date date = new Date();
                String dateStr = formatter.format(date);
                Platform.runLater(()->{
                    dateAndTime.setText(dateStr);
                });
            }
        });
        thread.start();
        return thread;
    }

    @Override
    public void setStopTrue(){
        stop=true;
        Thread thread=setDateAndTime();
        thread.interrupt();
    }

    @FXML
    void onActionCheckImg(KeyEvent event) {
        String imgEmp=imagePath.getText();
        if (imgEmp.endsWith(".png") || imgEmp.endsWith(".jpg") ){
            imagePath.setUnFocusColor(Paint.valueOf("blue"));
            this.isImgPath=true;
        }else{
            imagePath.setUnFocusColor(Paint.valueOf("red"));
            this.isImgPath=false;
        }
        isButtonsEnable();
    }

    @FXML
    void onActionCheckItemName(KeyEvent event) {
        try {
            Pattern compile = RegExPatterns.getItemNamePattern();
            Matcher matcher = compile.matcher(itemName.getText());
            boolean matches = matcher.matches();
            if (matches) {
                itemName.setUnFocusColor(Paint.valueOf("blue"));
                this.isItemNameOk = true;
            } else {
                itemName.setUnFocusColor(Paint.valueOf("red"));
                this.isItemNameOk = false;
            }
            isButtonsEnable();
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    void onActionCheckPrice(KeyEvent event) {

        Pattern compile= RegExPatterns.getCurrencyPricePattern();
        Matcher matcher=compile.matcher(price.getText());
        boolean matches=matcher.matches();
        if (matches){
            price.setUnFocusColor(Paint.valueOf("blue"));
            this.isPriceOk=true;
        }else{
            price.setUnFocusColor(Paint.valueOf("red"));
            this.isPriceOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    void onActionCheckQty(KeyEvent event) {
        Pattern compile= RegExPatterns.getIntPattern();
        Matcher matcher=compile.matcher(QtyAvailable.getText());
        boolean matches=matcher.matches();
        if (matches){
            QtyAvailable.setUnFocusColor(Paint.valueOf("blue"));
            this.isQtyAvailableOk=true;
        }else{
            QtyAvailable.setUnFocusColor(Paint.valueOf("red"));
            this.isQtyAvailableOk=false;
        }

        isButtonsEnable();
    }

    private void isButtonsEnable() {
        if (isImgPath&&isItemIdOk&&isItemNameOk&&isPriceOk&&isQtyAvailableOk){
            setButtonDisable(false);
        }else{
            setButtonDisable(true);
        }
    }

    private void setButtonDisable(boolean isDisable) {
        save.setDisable(isDisable);
    }

    private void setBooleanFalse(boolean isFalse){
        isItemIdOk=isFalse;
        isPriceOk=isFalse;
        isItemNameOk=isFalse;
        isPriceOk=isFalse;
        isQtyAvailableOk=isFalse;
    }
}




