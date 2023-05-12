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
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import lk.ijse.posm.Launcher;
import lk.ijse.posm.db.DbConnection;
import lk.ijse.posm.dto.CuriorMail;
import lk.ijse.posm.dto.Customer;
import lk.ijse.posm.model.*;
import lk.ijse.posm.util.GenerateReports.GeneratePdf;
import lk.ijse.posm.util.GenerateReports.GeneratePdfParcel;
import lk.ijse.posm.util.Interfaces.MainController;
import lk.ijse.posm.util.ValidationPattern.RegExPatterns;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CuriorForm implements Initializable, MainController {

    public Label mailId;

    public Label sendDate;

    public Label postManName;

    public Label deliveredDate;

    @FXML
    private AnchorPane root;

    @FXML
    private JFXComboBox<String> comboBoxMailType;

    @FXML
    private JFXTextField senderName;

    @FXML
    private JFXTextField senderAddress;

    @FXML
    private JFXTextField customerId;

    @FXML
    private JFXTextField customerName;

    @FXML
    private JFXTextField receiversName;

    @FXML
    private JFXTextField recieversTeleNo;

    @FXML
    private JFXTextField receiversAddress;

    @FXML
    private JFXTextField customerTeleNo;

    @FXML
    private JFXTextField weight;

    @FXML
    private JFXButton RemoveBtn;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXComboBox<String> customerIdComboBox;

    @FXML
    private Label price;

    @FXML
    private TextField mailEnteredTextBox;

    @FXML
    private Label dateAndTime;

    @FXML
    private JFXButton todayParcelDetails;

    private volatile boolean stop=false;

    private ObservableList<String> customerIdList;

    String customer_id="C000";

    private double totalPrice;

    private boolean isSendersNameCheckOk=false;

    private boolean isSendersAddressCheckOk=false;

    private boolean isCustomerNameCheckOK=false;

    private boolean isReceiversNameCheckOk=false;

    private boolean isReceiversAddressCheckOk=false;

    private boolean isReceiversTpCheckOk=false;

    private boolean isCustomerIdCheckOk=false;

    private boolean isCustomerTpOk=false;

    private boolean isWeightCheckOk=false;

    private boolean isMailTypeOk=false;

    @FXML
    void onActionSave(ActionEvent event) {
        String[] strings = receiversAddress.getText().split(",");
        String receiversAddresses=strings[strings.length-1];
        strings=weight.getText().split("Kg");
        double weightAmount=Double.parseDouble(strings[0]);

        boolean isUpdated = false;
        boolean isSaved = false;

        GeneratePdfParcel generatePdf = new GeneratePdfParcel();

        generatePdf.setCustName(customerName.getText());
        generatePdf.setCustId(customerIdComboBox.getValue());
        generatePdf.setCustTp(customerTeleNo.getText());

        generatePdf.setMailId(mailId.getText());
        generatePdf.setWeight(weightAmount);
        generatePdf.setPrice(totalPrice);

        generatePdf.setSenderName(senderName.getText());
        generatePdf.setSenderAddress(senderAddress.getText());
        generatePdf.setSendDate(sendDate.getText());

        generatePdf.setReceiverName(receiversName.getText());
        generatePdf.setReceiverAddress(receiversAddress.getText());
        generatePdf.setReceiverTp(recieversTeleNo.getText());

        if (saveBtn.getText().equals("Save")){
            try {
                isSaved = CuriorDetailsModel.save(new Customer(customerIdComboBox.getValue(),
                                customerName.getText(),
                                customerTeleNo.getText()),
                                new CuriorMail(mailId.getText(),
                                        comboBoxMailType.getValue(),
                                        sendDate.getText(),
                                        senderName.getText(),
                                        senderAddress.getText(),
                                        receiversName.getText(),
                                        recieversTeleNo.getText(),
                                        receiversAddress.getText(),
                                        weightAmount,
                                        totalPrice,
                                        CuriorModel.getPostMan((comboBoxMailType.getValue().equals("International"))?"Air port":receiversAddresses).getPostman_id()
                                        )
                        );

                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved Successfully !!").showAndWait();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Saved UnSuccessful !!").showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            }
        }else {
            try {
                isUpdated = CuriorDetailsModel.update(new Customer(customerIdComboBox.getValue(),
                                customerName.getText(),
                                customerTeleNo.getText()),
                        new CuriorMail(mailId.getText(),
                                comboBoxMailType.getValue(),
                                sendDate.getText(),
                                senderName.getText(),
                                senderAddress.getText(),
                                receiversName.getText(),
                                recieversTeleNo.getText(),
                                receiversAddress.getText(),
                                weightAmount,
                                totalPrice,
                                CuriorModel.getPostMan((comboBoxMailType.getValue().equals("International"))?"Air port":receiversAddresses).getPostman_id()
                        )
                );

                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Updated Successfully !!").showAndWait();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Updated UnSuccessful !!").showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            }
        }

        if (isSaved || isUpdated){

            generatePdf.generateCuriorReport();

            File pdfFile = new File("invoiceCurior.pdf");

            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(pdfFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Desktop is not supported");
                }
            } else {
                System.out.println("File not found");
            }

            try {
                URL resource = getClass().getResource("/view/emailGetCuriorForm.fxml");
                assert resource != null;
                Parent load = FXMLLoader.load(resource);
                root.getChildren().clear();
                root.getChildren().add(load);
//                FXMLLoader fxmlLoader = new FXMLLoader();
//                fxmlLoader.setLocation(getClass().getResource("/view/emailGetForm.fxml"));
//                AnchorPane anchorPane = fxmlLoader.load();
//                EmailGetFormController controller=fxmlLoader.getController();
//                controller.setOrderReport(generatePdf);
            }catch (IOException exception){
                exception.printStackTrace();
            }

        }

        makeAllNull();
        saveBtn.setText("Save");
        setBooleanFalse(false);
        isButtonsEnable();
    }

    private void makeAllNull() {
        generateMailId();
        comboBoxMailType.getSelectionModel().select(null);
        setDate();
        senderName.setText(null);
        senderName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        senderAddress.setText(null);
        senderAddress.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        receiversName.setText(null);
        receiversName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        receiversAddress.setText(null);
        receiversAddress.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        recieversTeleNo.setText(null);
        recieversTeleNo.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        customerIdComboBox.getSelectionModel().select(null);
        customerName.setText(null);
        customerName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        customerTeleNo.setText(null);
        customerTeleNo.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        weight.setText(null);
        weight.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        price.setText(null);
        postManName.setText(null);
    }

    @FXML
    public void onActionCuriorDetails(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/curiorDetailsForm.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        root.getChildren().clear();
        root.getChildren().add(load);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        generateMailId();

        setComboBox();

        setDate();

        loadCustomerIds();

        labelEdit(false);

        saveBtn.setText("Save");

        setDateAndTime();

        setBooleanFalse(false);

        isButtonsEnable();
    }

    private void generateMailId() {
        try {
            mailId.setText(CuriorModel.generateNextMailId());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
        }
    }

    private void setComboBox() {

        comboBoxMailType.setValue(null);

        ObservableList<String> mailType = FXCollections.observableArrayList();

        mailType.addAll("International","Local");

        comboBoxMailType.setItems(mailType);

    }

    private void setDate() {
        sendDate.setText(String.valueOf(Launcher.date));
    }

    private void loadCustomerIds() {
        try {
            List<String> ids = CustomerModel.getIds();
            customerIdList= FXCollections.observableArrayList();

            for (String id : ids) {
                customerIdList.add(id);
            }
            customerIdComboBox.setItems(customerIdList);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
        }
    }

    private void labelEdit(boolean isEligible) {
        customerName.setEditable(isEligible);
        customerTeleNo.setEditable(isEligible);
    }

    @FXML
    void onActionGetPostManName(ActionEvent event) {
        String[] strings = receiversAddress.getText().split(",");
        String receiversAddresses=strings[strings.length-1];
        try {
            postManName.setText(CuriorModel.getPostMan(comboBoxMailType.getValue().equals("International")?"Air port":receiversAddresses).getEmployee_name());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
        }
    }

    @FXML
    void onActionGetCustomerDetails(ActionEvent event) {
        if (customerIdComboBox.getValue()==null){
            isCustomerIdCheckOk=false;
        }else{
            isCustomerIdCheckOk=true;
        }
        isButtonsEnable();

        if (isCustomerIdCheckOk) {
            try {
                String cus_id = customerIdComboBox.getSelectionModel().getSelectedItem();

                String newCustomerId = null;

                try {
                    newCustomerId = generateCustomerId();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (cus_id.equals(newCustomerId)) {
                    labelEdit(true);
                    customerName.setText(null);
                    customerTeleNo.setText(null);
                } else {
                    Customer customer = null;
                    try {
                        customer = CustomerModel.searchById(cus_id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
                    }
                    customerName.setText(customer.getCustomer_name());
                    customerTeleNo.setText(customer.getCustomer_contact());
                    labelEdit(false);
                    if (customer.getCustomer_contact().equals("0000000000")) {
                        customerTeleNo.setEditable(true);
                    }
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }

    @FXML
    void onActionGetPrice(ActionEvent event) {

        double weightAmount=0.0;

        try{
            String[] strings=weight.getText().split(" Kg");
            weightAmount=Double.parseDouble(strings[0]);
        }catch (NullPointerException exception){
            exception.printStackTrace();
        }

        Pattern compile= RegExPatterns.getDoublePattern();
        Matcher matcher=compile.matcher(weightAmount+"");
        boolean matches=matcher.matches();
        if (matches){
            weight.setUnFocusColor(Paint.valueOf("blue"));
            this.isWeightCheckOk=true;
        }else{
            weight.setUnFocusColor(Paint.valueOf("red"));
            this.isWeightCheckOk=false;
        }

        isButtonsEnable();
        if (isWeightCheckOk&&isMailTypeOk) {
            try {
                totalPrice = CuriorModel.calculatePrice(weightAmount, comboBoxMailType.getValue());
                price.setText("Rs." + totalPrice);
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
            }
        }
    }

    @FXML
    void newIdBtn(ActionEvent event) {

        if (customer_id.equals("C000")) {
            try {
                customer_id = generateCustomerId();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            customerIdList.add(customer_id);
            customerIdComboBox.setItems(customerIdList);
            customerIdComboBox.setPromptText(customer_id);

        }else{
            customerIdComboBox.getSelectionModel().select(customer_id);
        }

        labelEdit(true);
    }

    private String generateCustomerId() throws SQLException {
        return CustomerModel.generateNextCustId();
    }

    @FXML
    void onActionSearch(ActionEvent event) {

        CuriorMail curiorMail=null;
        Customer customer=null;

        ArrayList<String> mailIds=null;
        try {
            mailIds= CuriorModel.getMailIds();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean isWrongIds=false;
        for(String id :mailIds){
            if (id.equals(mailEnteredTextBox.getText())){
                isWrongIds=true;
            }
        }
        if (!isWrongIds){
            mailEnteredTextBox.setText("Wrong Order Id Or Null ");
            makeAllNull();
        } else {

            try {
                curiorMail = CuriorDetailsModel.getAllCuriorData(mailEnteredTextBox.getText());
                customer = CuriorDetailsModel.getCustomerDetails(curiorMail.getCustomerId());
            } catch (SQLException exception) {
                new Alert(Alert.AlertType.ERROR, "SQL ERROR. Please Try Again Later !").showAndWait();
            }

            mailId.setText(curiorMail.getMail_id());
            comboBoxMailType.getSelectionModel().select(curiorMail.getMail_type());
            sendDate.setText(curiorMail.getSend_date());
            senderName.setText(curiorMail.getSend_Name());
            senderAddress.setText(curiorMail.getSend_Address());
            receiversName.setText(curiorMail.getReceiver_Name());
            receiversAddress.setText(curiorMail.getReceiver_Address());
            recieversTeleNo.setText(curiorMail.getReceiver_Telephone_No());
            customerIdComboBox.getSelectionModel().select(customer.getCustomer_id());
            customerName.setText(customer.getCustomer_name());
            customerTeleNo.setText(customer.getCustomer_contact());
            weight.setText(curiorMail.getWeight() + " Kg");
            price.setText("Rs."+curiorMail.getPrice() + "");
            postManName.setText(curiorMail.getDelivery_postman_id());

            saveBtn.setText("Update");
        }
    }

    @FXML
    void onActionRemove(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do u really want to remove the Parcel ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES){
            try {
                boolean isRemove = CuriorDetailsModel.remove(mailId.getText());
                saveBtn.setText("Save");
                if (isRemove) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Removed Successfully !!").showAndWait();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Removed UnSuccessful !!").showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "SQL ERROR. Please Try Again Later !").showAndWait();
            }

            makeAllNull();
            mailEnteredTextBox.setText(null);
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
    public void onActionSendersNameCheck(KeyEvent keyEvent) {
        Pattern compile1= RegExPatterns.getNamePattern();
        Matcher matcher=compile1.matcher(senderName.getText());
        boolean matches1=matcher.matches();

        Pattern compile2=RegExPatterns.getTwoStringCheckPattern();
        matcher=compile2.matcher(senderName.getText());
        boolean matches2=matcher.matches();

        if (matches1 && matches2){
            senderName.setUnFocusColor(Paint.valueOf("blue"));
            isSendersNameCheckOk=true;
        }else{
            senderName.setUnFocusColor(Paint.valueOf("red"));
            isSendersNameCheckOk=false;
        }
        isButtonsEnable();
    }

    @FXML
    public void onActionSendersAddressCheck(KeyEvent keyEvent) {
        Pattern compile= RegExPatterns.getAddressPattern();
        Matcher matcher=compile.matcher(senderAddress.getText());
        boolean matches=matcher.matches();

        if (matches){
            senderAddress.setUnFocusColor(Paint.valueOf("blue"));
            isSendersAddressCheckOk=true;
        }else{
            senderAddress.setUnFocusColor(Paint.valueOf("red"));
            isSendersAddressCheckOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    public void onActionCustomerNameCheck(KeyEvent keyEvent) {
        Pattern compile1= RegExPatterns.getNamePattern();
        Matcher matcher=compile1.matcher(customerName.getText());
        boolean matches1=matcher.matches();

        Pattern compile2=RegExPatterns.getTwoStringCheckPattern();
        matcher=compile2.matcher(customerName.getText());
        boolean matches2=matcher.matches();

        if (matches1 && matches2){
            customerName.setUnFocusColor(Paint.valueOf("blue"));
            isCustomerNameCheckOK=true;
        }else{
            customerName.setUnFocusColor(Paint.valueOf("red"));
            isCustomerNameCheckOK=false;
        }
        isButtonsEnable();
    }

    @FXML
    public void onActionReceiversNameCheck(KeyEvent keyEvent) {
        Pattern compile1= RegExPatterns.getNamePattern();
        Matcher matcher=compile1.matcher(receiversName.getText());
        boolean matches1=matcher.matches();

        Pattern compile2=RegExPatterns.getTwoStringCheckPattern();
        matcher=compile2.matcher(receiversName.getText());
        boolean matches2=matcher.matches();

        if (matches1 && matches2){
            receiversName.setUnFocusColor(Paint.valueOf("blue"));
            isReceiversNameCheckOk=true;
        }else{
            receiversName.setUnFocusColor(Paint.valueOf("red"));
            isReceiversNameCheckOk =false;
        }
        isButtonsEnable();
    }

    @FXML
    public void onActionReceiversTpCheck(KeyEvent keyEvent) {
        Pattern compile= RegExPatterns.getMobilePattern();
        Matcher matcher=compile.matcher(recieversTeleNo.getText());
        boolean matches=matcher.matches();
        if (matches){
            recieversTeleNo.setUnFocusColor(Paint.valueOf("blue"));
            this.isReceiversTpCheckOk=true;
        }else{
            recieversTeleNo.setUnFocusColor(Paint.valueOf("red"));
            this.isReceiversTpCheckOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    public void onActionReceiversAddressCheck(KeyEvent keyEvent) {
        Pattern compile= RegExPatterns.getAddressPattern();
        Matcher matcher=compile.matcher(receiversAddress.getText());
        boolean matches=matcher.matches();

        if (matches){
            receiversAddress.setUnFocusColor(Paint.valueOf("blue"));
            isReceiversAddressCheckOk=true;
        }else{
            receiversAddress.setUnFocusColor(Paint.valueOf("red"));
            isReceiversAddressCheckOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    public void onActionCustomerTpCheck(KeyEvent keyEvent) {
        Pattern compile= RegExPatterns.getMobilePattern();
        Matcher matcher=compile.matcher(customerTeleNo.getText());
        boolean matches=matcher.matches();
        if (matches){
            customerTeleNo.setUnFocusColor(Paint.valueOf("blue"));
            this.isCustomerTpOk=true;
        }else{
            customerTeleNo.setUnFocusColor(Paint.valueOf("red"));
            this.isCustomerTpOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    public void onActionWeightCheck(KeyEvent keyEvent) {

    }

    private void isButtonsEnable() {
        if (isCustomerIdCheckOk&&isCustomerNameCheckOK&&isCustomerTpOk&&isSendersAddressCheckOk&&
                isSendersNameCheckOk&&isReceiversAddressCheckOk&&isReceiversNameCheckOk&&isReceiversTpCheckOk&&isMailTypeOk&&isWeightCheckOk){
            setButtonDisable(false);
        }else{
            setButtonDisable(true);
        }
    }

    private void setButtonDisable(boolean isDisable) {
        saveBtn.setDisable(isDisable);
    }

    private void setBooleanFalse(boolean isFalse){
        isCustomerIdCheckOk=isFalse;
        isCustomerNameCheckOK=isFalse;
        isCustomerTpOk=isFalse;
        isSendersNameCheckOk=isFalse;
        isSendersAddressCheckOk=isFalse;
        isReceiversTpCheckOk=isFalse;
        isReceiversAddressCheckOk=isFalse;
        isReceiversNameCheckOk=isFalse;
        isMailTypeOk=isFalse;
        isWeightCheckOk=isFalse;
    }

    public void onActionMailType(ActionEvent event) {
        if (comboBoxMailType.getValue()==null){
            isMailTypeOk=false;
        }else{
            isMailTypeOk=true;
        }
        isButtonsEnable();
    }

    @FXML
    void onActionTodayParcelDetails(ActionEvent event) throws SQLException, FileNotFoundException, JRException {
        if(CuriorModel.isTodayMailsOk()) {

            InputStream input = new FileInputStream(new File("src/main/resources/report/curiorReport.jrxml"));
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DbConnection.getInstance().getConnection());

            JasperViewer.viewReport(jasperPrint, false);


        }else{
            new Alert(Alert.AlertType.WARNING,"Still No Parcel Details to Show.").showAndWait();
        }
    }

}
