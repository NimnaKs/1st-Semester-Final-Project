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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import lk.ijse.posm.Launcher;
import lk.ijse.posm.db.DbConnection;
import lk.ijse.posm.dto.Change;
import lk.ijse.posm.dto.Customer;
import lk.ijse.posm.dto.Tm.CurrencyTM;
import lk.ijse.posm.model.*;
import lk.ijse.posm.util.GenerateReports.GeneratePdf;
import lk.ijse.posm.util.GenerateReports.GeneratePdfMoneyTransfer;
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

public class MoneyTransferForm implements Initializable, MainController {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private TextField enteredPaymentId;

    @FXML
    private JFXButton search;

    @FXML
    private Label paymentId;

    @FXML
    private JFXTextField customerName;

    @FXML
    private JFXTextField customerTeleNo;

    @FXML
    private JFXTextField sellingMoneyAmount;

    @FXML
    private JFXButton RemoveBtn;

    @FXML
    private JFXComboBox<String> customerIdComboBox;

    @FXML
    private JFXButton newId;

    @FXML
    private Label paymentDate;

    @FXML
    private JFXComboBox<String> sellingMoneyTypeComboBox;

    @FXML
    private JFXComboBox<String> receivingMoneyComboBox;

    @FXML
    private Label receivingMoneyAmount;

    @FXML
    private Label dateAndTime;

    private volatile boolean stop=false;

    private String customer_id="C000";

    private ObservableList<String> customerIdList=null;

    private ObservableList<String> sellingMoneyType=FXCollections.observableArrayList();

    private ObservableList<String> receivingMoneyType=FXCollections.observableArrayList();

    private boolean isCustomerIdOk=false;

    private boolean isCustomerNameOk=false;

    private boolean isCustomerTpOk=false;

    private boolean isSellingMoneyTypeOk=false;

    private boolean isSellingMoneyAmountOk=false;

    private boolean isReceivingMoneyTypeOk=false;

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

    @FXML
    void onActionRemove(ActionEvent event) {
        boolean isRemove=false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do u really want to remove the Payment Details ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        String newPaymentId=null;
        try {
            newPaymentId=ChangeModel.generateNextPaymentId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (alert.getResult() == ButtonType.YES && (!paymentId.getText().equals(newPaymentId))) {
                try {
                    isRemove = ChangeModel.remove(new Change(paymentId.getText(), paymentDate.getText(), sellingMoneyTypeComboBox.getValue(),
                            Double.parseDouble(sellingMoneyAmount.getText()), receivingMoneyComboBox.getValue(), Double.parseDouble(receivingMoneyAmount.getText())));
                    if (isRemove) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Details Removed Successfully !!").showAndWait();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Details Removed UnSuccessful !!").showAndWait();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
                }
            }
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        saveBtn.setText("Save");
        makeAllNull();
        setBooleanFalse(false);
        isButtonsEnable();
    }

    private String generateCustomerId() throws SQLException {
        return CustomerModel.generateNextCustId();
    }

    private void labelEdit(boolean isEligible) {
        customerName.setEditable(isEligible);
        customerTeleNo.setEditable(isEligible);
    }

