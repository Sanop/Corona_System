package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class GlobalCovid19Controller {

    public Label lblDate;
    public Label lblConfirmedCases;
    public Label lblRecoverdCases;
    public Label lblDeathes;
    public JFXDatePicker dtpTody;
    public TextField txtconfirmedCases;
    public TextField txtRecoverdCases;
    public TextField txtDeathes;
    public JFXButton btnUpdate;
    public JFXButton btnHome;
    public AnchorPane root;
    public Label lblBanner;
    public static int newTotal_Confirmed_Cases;

    public void initialize() {
        loadLabels();
        txtconfirmedCases.setDisable(true);
        txtRecoverdCases.setDisable(true);
        txtDeathes.setDisable(true);
        btnUpdate.setDisable(true);
        dtpTody.requestFocus();

        txtconfirmedCases.setPromptText(lblConfirmedCases.getText());
        txtRecoverdCases.setPromptText(lblRecoverdCases.getText());
        txtDeathes.setPromptText(lblDeathes.getText());

        dtpTody.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                Calendar c = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                c.set(Calendar.MONTH,10);
                c.set(Calendar.DATE,01);
                c.set(Calendar.YEAR,2019);
                Date d = c.getTime();
                String d2 = sdf.format(d);
                LocalDate parse = LocalDate.parse(d2);
//                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(parse) < 0);
            }
        });

    }

    private void loadLabels() {
        Connection connection = DBConnection.getInstance().getConnection();
        int total_confirm_Cases = 0;
        int total_recoverd = 0;
        int total_deaths = 0;
        String date = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select date,confirmed_Cases,recoverd,deaths from global_Covid_19 ;");
            while(resultSet.next()){
                date = resultSet.getString(1);
                total_confirm_Cases = total_confirm_Cases + Integer.parseInt(resultSet.getString(2));
                total_recoverd = total_recoverd + Integer.parseInt(resultSet.getString(3));
                total_deaths = total_deaths + Integer.parseInt(resultSet.getString(4));
            }


            lblDate.setText(date);

            String pattern = "###,###,###";

            DecimalFormat format = new DecimalFormat(pattern);
            lblConfirmedCases.setText(format.format(total_confirm_Cases));
            lblRecoverdCases.setText(format.format(total_recoverd));
            lblDeathes.setText(format.format(total_deaths));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void txtconfirmedCasesOnAction(ActionEvent actionEvent) {
        txtRecoverdCases.requestFocus();
    }

    public void txtRecoverdCasesOnAction(ActionEvent actionEvent) {
        txtDeathes.requestFocus();
    }

    public void txtDeathesOnAction(ActionEvent actionEvent) {
        updateOnAction();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        updateOnAction();
    }

    private void updatePastDetails() {

        String date = dtpTody.getValue().toString();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from global_covid_19 where date = ?");

            preparedStatement.setObject(1, date);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            String confirm_Cases = resultSet.getString(2);
            String recoverd = resultSet.getString(3);
            String deathes = resultSet.getString(4);

            String total_Confirmed_Cases = resultSet.getString(5);
            String total_Recoverd = resultSet.getString(6);
            String total_Deaths = resultSet.getString(7);

            int int_ConfirmCases = Integer.parseInt(confirm_Cases);
            int int_Recoverd = Integer.parseInt(recoverd);
            int int_Deaths = Integer.parseInt(deathes);

            int int_Total_Confirmed_Cases = Integer.parseInt(total_Confirmed_Cases);
            int int_Total_Recoverd = Integer.parseInt(total_Recoverd);
            int int_Total_Deaths = Integer.parseInt(total_Deaths);

            int_Total_Confirmed_Cases = int_Total_Confirmed_Cases - int_ConfirmCases;
            int_Total_Recoverd = int_Total_Recoverd - int_Recoverd;
            int_Total_Deaths = int_Total_Deaths - int_Deaths;

            int new_Confirm_Cases = Integer.parseInt(txtconfirmedCases.getText());
            int new_Recoverd = Integer.parseInt(txtRecoverdCases.getText());
            int new_Deathes = Integer.parseInt(txtDeathes.getText());

            int_Total_Confirmed_Cases = int_Total_Confirmed_Cases + new_Confirm_Cases;
            int_Total_Recoverd = int_Total_Recoverd + new_Recoverd;
            int_Total_Deaths = int_Total_Deaths + new_Deathes;

            preparedStatement = connection.prepareStatement("update global_covid_19 set confirmed_Cases = ? , recoverd = ? , deaths = ? , total_Confirmed_Cases = ?, total_Recoverd = ? , total_Deaths = ?,status = ? where date = ?");

            preparedStatement.setObject(1, new_Confirm_Cases + "");
            preparedStatement.setObject(2, new_Recoverd + "");
            preparedStatement.setObject(3, new_Deathes + "");
            preparedStatement.setObject(4, int_Total_Confirmed_Cases + "");
            preparedStatement.setObject(5, int_Total_Recoverd + "");
            preparedStatement.setObject(6, int_Total_Deaths + "");
            preparedStatement.setObject(7,"Updated");
            preparedStatement.setObject(8,date);

            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void btnHomeOnAction(ActionEvent actionEvent) throws IOException {
        Scene dashBoardScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/DashBoard.fxml")));
        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(dashBoardScene);
        primaryStage.setTitle("Dash Board");
        Image img = new Image("/images/virus.png");
        primaryStage.getIcons().add(img);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
    }

    public int[] setTotals(String todayCases, String todayRecverd, String todayDeaths) {
        Connection connection = DBConnection.getInstance().getConnection();
        int total_Cases = 0;
        int total_Recoverd = 0;
        int total_Deaths = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select confirmed_Cases,recoverd,deaths from global_Covid_19;");
            while(resultSet.next()){
                total_Cases = Integer.parseInt(resultSet.getString(1)) + total_Cases;
                total_Recoverd = Integer.parseInt(resultSet.getString(2)) + total_Recoverd;
                total_Deaths = Integer.parseInt(resultSet.getString(3)) + total_Deaths;
            }

            total_Cases = total_Cases + Integer.parseInt(todayCases);
            total_Recoverd = total_Recoverd + Integer.parseInt(todayRecverd);
            total_Deaths = total_Deaths + Integer.parseInt(todayDeaths);

            int array[] = {total_Cases, total_Recoverd, total_Deaths};
            return array;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new int[0];
    }

    public void dtpTodyOnAction(ActionEvent actionEvent) {
        txtconfirmedCases.setDisable(false);
        txtRecoverdCases.setDisable(false);
        txtDeathes.setDisable(false);
        txtconfirmedCases.requestFocus();
        btnUpdate.setDisable(false);

        String date = null;
        try {
            date = dtpTody.getValue().toString();
        }catch (RuntimeException e){

        }


        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from global_covid_19 where date = ?");

            preparedStatement.setObject(1, date);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                lblBanner.setVisible(true);
                txtconfirmedCases.setText(resultSet.getString(2));
                txtRecoverdCases.setText(resultSet.getString(3));
                txtDeathes.setText(resultSet.getString(4));
                return;
            }
            txtconfirmedCases.clear();
            txtRecoverdCases.clear();
            txtDeathes.clear();
            txtconfirmedCases.requestFocus();
            lblBanner.setVisible(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void update() {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from global_covid_19 where date = ?");
            preparedStatement.setObject(1,dtpTody.getValue().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                updatePastDetails();
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String date = dtpTody.getValue().toString();
        String confirmedCases = txtconfirmedCases.getText();
        String recoverdCases = txtRecoverdCases.getText();
        String deaths = txtDeathes.getText();
        String adminID = LoginController.userID;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into global_covid_19 values(?,?,?,?,?,?,?,?,?)");
            preparedStatement.setObject(1, date);
            preparedStatement.setObject(2, confirmedCases);
            preparedStatement.setObject(3, recoverdCases);
            preparedStatement.setObject(4, deaths);

            int[] totals = setTotals(confirmedCases, recoverdCases, deaths);

            preparedStatement.setObject(5, totals[0] + "");
            preparedStatement.setObject(6, totals[1] + "");
            preparedStatement.setObject(7, totals[2] + "");
            preparedStatement.setObject(8, adminID);
            preparedStatement.setObject(9,"Not Update");

            int i = preparedStatement.executeUpdate();
            if (i != 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Successfully Updated").showAndWait();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateOnAction(){
        if (txtconfirmedCases.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Fields Can not be empty").showAndWait();
            txtconfirmedCases.requestFocus();
        } else if (txtRecoverdCases.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Fields can not be empty").showAndWait();
            txtRecoverdCases.requestFocus();
        } else if (txtDeathes.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Fields can not be empty").showAndWait();
            txtDeathes.requestFocus();
        } else if (txtconfirmedCases.getText().trim().matches("\\d+")) {
            if (txtRecoverdCases.getText().trim().matches("\\d+")) {
                if (txtDeathes.getText().trim().matches("\\d+")) {
                    update();
                    loadLabels();
                    txtconfirmedCases.clear();
                    txtconfirmedCases.setDisable(true);
                    txtRecoverdCases.clear();
                    txtRecoverdCases.setDisable(true);
                    txtDeathes.clear();
                    txtDeathes.setDisable(true);
                    btnUpdate.setDisable(true);
                    lblBanner.setVisible(false);
                    dtpTody.getEditor().clear();
                    dtpTody.setValue(null);
                    dtpTody.requestFocus();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Please enter only digits").showAndWait();
                    txtDeathes.requestFocus();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Please enter only digits").showAndWait();
                txtRecoverdCases.requestFocus();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please enter only digits").showAndWait();
            txtconfirmedCases.requestFocus();
        }
    }
}
