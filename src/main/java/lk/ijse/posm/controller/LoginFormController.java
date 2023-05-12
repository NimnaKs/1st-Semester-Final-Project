package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.posm.dto.User;
import lk.ijse.posm.model.UserModel;
import lk.ijse.posm.util.MailPack.Mail;
import lk.ijse.posm.util.ValidationPattern.RegExPatterns;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFormController implements Initializable {

    @FXML
    public JFXTextField userName;

    @FXML
    public JFXPasswordField passwordHide;

    @FXML
    public JFXTextField passwordShow;

    @FXML
    public Label lblEyeClose;

    @FXML
    public Label lblEyeOpen;

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton login;

    @FXML
    private Label forgetPassword;

    @FXML
    private Label userNameError;

    @FXML
    private Label userPasswordError;

    private String password=null;

    public static int OTP=0;

    public static String userNameAvailable=null;

    @FXML
    void onActionLogin(ActionEvent event) throws IOException {
        String passwords=null;

        try {
            passwords = UserModel.getCredentials(userName.getText());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "UserName is Invalid or UserPassword is Invalid !").show();
            e.printStackTrace();
        }

        if(passwords!=null && passwords.equals(password)){
            Thread thread=null;
            try {
                checkUserNameMatch(userName.getText());
                Mail mail = new Mail("Hii Mr. "+UserModel.getUserName(userNameAvailable)+".\nYou Log In to Post office Kalutara", UserModel.getUsreEmail(userNameAvailable),
                        "Login Information", "/Users/mac/Desktop/Post Office Management System 2/src/main/resources/assert/ReviewBackgroundForPdf.png");
                mail.outMail();
                thread=new Thread(mail);
                thread.start();
            } catch (SQLException | MessagingException e) {
                e.printStackTrace();
            }

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashBoardControlForm.fxml"));
            Scene scene = new Scene(anchorPane);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Post Office Management System");
            stage.centerOnScreen();
            stage.setFullScreen(true);
            thread.interrupt();

        }else{
            new Alert(Alert.AlertType.ERROR, "UserName is Invalid or UserPassword is Invalid !").show();
        }

    }

    @FXML
    void onMouseClickForgetPassword(MouseEvent event) throws IOException {

        Thread thread=null;
        if((userName.getText()!=null) && (checkUserNameMatch(userName.getText()))){
            do {
                OTP = newOtpPassword();
            } while (OTP > 10000);
            try {
                Mail mail = new Mail("Your OTP is "+OTP, UserModel.getUsreEmail(userNameAvailable),
                "Your OTP for login to  Kalutara Post Office System", "/Users/mac/Desktop/Post Office Management System 2/src/main/resources/assert/ReviewBackgroundForPdf.png");
                mail.outMail();
                thread=new Thread(mail);
                thread.start();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/emailGetLoginForm.fxml"));
            Scene scene = new Scene(anchorPane);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Post Office Management System");
            stage.centerOnScreen();
            stage.setFullScreen(true);
//            thread.interrupt();

        }else{
            new Alert(Alert.AlertType.ERROR,"Invalid Username.Please Enter user Name to proceed").showAndWait();
        }
    }

    @FXML
    void HidePasswordOnAction(KeyEvent event) {
        if (passwordHide.getText() != null) {
            Pattern compile = RegExPatterns.getUserPWPattern();
            Matcher matcher = compile.matcher(passwordHide.getText());
            boolean matches = matcher.matches();
            if (matches) {
                passwordHide.setUnFocusColor(Paint.valueOf("blue"));
                userPasswordError.setVisible(false);
            } else {
                passwordHide.setUnFocusColor(Paint.valueOf("red"));
                userPasswordError.setVisible(true);
            }
            password=passwordHide.getText();
            passwordShow.setText(password);
        }

    }

    @FXML
    void close_eye_clicked(MouseEvent event) {
        lblEyeClose.setVisible(false);
        lblEyeOpen.setVisible(true);
        passwordHide.setVisible(false);
        passwordShow.setVisible(true);
    }

    @FXML
    public void showPasswordOnAction(KeyEvent keyEvent) {
        if (passwordShow.getText() != null) {
            Pattern compile = RegExPatterns.getUserPWPattern();
            Matcher matcher = compile.matcher(passwordShow.getText());
            boolean matches = matcher.matches();
            if (matches) {
                passwordShow.setUnFocusColor(Paint.valueOf("blue"));
                userPasswordError.setVisible(false);
            } else {
                passwordShow.setUnFocusColor(Paint.valueOf("red"));
                userPasswordError.setVisible(true);
            }
            password = passwordShow.getText();
            passwordHide.setText(password);
        }
    }

    @FXML
    public void open_eye_clicked(MouseEvent mouseEvent) {
        lblEyeClose.setVisible(true);
        lblEyeOpen.setVisible(false);
        passwordHide.setVisible(true);
        passwordShow.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblEyeClose.setVisible(true);
        lblEyeOpen.setVisible(false);
        passwordHide.setVisible(true);
        passwordShow.setVisible(false);
        userNameError.setVisible(false);
        userPasswordError.setVisible(false);
    }

    @FXML
    void onActionUserName(KeyEvent event) {
        Pattern compile= RegExPatterns.getUserNamePattern();
        Matcher matcher=compile.matcher(userName.getText());
        boolean matches=matcher.matches();
        if (matches){
            userName.setUnFocusColor(Paint.valueOf("blue"));
            userNameError.setVisible(false);
        }else{
            userName.setUnFocusColor(Paint.valueOf("red"));
            userNameError.setVisible(true);
        }
    }

    private int newOtpPassword(){
        Random random=new Random();
        return random.nextInt(10000);
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
                userNameAvailable=user_Name;
                return true;
            }
        }

        return false;
    }
}