    @FXML
    void onActionSave(ActionEvent event) throws JRException, SQLException, FileNotFoundException {

        boolean isUpdated = false;
        boolean isSaved = false;

        GeneratePdfMoneyTransfer generatePdf = new GeneratePdfMoneyTransfer();

        generatePdf.setCustId(customerIdComboBox.getValue());
        generatePdf.setCustName(customerName.getText());
        generatePdf.setCustTp(customerTeleNo.getText());

        generatePdf.setPayemntId(paymentId.getText());
        generatePdf.setReceivingMoneyAmount(Double.parseDouble(receivingMoneyAmount.getText()));
        generatePdf.setSellingMoneyAmount(Double.parseDouble(sellingMoneyAmount.getText()));
        generatePdf.setReceivingMoneyType(receivingMoneyComboBox.getValue());
        generatePdf.setSellingMoneyType(sellingMoneyTypeComboBox.getValue());


        if (saveBtn.getText().equals("Save")) {
            try {
                isSaved = ChangeModel.save(new Customer(customerIdComboBox.getValue(),
                                customerName.getText(),
                                customerTeleNo.getText()),
                        new Change(paymentId.getText(), paymentDate.getText(), sellingMoneyTypeComboBox.getValue(),
                                Double.parseDouble(sellingMoneyAmount.getText()), receivingMoneyComboBox.getValue(), Double.parseDouble(receivingMoneyAmount.getText())
                        )
                );

                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Details Saved Successfully !!").showAndWait();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Details Saved UnSuccessful !!").showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            }

        }else {
            try {
                isUpdated = ChangeModel.update(new Customer(customerIdComboBox.getValue(),
                                customerName.getText(),
                                customerTeleNo.getText()),
                        new Change(paymentId.getText(), paymentDate.getText(), sellingMoneyTypeComboBox.getValue(),
                                Double.parseDouble(sellingMoneyAmount.getText()), receivingMoneyComboBox.getValue(), Double.parseDouble(receivingMoneyAmount.getText())));

                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Details Updated Successfully !!").showAndWait();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Details Updated UnSuccessful !!").showAndWait();
                }
            }catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            }
            saveBtn.setText("Save");
        }

        if (isSaved || isUpdated) {

            generatePdf.generateMoneyTrasferReport();

            File pdfFile = new File("invoiceMoneyTrasfer.pdf");

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
                URL resource = getClass().getResource("/view/emailGetMoneyTrasferForm.fxml");
                assert resource != null;
                Parent load = FXMLLoader.load(resource);
                root.getChildren().clear();
                root.getChildren().add(load);
//                FXMLLoader fxmlLoader = new FXMLLoader();
//                fxmlLoader.setLocation(getClass().getResource("/view/emailGetForm.fxml"));
//                AnchorPane anchorPane = fxmlLoader.load();
//                EmailGetFormController controller=fxmlLoader.getController();
//                controller.setOrderReport(generatePdf);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        makeAllNull();
        setBooleanFalse(false);
        isButtonsEnable();
    }

    private void makeAllNull() {
        customerIdComboBox.getSelectionModel().select(null);
        customerName.setText(null);
        customerName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        customerTeleNo.setText(null);
        customerTeleNo.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        setPaymentId();
        setDate();
        sellingMoneyTypeComboBox.getSelectionModel().select(null);
        sellingMoneyAmount.setText(null);
        sellingMoneyAmount.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        receivingMoneyComboBox.getSelectionModel().select(null);
        receivingMoneyAmount.setText(null);
    }

    @FXML
    void onActionSearch(ActionEvent event) {
        ArrayList<String> paymentIdList=null;
        try {
            paymentIdList= ChangeModel.getPaymentIds();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean isWrongIds=false;
        for(String id :paymentIdList){
            if (id.equals(enteredPaymentId.getText())){
                isWrongIds=true;
            }
        }
        if (!isWrongIds) {
            enteredPaymentId.setText("Wrong Order Id Or Null ");
            makeAllNull();
        }else {
            String paymentIds = enteredPaymentId.getText();
            Change change = null;
            Customer customer = null;

            try {
                change = ChangeModel.getMoneyTransferDetails(paymentIds);
                customer = CustomerModel.searchById(change.getCustomerId());
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            }

            customerIdComboBox.getSelectionModel().select(change.getCustomerId());
            customerName.setText(customer.getCustomer_name());
            customerTeleNo.setText(customer.getCustomer_contact());
            paymentId.setText(paymentIds);
            paymentDate.setText(change.getPaymentDate());
            sellingMoneyTypeComboBox.getSelectionModel().select(change.getSellingMoneyType());
            sellingMoneyAmount.setText(change.getSellingMoneyAmount() + "");
            receivingMoneyComboBox.getSelectionModel().select(change.getReceivingMoneyType());
            receivingMoneyAmount.setText(change.getReceivingMoneyAmount() + "");

            saveBtn.setText("Update");
        }

    }

    @FXML
    void onActionSelectCustomerId(ActionEvent event) {
        if (customerIdComboBox.getValue()==null){
            isCustomerIdOk=false;
        }else{
            isCustomerIdOk=true;
        }
        isButtonsEnable();
        if (isCustomerIdOk) {
            String cus_id = null;
            try {
                cus_id = customerIdComboBox.getSelectionModel().getSelectedItem();

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
    void onActionSellingMoneyType(ActionEvent event) {
        if (sellingMoneyTypeComboBox.getValue()==null){
            isSellingMoneyTypeOk=false;
            isReceivingMoneyTypeOk=false;
        }else{
            isSellingMoneyTypeOk=true;
            isReceivingMoneyTypeOk=true;
        }
        isButtonsEnable();
        if (isSellingMoneyTypeOk&&isReceivingMoneyTypeOk) {
            try {
                if (sellingMoneyTypeComboBox.getValue().equals("Sri Lankan LKR")) {
                    receivingMoneyType.remove("Sri Lankan LKR");
                    receivingMoneyComboBox.setItems(receivingMoneyType);
                } else {
                    receivingMoneyComboBox.getSelectionModel().select("Sri Lankan LKR");
                    receivingMoneyComboBox.setEditable(false);
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }

    @FXML
    void onActionReceivingMoneyType(ActionEvent event) {
        if (receivingMoneyComboBox.getValue()==null){
            isSellingMoneyTypeOk=false;
            isReceivingMoneyTypeOk=false;
        }else{
            isSellingMoneyTypeOk=true;
            isReceivingMoneyTypeOk=true;
        }
        isButtonsEnable();
        if (isReceivingMoneyTypeOk&&isReceivingMoneyTypeOk) {
            try {
                if (receivingMoneyComboBox.getValue().equals("Sri Lankan LKR")) {
                    sellingMoneyType.remove("Sri Lankan LKR");
                    sellingMoneyTypeComboBox.setItems(sellingMoneyType);
                } else {
                    sellingMoneyTypeComboBox.getSelectionModel().select("Sri Lankan LKR");
                    sellingMoneyTypeComboBox.setEditable(false);
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Set Send Date
        setDate();

        //Load Customer Ids
        loadCustomerIds();

        //set PaymentId
        setPaymentId();

        //Load selling Money Type
        loadMoneyType();

        saveBtn.setText("Save");

        setDateAndTime();

        isButtonsEnable();
    }

    private void loadMoneyType() {
        ArrayList<CurrencyTM> currencyList=null;
        try {
            currencyList=CurrencyModel.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (CurrencyTM moneyType : currencyList){
            sellingMoneyType.add(moneyType.getMoneyType());
            receivingMoneyType.add(moneyType.getMoneyType());
        }

        sellingMoneyTypeComboBox.setItems(sellingMoneyType);

        receivingMoneyComboBox.setItems(receivingMoneyType);

    }

    private void setDate() {
        paymentDate.setText(String.valueOf(Launcher.date));
    }

    private void loadCustomerIds() {
        try {
            List<String> ids = CustomerModel.getIds();
            customerIdList = FXCollections.observableArrayList();

            for (String id : ids) {
                customerIdList.add(id);
            }
            customerIdComboBox.setItems(customerIdList);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error.Please Try Again Later!!").showAndWait();
        }
    }

    private void setPaymentId() {
        try {
            paymentId.setText(ChangeModel.generateNextPaymentId());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        }
    }

    @FXML
    void onActionSetReceivingMoneyAmount(ActionEvent event) {
        Pattern compile= RegExPatterns.getDoublePattern();
        Matcher matcher=compile.matcher(sellingMoneyAmount.getText());
        boolean matches=matcher.matches();
        if (matches){
            sellingMoneyAmount.setUnFocusColor(Paint.valueOf("blue"));
            this.isSellingMoneyAmountOk=true;
        }else{
            sellingMoneyAmount.setUnFocusColor(Paint.valueOf("red"));
            this.isSellingMoneyAmountOk=false;
        }

        isButtonsEnable();

        if (matches) {
            try {
                if (Double.parseDouble(sellingMoneyAmount.getText()) > (ChangeModel.getSellingMoneyAmount(sellingMoneyTypeComboBox.getValue()))) {
                    new Alert(Alert.AlertType.WARNING, "The Requested Amount is insufficient.Please type sufficient amount.").showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "The SQL Error.Please Try Again Later !").showAndWait();
            }

            if (!sellingMoneyTypeComboBox.getValue().equals("Sri Lankan LKR")) {
                try {
                    receivingMoneyAmount.setText(ChangeModel.getReceivingMoneyAmount(sellingMoneyTypeComboBox.getValue(), Double.parseDouble(sellingMoneyAmount.getText())) + "");
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "The SQL Error.Please Try Again Later !").showAndWait();
                }
            } else {
                try {
                    receivingMoneyAmount.setText(ChangeModel.getReceivingMoneyAmountFromAnotherCurrencyType(receivingMoneyComboBox.getValue(), Double.parseDouble(sellingMoneyAmount.getText())) + "");
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "The SQL Error.Please Try Again Later !").showAndWait();
                }

            }
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
    void onActionCustomerNameCheck(KeyEvent event) {
        Pattern compile1= RegExPatterns.getNamePattern();
        Matcher matcher=compile1.matcher(customerName.getText());
        boolean matches1=matcher.matches();

        Pattern compile2=RegExPatterns.getTwoStringCheckPattern();
        matcher=compile2.matcher(customerName.getText());
        boolean matches2=matcher.matches();

        if (matches1 && matches2){
            customerName.setUnFocusColor(Paint.valueOf("blue"));
            isCustomerNameOk=true;
        }else{
            customerName.setUnFocusColor(Paint.valueOf("red"));
            isCustomerNameOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    void onActionCustomerTpCheck(KeyEvent event) {
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

    private void isButtonsEnable() {
        if (isCustomerIdOk&&isSellingMoneyTypeOk&&isSellingMoneyAmountOk&&isReceivingMoneyTypeOk&&isCustomerNameOk&&isCustomerTpOk){
            setButtonDisable(false);
        }else{
            setButtonDisable(true);
        }
    }

    private void setButtonDisable(boolean isDisable) {
        saveBtn.setDisable(isDisable);
    }

    private void setBooleanFalse(boolean isFalse){
        isCustomerIdOk=isFalse;
        isReceivingMoneyTypeOk=isFalse;
        isSellingMoneyAmountOk=isFalse;
        isSellingMoneyTypeOk=isFalse;
        isCustomerNameOk=isFalse;
        isCustomerTpOk=isFalse;
    }

    @FXML
    void onActionTodayMoneyTrasferDetails(ActionEvent event) throws FileNotFoundException, JRException, SQLException {
        if(ChangeModel.isTodayChangesOk()) {
            InputStream input = new FileInputStream(new File("src/main/resources/report/moneyTransfer.jrxml"));
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DbConnection.getInstance().getConnection());

            JasperViewer.viewReport(jasperPrint, false);
        }else{
            new Alert(Alert.AlertType.WARNING,"Still No Money Trasfers to Show.").showAndWait();
        }
    }
}
