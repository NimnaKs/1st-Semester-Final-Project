package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.FontWeight;
import lk.ijse.posm.Launcher;
import lk.ijse.posm.dto.NewUser;
import lk.ijse.posm.model.UserModel;
import lk.ijse.posm.util.Interfaces.MainController;
import lk.ijse.posm.util.ValidationPattern.RegExPatterns;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsForm implements Initializable, MainController {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton updateDetails;

    @FXML
    private Label jobRoll;

    @FXML
    private JFXButton changePassword;

    @FXML
    private JFXTextField employeeName;

    @FXML
    private JFXTextField emailAddress;

    @FXML
    private JFXTextField contactNo;

    @FXML
    private JFXTextField userName;

    @FXML
    private JFXTextField currentPw;

    @FXML
    private JFXTextField newPW;

    @FXML
    private JFXButton addUser;

    @FXML
    private Label userId;

    @FXML
    private Label dateAndTime;

    @FXML
    private JFXPasswordField currentPWPF;

    @FXML
    private JFXPasswordField newPasswordPWPF;

    @FXML
    private Label lblEyeClose;

    @FXML
    private Label lblEyeOpen;

    @FXML
    private Label lblEyeClose1;

    @FXML
    private Label lblEyeOpen1;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userPasswordError;

    private String currentPassword=null;

    private String newPassword=null;

    private volatile boolean stop=false;

    private boolean isManager=false;

    private boolean isUserNameOk=false;

    private boolean isEmployeeNameOk=false;

    private boolean isEmailOk=false;

    private boolean isTpOk=false;

    private boolean isCPwOk=false;

    private boolean isNPWOk=false;

    private String currentUserName=null;
    @FXML
    void close_eye_clickedCurrentPassword(MouseEvent event) {
        lblEyeClose.setVisible(false);
        lblEyeOpen.setVisible(true);
        currentPw.setVisible(true);
        currentPWPF.setVisible(false);
    }

    @FXML
    void close_eye_clickedNewPassword(MouseEvent event) {
        lblEyeClose1.setVisible(false);
        lblEyeOpen1.setVisible(true);
        newPW.setVisible(true);
        newPasswordPWPF.setVisible(false);
    }

    @FXML
    void onActionCheckEmail(KeyEvent keyEvent) {
        Pattern compile= RegExPatterns.getEmailPattern();
        Matcher matcher=compile.matcher(emailAddress.getText());
        boolean matches=matcher.matches();
        if (matches){
            emailAddress.setUnFocusColor(Paint.valueOf("blue"));
            this.isEmailOk=true;
        }else{
            emailAddress.setUnFocusColor(Paint.valueOf("red"));
            this.isEmailOk=false;
        }
        isButtonsEnable();
    }

    private void isButtonsEnable() {
        if (isUserNameOk&&isEmployeeNameOk&&isEmailOk&&isTpOk){
            setButtonDisable(false);
        }else{
            setButtonDisable(true);
        }
    }

    private void setButtonDisable(boolean isDisable) {
        updateDetails.setDisable(isDisable);
    }

    private void setBooleanFalse(boolean isFalse){
        isUserNameOk=isFalse;
        isEmployeeNameOk=isFalse;
        isEmailOk=isFalse;
        isTpOk=isFalse;
    }

    @FXML
    void onActionCheckTp(KeyEvent keyEvent) {
        Pattern compile= RegExPatterns.getMobilePattern();
        Matcher matcher=compile.matcher(contactNo.getText());
        boolean matches=matcher.matches();
        if (matches){
            contactNo.setUnFocusColor(Paint.valueOf("blue"));
            this.isTpOk=true;
        }else{
            contactNo.setUnFocusColor(Paint.valueOf("red"));
            this.isTpOk=false;
        }
        isButtonsEnable();
    }

    @FXML
    void onActionCheckUserName(KeyEvent keyEvent) {
        Pattern compile= RegExPatterns.getUserNamePattern();
        Matcher matcher=compile.matcher(userName.getText());
        boolean matches=matcher.matches();
        if (matches&&checkUserNameMatch(userName.getText())){
            userName.setUnFocusColor(Paint.valueOf("blue"));
            this.isUserNameOk=true;
            userNameLabel.setVisible(false);
        }else{
            userName.setUnFocusColor(Paint.valueOf("red"));
            this.isUserNameOk=false;
            userNameLabel.setVisible(true);
        }
        isButtonsEnable();
    }

    @FXML
    void open_eye_clickedCurrentPassword(MouseEvent event) {
        lblEyeClose.setVisible(true);
        lblEyeOpen.setVisible(false);
        currentPw.setVisible(false);
        currentPWPF.setVisible(true);
    }

    @FXML
    void open_eye_clickedNewPassword(MouseEvent event) {
        lblEyeClose1.setVisible(true);
        lblEyeOpen1.setVisible(false);
        newPW.setVisible(false);
        newPasswordPWPF.setVisible(true);
    }

    @FXML
    void onActionAddUser(ActionEvent actionEvent) throws IOException {

        if (isManager) {
            URL resource = getClass().getResource("/view/addUser.fxml");
            assert resource != null;
            Parent load = FXMLLoader.load(resource);
            root.getChildren().clear();
            root.getChildren().add(load);
        }else {
            new Alert(Alert.AlertType.WARNING,"You Are Not A Manager.Only Manager Can Add A New User.").showAndWait();
        }

    }

    @FXML
    void onActionChangePassword(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do U Really Wnt To Change The Password ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try {
                boolean isCurrentPasswordMatch = UserModel.isMatch(currentPw.getText());
                boolean isUpdated = false;
                if (!isCurrentPasswordMatch) {
                    new Alert(Alert.AlertType.WARNING, "Entered Password Is Wrong Please Try Again Later !").showAndWait();
                } else {
                    isUpdated = UserModel.update(newPW.getText());
                }
                if (isUpdated){
                    new Alert(Alert.AlertType.CONFIRMATION, "Your Password Is Updated Successfully").showAndWait();
                }
            }catch (SQLException e){
                new Alert(Alert.AlertType.ERROR, "SQL Error Please Try Again Later.").showAndWait();
            }
        }
        currentPw.setText(null);
        newPW.setText(null);
        currentPWPF.setText(null);
        newPasswordPWPF.setText(null);
        setBooleanFalseChangePw(false);
        isButtonsEnableChangePw();
        setUnfocusedColor();
    }

    @FXML
    void onActionUpdate(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do U Really Want To Update The User Details ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            boolean isUpdate= false;
            try {
                isUpdate = UserModel.update(new NewUser(jobRoll.getText(),
                                            userId.getText(),
                                            userName.getText(),
                                            employeeName.getText(),
                                            emailAddress.getText(),
                                            contactNo.getText()));
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "SQL Error.Please Try Again Later!").showAndWait();
            }
            if (isUpdate){
                new Alert(Alert.AlertType.CONFIRMATION, "User Details Update Successful.").showAndWait();
            }else{
                new Alert(Alert.AlertType.WARNING, "User Details Update UnSuccessful.").showAndWait();
            }
        }

        setBooleanFalse(false);
        isButtonsEnable();
        setUnfocusedColor();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setLabels();

        setDateAndTime();

        lblEyeOpen.setVisible(false);
        lblEyeOpen1.setVisible(false);
        currentPw.setVisible(false);
        newPW.setVisible(false);

        setBooleanFalse(false);
        isButtonsEnable();

        userNameLabel.setVisible(false);
        userPasswordError.setVisible(false);

        setBooleanFalseChangePw(false);
        isButtonsEnableChangePw();
    }

    private void setLabels() {
        NewUser newUser=null;
        try {
            newUser=UserModel.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error Please Try Again Later.").showAndWait();
        }

        jobRoll.setText(newUser.getType());
        userId.setText(Launcher.userId);
        userName.setText(newUser.getUserName());
        employeeName.setText(newUser.getEmployeeName());
        emailAddress.setText(newUser.getEmployeeEmailAddress());
        contactNo.setText(newUser.getEmployeeContactNo());
        currentUserName=newUser.getUserName();

        if (newUser.getType().equals("Manager")){
            isManager=true;
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
    void onActionCheckEmployeeName(KeyEvent keyEvent) {
        Pattern compile1= RegExPatterns.getNamePattern();
        Matcher matcher=compile1.matcher(employeeName.getText());
        boolean matches1=matcher.matches();

        Pattern compile2=RegExPatterns.getTwoStringCheckPattern();
        matcher=compile2.matcher(employeeName.getText());
        boolean matches2=matcher.matches();

        if (matches1 && matches2){
            employeeName.setUnFocusColor(Paint.valueOf("blue"));
            isEmployeeNameOk=true;
        }else{
            employeeName.setUnFocusColor(Paint.valueOf("red"));
            isEmployeeNameOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    void showPasswordNewOnAction(KeyEvent keyEvent) {
        newPassword=newPW.getText();
        newPasswordPWPF.setText(newPassword);
        Pattern compile= RegExPatterns.getUserPWPattern();
        Matcher matcher=compile.matcher(newPW.getText());
        boolean matches=matcher.matches();
        compile=RegExPatterns.getUserPWPattern();
        matcher=compile.matcher(newPasswordPWPF.getText());
        boolean matches1=matcher.matches();
        if (matches&&matches1&&checkNPW(newPassword,currentPassword)){
            newPW.setUnFocusColor(Paint.valueOf("blue"));
            newPasswordPWPF.setUnFocusColor(Paint.valueOf("blue"));
            this.isNPWOk=true;
            userPasswordError.setVisible(false);
        }else{
            newPW.setUnFocusColor(Paint.valueOf("red"));
            newPasswordPWPF.setUnFocusColor(Paint.valueOf("red"));
            this.isNPWOk=false;
            userPasswordError.setVisible(true);
        }
        isButtonsEnableChangePw();
    }

    @FXML
    void showPassWordOnAction(KeyEvent keyEvent) {
        currentPassword=currentPw.getText();
        currentPWPF.setText(currentPassword);
        if (checkCPW(currentPassword)){
            currentPWPF.setUnFocusColor(Paint.valueOf("blue"));
            currentPw.setUnFocusColor(Paint.valueOf("blue"));
            this.isCPwOk=true;
        }else{
            currentPWPF.setUnFocusColor(Paint.valueOf("red"));
            currentPw.setUnFocusColor(Paint.valueOf("red"));
            this.isCPwOk=false;
        }
    }

    @FXML
    void onActionHidePassword(KeyEvent keyEvent) {
        currentPassword=currentPWPF.getText();
        currentPw.setText(currentPassword);
        if (checkCPW(currentPassword)){
            currentPWPF.setUnFocusColor(Paint.valueOf("blue"));
            currentPw.setUnFocusColor(Paint.valueOf("blue"));
            this.isCPwOk=true;
        }else{
            currentPWPF.setUnFocusColor(Paint.valueOf("red"));
            currentPw.setUnFocusColor(Paint.valueOf("red"));
            this.isCPwOk=false;
        }
        isButtonsEnableChangePw();
    }

    @FXML
    void hidePasswordNewOnAction(KeyEvent keyEvent) {
        newPassword=newPasswordPWPF.getText();
        newPW.setText(newPassword);
        Pattern compile= RegExPatterns.getUserPWPattern();
        Matcher matcher=compile.matcher(newPW.getText());
        boolean matches=matcher.matches();
        compile=RegExPatterns.getUserPWPattern();
        matcher=compile.matcher(newPasswordPWPF.getText());
        boolean matches1=matcher.matches();
        if (matches&&matches1&&checkNPW(newPassword,currentPassword)){
            newPW.setUnFocusColor(Paint.valueOf("blue"));
            newPasswordPWPF.setUnFocusColor(Paint.valueOf("blue"));
            this.isNPWOk=true;
            userPasswordError.setVisible(false);
        }else{
            newPW.setUnFocusColor(Paint.valueOf("red"));
            newPasswordPWPF.setUnFocusColor(Paint.valueOf("red"));
            this.isNPWOk=false;
            userPasswordError.setVisible(true);
        }
        isButtonsEnableChangePw();
    }

    private boolean checkCPW(String currentPassword){

        ArrayList<String> passwordList=null;

        try {
            passwordList=UserModel.getUserPasswords();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String passWord : passwordList){
            if (passWord.equals(currentPassword)){
                return true;
            }
        }

        return false;

    }

    private boolean checkNPW(String newPassword,String currentPassword){

        ArrayList<String> passwordList=null;

        try {
            passwordList=UserModel.getUserPasswords();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String passWord : passwordList){
            if (passWord.equals(currentPassword)){
                continue;
            }
            if (passWord.equals(newPassword)){
                return false;
            }
        }

        return true;

    }

    private void isButtonsEnableChangePw() {
        if (isCPwOk&&isNPWOk){
            setButtonDisableChangePw(false);
        }else{
            setButtonDisableChangePw(true);
        }
    }

    private void setButtonDisableChangePw(boolean isDisable) {
        changePassword.setDisable(isDisable);
    }

    private void setBooleanFalseChangePw(boolean isFalse){
        isNPWOk=isFalse;
        isCPwOk=isFalse;
    }

    private boolean checkUserNameMatch(String userName) {
        ArrayList<String> userNameList=null;

        try {
            userNameList=UserModel.getAllUsername();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String user_Name :userNameList){
            if (userName.equals(user_Name)){
                if (userName.equals(currentUserName)){
                    continue;
                }
                return false;
            }
        }

        return true;
    }

    private void setUnfocusedColor(){
        userName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        employeeName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        emailAddress.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        contactNo.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        currentPWPF.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        currentPw.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        newPasswordPWPF.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        newPW.setUnFocusColor(Paint.valueOf("#4d4d4d"));
    }

}



