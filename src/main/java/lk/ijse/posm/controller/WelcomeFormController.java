package lk.ijse.posm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeFormController {

    @FXML
    private AnchorPane root;

    @FXML
    private ImageView image;

    @FXML
    private Button btn;

    @FXML
    void clickBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/loginForm.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Post Office Management System");
        stage.centerOnScreen();
        stage.setFullScreen(true);
    }

    @FXML
    void onKeyPressed(KeyEvent event) {
    }

}










