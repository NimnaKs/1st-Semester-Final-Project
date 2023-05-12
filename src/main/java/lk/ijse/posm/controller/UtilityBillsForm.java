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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import lk.ijse.posm.Launcher;
import lk.ijse.posm.db.DbConnection;
import lk.ijse.posm.dto.Customer;
import lk.ijse.posm.dto.Payment;
import lk.ijse.posm.model.*;
import lk.ijse.posm.util.GenerateReports.GeneratePdfBill;
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

public class UtilityBillsForm implements Initializable, MainController {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private Label paymentId;

    @FXML
    private JFXButton searchBtn;

    @FXML
    private ComboBox<String> billType;

    @FXML
    private JFXTextField customerName;

    @FXML
    private JFXTextField customerTelephoneNo;

    @FXML
    private JFXTextField billOwnersName;

    @FXML
    private JFXTextField refferenceNo;

    @FXML
    private JFXTextField paymentAmount;

    @FXML
    private JFXButton RemoveBtn;

    @FXML
    private JFXComboBox<String> customerId;

    @FXML
    private JFXButton newId;

    @FXML
    private Label paymentDate;

    @FXML
    private JFXComboBox<String> companyName;

    @FXML
    private TextField EnterPaymentId;

    @FXML
    private Label dateAndTime;

    private volatile boolean stop=false;

    private ObservableList<String> customerIdList;

    private String customer_id="C000";

    private boolean isCustomerIdOk=false;

    private boolean isCustomerNameOk=false;

    private boolean isCustomerTpOk=false;

    private boolean isBillTypeOk=false;

    private boolean isCompanyNameOk=false;

    private boolean isBillOwnerNameOk=false;

    private boolean isReferenceNoOk=false;

    private boolean isPaymentOk=false;

