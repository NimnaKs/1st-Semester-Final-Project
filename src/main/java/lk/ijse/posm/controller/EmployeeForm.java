package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lk.ijse.posm.Launcher;
import lk.ijse.posm.dto.PostManEmployee;
import lk.ijse.posm.dto.PostManMailDetails;
import lk.ijse.posm.model.EmployeeModel;
import lk.ijse.posm.util.Interfaces.MainController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class EmployeeForm implements Initializable, MainController {

    @FXML
    private AnchorPane root;

    @FXML
    private Label stationaryName;

    @FXML
    private JFXButton updateEmployee;

    @FXML
    private ComboBox<String> idsComboBox;

    @FXML
    private Label postManName;

    @FXML
    private Label todayDate;

    @FXML
    private Label postManArea;

    @FXML
    private Label postQty;

    @FXML
    private JFXButton addEmployeeBtn;

    @FXML
    private TableView<PostManMailDetails> postManTable;

    @FXML
    private TableColumn<PostManMailDetails,String> colMailId;

    @FXML
    private TableColumn<PostManMailDetails,String> colReceiversName;

    @FXML
    private TableColumn<PostManMailDetails,String> colReceiversAddress;

    @FXML
    private ImageView imgView;

    @FXML
    private Label dateAndTime;

    private volatile boolean stop=false;

    private ObservableList<PostManMailDetails> obList=null;

    public static boolean isNew=false;

    public static String postmanId=null;

    @FXML
    void onActionAddEmployee(ActionEvent event) throws IOException {
        isNew=true;
        URL resource = getClass().getResource("/view/addPostMan.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        root.getChildren().clear();
        root.getChildren().add(load);
    }

    @FXML
    void onActionUpdateEmployee(ActionEvent event) throws IOException {
        URL resource = getClass().getResource("/view/addPostMan.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        root.getChildren().clear();
        root.getChildren().add(load);
    }

    @FXML
    void onActionPostmanDetails(ActionEvent event) throws MalformedURLException {
        PostManEmployee postManEmployee=null;
        ArrayList<PostManMailDetails> mailDetails=null;
        try {
            postManEmployee=EmployeeModel.getPostmanDetails(idsComboBox.getValue());
            postQty.setText(EmployeeModel.getMailCount(idsComboBox.getValue())+"");
            mailDetails=EmployeeModel.getMailDetails(idsComboBox.getValue());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        }

        postManArea.setText(postManEmployee.getPost_area());
        postManName.setText(postManEmployee.getEmployee_name());

        if(postManEmployee.getImg()!=null) {
            try {
                Image image = new Image(postManEmployee.getImg());
                imgView.setImage(image);
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
        
        setTable(mailDetails);

        postmanId=idsComboBox.getValue();
    }

    private void setTable(ArrayList<PostManMailDetails> mailDetails) {

        postManTable.getItems().clear();

        setCellValueFactory();

        for (PostManMailDetails postManMailDetails:mailDetails){
            obList.add(postManMailDetails);
        }

        postManTable.setItems(obList);

    }

    private void setCellValueFactory() {
        colMailId.setCellValueFactory(new PropertyValueFactory<>("mailId"));
        colReceiversName.setCellValueFactory(new PropertyValueFactory<>("receiverName"));
        colReceiversAddress.setCellValueFactory(new PropertyValueFactory<>("receiversAddress"));

        obList = FXCollections.observableArrayList();
        postManTable.setItems(obList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Load postman Id
        loadPostmanIds();

        //Set Send Date
        setDate();

        setDateAndTime();
    }

    private void loadPostmanIds() {

        ArrayList<String> postManIds=null;
        try {
            postManIds=EmployeeModel.getAllIds();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        }

        ObservableList<String> postmanIdList = FXCollections.observableArrayList();

        for (String postmanId:postManIds){
            postmanIdList.add(postmanId);
        }

        idsComboBox.setItems(postmanIdList);
    }

    private void setDate() {
        todayDate.setText(String.valueOf(Launcher.date));
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

}

