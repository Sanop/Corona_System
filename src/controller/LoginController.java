package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    public JFXComboBox cmbUserType;
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public JFXButton btnLogin;
    public static String userID;
    public AnchorPane root;
    public Label lblForgot;

    public void initialize(){
        txtUserName.setDisable(true);
        txtPassword.setDisable(true);
        btnLogin.setDisable(true);

        cmbUserType.requestFocus();
        loadCmd();
        cmbUserType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                txtUserName.setDisable(false);
                txtPassword.setDisable(false);
                btnLogin.setDisable(false);
                txtUserName.requestFocus();
            }
        });
    }

    private void loadCmd() {
        ObservableList items = cmbUserType.getItems();
        items.add("Admin");
        items.add("P.S.T.F Member");
        items.add("Hospital IT");
        items.add("Quarantine Center IT");
    }

    public void txtUserNameOnAction(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
        loginCode();
    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
        loginCode();
    }

    private void login() {
        try {
            Scene dashBoardScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/DashBoard.fxml")));
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(dashBoardScene);
            primaryStage.centerOnScreen();
            Image img = new Image("/images/virus.png");
            primaryStage.getIcons().add(img);
            primaryStage.sizeToScene();
            primaryStage.setTitle("Dash Bord");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginCode(){
        String userType = cmbUserType.getSelectionModel().getSelectedItem().toString();
        String userName = txtUserName.getText();
        String password = txtPassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("select id from users where user_Name = ? and password = ? and user_Role = ?");
            preparedStatement.setObject(1,userName);
            preparedStatement.setObject(2,password);
            preparedStatement.setObject(3,userType);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String userId = resultSet.getString(1);
                if(userType.equals("Admin")){
                    setUserID(userId);
                    login();
                }else{
                    new Alert(Alert.AlertType.ERROR,"You are not Admin").showAndWait();
                    cmbUserType.getSelectionModel().clearSelection();
                    cmbUserType.requestFocus();
                    txtUserName.setDisable(true);
                    txtUserName.clear();
                    txtPassword.setDisable(true);
                    txtPassword.clear();
                    btnLogin.setDisable(true);
                }
            }else{
                new Alert(Alert.AlertType.ERROR,"Something went wrong, Please Enter Correct Details").showAndWait();
                cmbUserType.getSelectionModel().clearSelection();
                cmbUserType.requestFocus();
                txtUserName.setDisable(true);
                txtUserName.clear();
                txtPassword.setDisable(true);
                txtPassword.clear();
                btnLogin.setDisable(true);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void setUserID(String id){
        userID = id;
    }

    public void lblForgotOnMouseClicked(MouseEvent mouseEvent) {
        try {
            Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/ForgotPassword.fxml")));
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(scene);
            Image img = new Image("/images/virus.png");
            primaryStage.getIcons().add(img);
            primaryStage.setTitle("Forgot Password");
            primaryStage.centerOnScreen();
            primaryStage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