    @FXML
    void newIdBtn(ActionEvent event) {
        if (customer_id.equals("C000")) {
            try {
                customer_id = generateCustomerId();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            customerIdList.add(customer_id);
            customerId.setItems(customerIdList);
            customerId.getSelectionModel().select(customer_id);

        }else{
            customerId.setPromptText(customer_id);
        }

        setCustomerNameEdit(true);
    }

    @FXML
    void onActionRemove(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do u really want to remove the Payment Details ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        String newPaymentId=null;
        try {
            newPaymentId=PaymentModel.generateNextPaymentId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (alert.getResult() == ButtonType.YES && (!paymentId.getText().equals(newPaymentId))) {
            boolean isRemove= false;
            try {
                isRemove = PaymentModel.remove(paymentId.getText());
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "SQL Error.Try Again Later !!").showAndWait();
            }
            if (isRemove){
                new Alert(Alert.AlertType.CONFIRMATION, "Removed Successfully !!").showAndWait();
            }else{
                new Alert(Alert.AlertType.WARNING, "Remove UnSuccessful !!").showAndWait();
            }
        }
        makeAllNull();
        setDate();
        EnterPaymentId.setText(null);
    }

    @FXML
    void onActionSave(ActionEvent event) {

        boolean isSaved = false;
        boolean isUpdated = false;

        GeneratePdfBill generatePdf = new GeneratePdfBill();

        generatePdf.setCustName(customerName.getText());
        generatePdf.setCustId(customerId.getValue());
        generatePdf.setCustTp(customerTelephoneNo.getText());

        generatePdf.setBill_type(billType.getValue());
        generatePdf.setBill_owners_name(billOwnersName.getText());
        generatePdf.setCompany_name(companyName.getValue());
        generatePdf.setPayemntId(paymentId.getText());
        generatePdf.setRefereneceNo(refferenceNo.getText());
        generatePdf.setPayment(paymentAmount.getText());

        if (saveBtn.getText().equals("Save")) {

            try {
                isSaved = PaymentDetailsModel.save(new Customer(customerId.getValue(),
                                customerName.getText(),
                                customerTelephoneNo.getText()),
                        new Payment(paymentId.getText(),
                                billOwnersName.getText(),
                                billType.getValue(),
                                companyName.getValue(),
                                refferenceNo.getText(),
                                paymentDate.getText(),
                                customerId.getValue(),
                                Launcher.userId,
                                Double.parseDouble(paymentAmount.getText())));

                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved Successfully !!").showAndWait();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Saved UnSuccessful !!").showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            }

        }else{

            try {
                isUpdated = PaymentDetailsModel.update(new Customer(customerId.getValue(),
                                customerName.getText(),
                                customerTelephoneNo.getText()),
                        new Payment(paymentId.getText(),
                                billOwnersName.getText(),
                                billType.getValue(),
                                companyName.getValue(),
                                refferenceNo.getText(),
                                paymentDate.getText(),
                                customerId.getValue(),
                                Launcher.userId,
                                Double.parseDouble(paymentAmount.getText())));

                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Update Successfully !!").showAndWait();
                    saveBtn.setText("Save");
                } else {
                    new Alert(Alert.AlertType.WARNING, "Update UnSuccessful !!").showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            }

        }

        if (isSaved || isUpdated) {

            generatePdf.generateBillReport();

            File pdfFile = new File("invoiceBill.pdf");

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
                URL resource = getClass().getResource("/view/emailGetBillsForm.fxml");
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
        EnterPaymentId.setText(null);
        setBooleanFalse(false);
        isButtonsEnable();

    }

    private void makeAllNull() {
        customerId.getSelectionModel().select(null);
        customerName.setText(null);
        customerName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        customerTelephoneNo.setText(null);
        customerTelephoneNo.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        setPaymentId();
        billOwnersName.setText(null);
        billOwnersName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        billType.getSelectionModel().select(null);
        companyName.getSelectionModel().select(null);
        refferenceNo.setText(null);
        refferenceNo.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        paymentAmount.setText(null);
        paymentAmount.setUnFocusColor(Paint.valueOf("#4d4d4d"));
    }

    @FXML
    void onActionSearch(ActionEvent event) {

        ArrayList<String> paymentIdList=null;
        try {
            paymentIdList= PaymentModel.getPaymentIds();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean isWrongIds=false;
        for(String id :paymentIdList){
            if (id.equals(EnterPaymentId.getText())){
                isWrongIds=true;
            }
        }
        if (!isWrongIds) {
            EnterPaymentId.setText("Wrong Order Id Or Null ");
            makeAllNull();
        }else {
            Payment payment = null;
            Customer customer = null;
            saveBtn.setText("Update");

            try {
                payment = PaymentModel.getData(EnterPaymentId.getText());
                customer = CustomerModel.searchById(payment.getCustomer_id());
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
                e.printStackTrace();
            }

            customerId.getSelectionModel().select(payment.getCustomer_id());
            customerName.setText(customer.getCustomer_name());
            customerTelephoneNo.setText(customer.getCustomer_contact());
            paymentId.setText(payment.getPayment_id());
            paymentDate.setText(payment.getPayment_Date());
            billOwnersName.setText(payment.getBill_owner_name());
            billType.getSelectionModel().select(payment.getBill_type());
            companyName.getSelectionModel().select(payment.getCompany_name());
            refferenceNo.setText(payment.getReferenceNo());
            paymentAmount.setText(payment.getPaymentAmount()+"");
        }

    }

    @FXML
    void onActionCustomer(ActionEvent event) {
        if (customerId.getValue()==null){
            isCustomerIdOk=false;
        }else{
            isCustomerIdOk=true;
        }
        isButtonsEnable();
        if(isCustomerIdOk) {
            try {
                String cus_id = customerId.getSelectionModel().getSelectedItem();

                String newCustomerId = null;

                try {
                    newCustomerId = generateCustomerId();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (cus_id.equals(newCustomerId)) {
                    setCustomerNameEdit(true);
                    customerName.setText(null);
                    customerTelephoneNo.setText(null);
                } else {
                    Customer customer = null;
                    try {
                        customer = CustomerModel.searchById(cus_id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
                    }
                    customerName.setText(customer.getCustomer_name());
                    setCustomerNameEdit(false);
                    if (customer.getCustomer_contact().equals("0000000000")) {
                        customerTelephoneNo.setText(null);
                    } else {
                        customerTelephoneNo.setText(customer.getCustomer_contact());
                    }
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

        //Load Bill Type
        loadBillType();

        //Load Company Names
        loadCompanyNames();

        //set PaymentId
        setPaymentId();

        saveBtn.setText("Save");

        setDateAndTime();

        isButtonsEnable();
    }

    private void setPaymentId() {
        try {
            paymentId.setText(PaymentModel.generateNextPaymentId());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        }
    }

    private void loadCompanyNames() {
        ObservableList<String> companyNameList=FXCollections.observableArrayList();
        companyNameList.addAll("Water Board", "CBL", "Lecho", "Dialog", "Mobitel", "Airtel", "Hutch");
        companyName.setItems(companyNameList);
    }

    private void loadBillType() {
        ObservableList<String> billTypeList=FXCollections.observableArrayList();
        billTypeList.addAll("Water","Electric","Telecommunication");
        billType.setItems(billTypeList);
    }

    private void setDate() {
        paymentDate.setText(String.valueOf(Launcher.date));
    }

    private void loadCustomerIds() {
        try {
            List<String> ids = CustomerModel.getIds();
            customerIdList= FXCollections.observableArrayList();

            for (String id : ids) {
                customerIdList.add(id);
            }
            customerId.setItems(customerIdList);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        }
    }

    private String generateCustomerId() throws SQLException {
        return CustomerModel.generateNextCustId();
    }

    private void setCustomerNameEdit(boolean isEditable) {
        customerName.setEditable(isEditable);
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

    private void isButtonsEnable() {
        if (isCustomerIdOk&&isBillOwnerNameOk&&isBillTypeOk&&isCompanyNameOk&&isCustomerNameOk&&isCustomerTpOk
        &&isReferenceNoOk&&isPaymentOk){
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
        isBillOwnerNameOk=isFalse;
        isBillTypeOk=isFalse;
        isCompanyNameOk=isFalse;
        isCustomerNameOk=isFalse;
        isCustomerTpOk=isFalse;
        isReferenceNoOk=isFalse;
        isPaymentOk=isFalse;
    }

    @Override
    public void setStopTrue(){
        stop=true;
        Thread thread=setDateAndTime();
        thread.interrupt();
    }

    @FXML
    void onActionCheckBillOwnersName(KeyEvent event) {
        Pattern compile1= RegExPatterns.getNamePattern();
        Matcher matcher=compile1.matcher(billOwnersName.getText());
        boolean matches1=matcher.matches();

        Pattern compile2=RegExPatterns.getTwoStringCheckPattern();
        matcher=compile2.matcher(billOwnersName.getText());
        boolean matches2=matcher.matches();

        if (matches1 && matches2){
            billOwnersName.setUnFocusColor(Paint.valueOf("blue"));
            isBillOwnerNameOk=true;
        }else{
            billOwnersName.setUnFocusColor(Paint.valueOf("red"));
            isBillOwnerNameOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    void onActionCheckBillType(ActionEvent event) {
        if (billType.getValue()==null){
            isBillTypeOk=false;
        }else{
            isBillTypeOk=true;
        }
        isButtonsEnable();
    }

    @FXML
    void onActionCheckCompanyName(ActionEvent event) {
        if (companyName.getValue()==null){
            isCompanyNameOk=false;
        }else{
            isCompanyNameOk=true;
        }
        isButtonsEnable();
    }

    @FXML
    void onActionCheckName(KeyEvent event) {
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
    void onActionCheckPayments(ActionEvent event) {
        Pattern compile= RegExPatterns.getDoublePattern();
        Matcher matcher=compile.matcher(paymentAmount.getText());
        boolean matches=matcher.matches();
        if (matches){
            paymentAmount.setUnFocusColor(Paint.valueOf("blue"));
            this.isPaymentOk=true;
        }else{
            paymentAmount.setUnFocusColor(Paint.valueOf("red"));
            this.isPaymentOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    void onActionCheckReferenceNumber(KeyEvent event) {
        Pattern compile= RegExPatterns.getReferenceTypePattern();
        Matcher matcher=compile.matcher(refferenceNo.getText());
        boolean matches=matcher.matches();
        if (matches){
            refferenceNo.setUnFocusColor(Paint.valueOf("blue"));
            this.isReferenceNoOk=true;
        }else{
            refferenceNo.setUnFocusColor(Paint.valueOf("red"));
            this.isReferenceNoOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    void onActionCheckTelephoneNumber(KeyEvent event) {
        Pattern compile= RegExPatterns.getMobilePattern();
        Matcher matcher=compile.matcher(customerTelephoneNo.getText());
        boolean matches=matcher.matches();
        if (matches){
            customerTelephoneNo.setUnFocusColor(Paint.valueOf("blue"));
            this.isCustomerTpOk=true;
        }else{
            customerTelephoneNo.setUnFocusColor(Paint.valueOf("red"));
            this.isCustomerTpOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    void onActionTodayPaymentDetails(ActionEvent event) throws FileNotFoundException, JRException, SQLException {
        if(PaymentModel.isTodayPaymentsOk()) {
            InputStream input = new FileInputStream(new File("src/main/resources/report/billPayment.jrxml"));
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DbConnection.getInstance().getConnection());

            JasperViewer.viewReport(jasperPrint, false);
        }else{
            new Alert(Alert.AlertType.WARNING,"Still No Bill Payments to Show.").showAndWait();
        }
    }


}
