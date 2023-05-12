package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import lk.ijse.posm.util.GenerateReports.GeneratePdf;
import lk.ijse.posm.util.MailPack.Mail;
import lk.ijse.posm.util.ValidationPattern.RegExPatterns;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailGetCuriorFormController {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton sendBtn;

    @FXML
    private JFXButton skipBtn;

    @FXML
    private JFXTextField emailTxtField;

    private boolean isEmailOk=false;

    private GeneratePdf generatePdf;

    @FXML
    void onActionSend(ActionEvent event) {
        Thread thread=null;
        try {
            URL resource = getClass().getResource("/view/curiorForm.fxml");
            assert resource != null;
            Parent load = FXMLLoader.load(resource);
            root.getChildren().clear();
            root.getChildren().add(load);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if (isEmailOk) {
            try {
                Mail mail=new Mail("Your Curior Invoice Of Kalutara Post Office",emailTxtField.getText(),"Curior Report Information","invoiceCurior.pdf");
                mail.outMail();
                thread=new Thread(mail);
                thread.start();
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void onActionSkip(ActionEvent event) {

        try {
            URL resource = getClass().getResource("/view/curiorForm.fxml");
            assert resource != null;
            Parent load = FXMLLoader.load(resource);
            root.getChildren().clear();
            root.getChildren().add(load);
        }catch (IOException exception){
            exception.printStackTrace();
        }

    }

    @FXML
    void onActionEmailTxt(KeyEvent event) {
        Pattern compile= RegExPatterns.getEmailPattern();
        Matcher matcher=compile.matcher(emailTxtField.getText());
        boolean matches=matcher.matches();
        if (matches){
            emailTxtField.setUnFocusColor(Paint.valueOf("blue"));
            this.isEmailOk=true;
        }else{
            emailTxtField.setUnFocusColor(Paint.valueOf("red"));
            this.isEmailOk=false;
        }
    }

}


