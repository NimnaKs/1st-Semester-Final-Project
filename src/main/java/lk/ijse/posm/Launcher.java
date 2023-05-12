package lk.ijse.posm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.posm.util.GenerateReports.GeneratePdf;
import lk.ijse.posm.util.MailPack.Mail;

import java.time.LocalDate;


public class Launcher extends Application {

    public static LocalDate date=LocalDate.now();
    public static String userId="User0000";
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/welcomeForm.fxml"));
        stage.setTitle("Post Office Management System");
        stage.centerOnScreen();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setFullScreen(true);

    }
}
