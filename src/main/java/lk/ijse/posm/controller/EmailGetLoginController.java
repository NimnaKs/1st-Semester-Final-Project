package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.posm.model.UserModel;
import lk.ijse.posm.util.MailPack.Mail;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

public class EmailGetLoginController {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField otpGetTxt;

    @FXML
    private JFXButton backBtn;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private Label sendAgainBtn;

    @FXML
    void onClickBack(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/loginForm.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Post Office Management System");
        stage.centerOnScreen();
        stage.setFullScreen(true);
    }

    @FXML
    void onClickSave(ActionEvent event) throws IOException {

        Thread thread=null;

        try {
            Mail mail = new Mail("Hii Mr. "+UserModel.getUserName(LoginFormController.userNameAvailable)+".\nYou Log In to Post office Kalutara", UserModel.getUsreEmail(LoginFormController.userNameAvailable),
                    "Login Information", "/Users/mac/Desktop/Post Office Management System 2/src/main/resources/assert/ReviewBackgroundForPdf.png");
            mail.outMail();
            thread=new Thread(mail);
            thread.start();
        } catch (SQLException | MessagingException e) {
            e.printStackTrace();
        }

        if (otpGetTxt.getText() != null || otpGetTxt.equals(LoginFormController.OTP)){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashBoardControlForm.fxml"));
            Scene scene = new Scene(anchorPane);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Post Office Management System");
            stage.centerOnScreen();
            stage.setFullScreen(true);


        }
    }

    @FXML
    void onClickSend(MouseEvent event) {
            do {
               LoginFormController.OTP = newOtpPassword();
            } while (LoginFormController.OTP > 10000);
            try {
                Mail mail = new Mail("Your OTP is "+LoginFormController.OTP, UserModel.getUsreEmail(LoginFormController.userNameAvailable),
                        "Your OTP for login to  Kalutara Post Office System", "/Users/mac/Desktop/Post Office Management System 2/src/main/resources/assert/ReviewBackgroundForPdf.png");
                mail.outMail();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private int newOtpPassword(){
        Random random=new Random();
        return random.nextInt(10000);
    }


}
