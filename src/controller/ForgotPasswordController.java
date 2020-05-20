package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgotPasswordController {

    public JFXButton btnHome;
    public AnchorPane root;
    public JFXButton btnSearch;
    public JFXTextField txtMail;
    public JFXTextField txtName;
    public JFXTextField txtContact;
    public Label lblMatching;
    public Label lblNotMatching;
    public Label lblValidateEmail;
    public Label lblValidateContact;
    public JFXPasswordField txtNewPassword;
    public JFXButton btnChange;
    public JFXPasswordField txtConfirmPassword;
    String name;

    public void btnHomeOnAction(ActionEvent actionEvent) throws IOException {
        goTOLogin();
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        search();
    }

    public void search() {
        if (txtMail.getText().isEmpty()) {
            txtMail.requestFocus();
            lblMatching.setVisible(false);
            lblNotMatching.setVisible(false);
        } else if (txtName.getText().isEmpty()) {
            txtName.requestFocus();
        } else if (txtContact.getText().isEmpty()) {
            txtContact.requestFocus();
        } else if (txtMail.getText().matches("[a-z0-9]+[@][a-z]+.com")) {
            lblValidateEmail.setVisible(false);
            if (txtContact.getText().matches("\\d{3}[-]\\d{7}")) {
                lblValidateContact.setVisible(false);
                Connection connection = DBConnection.getInstance().getConnection();

                String mail = txtMail.getText();
                name = txtName.getText();
                String contact = txtContact.getText();

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("select * from users where name = ? and contact = ? and e_mail = ?");
                    preparedStatement.setObject(1, name);
                    preparedStatement.setObject(2, contact);
                    preparedStatement.setObject(3, mail);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        lblNotMatching.setVisible(false);
                        lblMatching.setVisible(true);
                        txtNewPassword.setVisible(true);
                        txtConfirmPassword.setVisible(true);
                        btnChange.setVisible(true);
                        txtNewPassword.requestFocus();
                        txtMail.setDisable(true);
                        txtName.setDisable(true);
                        txtContact.setDisable(true);
                    } else {
                        lblMatching.setVisible(false);
                        lblNotMatching.setVisible(true);

                    }
                    txtMail.clear();
                    txtContact.clear();
                    txtName.clear();
                    txtMail.requestFocus();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                lblValidateContact.setVisible(true);
            }
        } else {
            lblValidateEmail.setVisible(true);
        }

    }

    public void btnChangeOnAction(ActionEvent actionEvent) {
        Connection connection = DBConnection.getInstance().getConnection();

        String passwordText = txtNewPassword.getText();
        String confirm = txtConfirmPassword.getText();
        if(passwordText.equals(confirm)){
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("update users set password = ? where name = ?");
                preparedStatement.setObject(1,confirm);
                preparedStatement.setObject(2,name);
                int i = preparedStatement.executeUpdate();
                if(i != 0){
                    new Alert(Alert.AlertType.CONFIRMATION,"Success").showAndWait();
                    goTOLogin();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            new Alert(Alert.AlertType.ERROR,"Please enter same password in two fields").showAndWait();
            txtNewPassword.clear();
            txtConfirmPassword.clear();
            txtNewPassword.requestFocus();
        }
    }

    public void goTOLogin() throws IOException {
        Scene dashBoardScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Login.fxml")));
        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(dashBoardScene);
        primaryStage.setTitle("Login");
        Image img = new Image("/images/virus.png");
        primaryStage.getIcons().add(img);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
    }
}
