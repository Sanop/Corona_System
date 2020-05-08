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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
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
    public JFXButton btnRegister;
    public static String userID;
    public AnchorPane root;

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
        items.add("Quarantine Center Manager");
        items.add("Hospital Manager");
    }

    public void txtUserNameOnAction(ActionEvent actionEvent) {
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
        String userType = cmbUserType.getSelectionModel().getSelectedItem().toString();
        String userName = txtUserName.getText();
        String password = txtPassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("select id from users where name = ? and password = ? and status = ?");
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

    public void btnRegisterOnAction(ActionEvent actionEvent) {
    }

    public static void setUserID(String id){
        userID = id;
    }
}
