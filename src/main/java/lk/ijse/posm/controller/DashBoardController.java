package lk.ijse.posm.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lk.ijse.posm.dto.MonthIncome;
import lk.ijse.posm.model.*;
import lk.ijse.posm.util.Interfaces.MainController;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DashBoardController implements Initializable, MainController {

    @FXML
    private StackedBarChart<String,Number> chart;

    @FXML
    private Label totalSales;

    @FXML
    private Label totalOrders;

    @FXML
    private Label totalCustomers;

    @FXML
    private Label normalMails;

    @FXML
    private Label internationalMails;

    @FXML
    private Label parcels;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label totalSalesIncome;

    @FXML
    private Label emsCount;

    @FXML
    private Label dateAndTime;

    private volatile boolean stop=false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setLabels();

        setStackedChart();

        setDateAndTime();

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

    private void setStackedChart() {
        chart.setStyle("-fx-background-color: #21222D");
        final XYChart.Series<String,Number> series1=new XYChart.Series<>();
        series1.setName("Item Store Income");
        final XYChart.Series<String,Number> series2=new XYChart.Series<>();
        series2.setName("Parcel Income");
        final XYChart.Series<String,Number> series3=new XYChart.Series<>();
        series3.setName("Utility Bill Income");
        final XYChart.Series<String,Number> series4=new XYChart.Series<>();
        series4.setName("Money Transfer Income");


        ArrayList<MonthIncome> orderIncomeList=null;
        ArrayList<MonthIncome> parcelDetailsIncome=null;
        ArrayList<MonthIncome> billPaymentIncome=null;
        ArrayList<MonthIncome> changeDetailsIncome=null;

        try {
            orderIncomeList=OrderModel.getOrderTotalInEachMonth();
            parcelDetailsIncome=CuriorModel.getParcelTotalIncome();
            billPaymentIncome=PaymentModel.getBillPaymentIncome();
            changeDetailsIncome=ChangeModel.getChangeIncome();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"SQL ERROR .Please Try Again Later !").showAndWait();
        }

        for (int i = 0; i < orderIncomeList.size(); i++) {
            series1.getData().add(new XYChart.Data<>(orderIncomeList.get(i).getMonth(),(Number)orderIncomeList.get(i).getIncome()));
            series2.getData().add(new XYChart.Data<>(parcelDetailsIncome.get(i).getMonth(),(Number)parcelDetailsIncome.get(i).getIncome()));
            series3.getData().add(new XYChart.Data<>(billPaymentIncome.get(i).getMonth(),(Number)billPaymentIncome.get(i).getIncome()));
            series4.getData().add(new XYChart.Data<>(changeDetailsIncome.get(i).getMonth(),(Number)changeDetailsIncome.get(i).getIncome()));
        }

        chart.getData().addAll(series1,series2,series3,series4);

    }

    private void setLabels() {
        try {
            welcomeLabel.setText("nice To meet u Mr."+UserModel.getUserName(LoginFormController.userNameAvailable)+"!");
            totalSalesIncome.setText("Rs."+ OrderModel.getTotalSalesIncome());
            totalOrders.setText(OrderModel.getTotalOrders()+"");
            totalSales.setText(OrderModel.getTotalItemSales()+"");
            totalCustomers.setText(OrderModel.getTotalOrders()+"");
            emsCount.setText("5");
            normalMails.setText(MailModel.getTodayTotalMails()+"");
            internationalMails.setText(MailModel.getInternationalMails()+"");
            parcels.setText(CuriorModel.getParcelCount()+"");
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"SQL ERROR .Please Try Again Later.").showAndWait();
        }
    }

    @Override
    public void setStopTrue(){
        stop=true;
        Thread thread=setDateAndTime();
        thread.interrupt();
    }
}

