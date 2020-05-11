package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class DashBoardController {
    public JFXButton btnManageHospitals;
    public JFXButton btnManageQuarantineCenters;
    public AnchorPane root;
    public JFXButton btnGlobalCovid19;
    public JFXButton btnManageUsers;

    public void btnManageHospitalsOnAction(ActionEvent actionEvent) {
        try {
            Scene manageHospitalScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Hospital.fxml")));
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(manageHospitalScene);
            primaryStage.setTitle("Hospital Management");
            primaryStage.centerOnScreen();
            Image img = new Image("/images/virus.png");
            primaryStage.getIcons().add(img);
            primaryStage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnManageQuarantineCentersOnAction(ActionEvent actionEvent) {
        try {
            Scene manageHospitalScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/QuarantineCenters.fxml")));
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(manageHospitalScene);
            primaryStage.setTitle("Quarantine Center Management");
            primaryStage.centerOnScreen();
            Image img = new Image("/images/virus.png");
            primaryStage.getIcons().add(img);
            primaryStage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnGlobalCovid19OnAction(ActionEvent actionEvent) {
        try {
            Scene globalCovid19Scene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Global_Covid_19.fxml")));
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(globalCovid19Scene);
            primaryStage.setTitle("Global Covid-19");
            primaryStage.centerOnScreen();
            Image img = new Image("/images/virus.png");
            primaryStage.getIcons().add(img);
            primaryStage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnManageUsersOnAction(ActionEvent actionEvent) {
        try {
            Scene manageHospitalScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Users.fxml")));
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(manageHospitalScene);
            primaryStage.setTitle("User Management");
            primaryStage.centerOnScreen();
            Image img = new Image("/images/virus.png");
            primaryStage.getIcons().add(img);
            primaryStage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) {
        logout();
    }

    public void imgLogoutOnMouseCLicked(MouseEvent mouseEvent) {
        logout();
    }

    private void logout(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to log out?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.get().equals(ButtonType.YES)){
            try {
                Scene manageHospitalScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Login.fxml")));
                Stage primaryStage = (Stage) root.getScene().getWindow();
                primaryStage.setScene(manageHospitalScene);
                primaryStage.setTitle("Login Form");
                primaryStage.centerOnScreen();
                Image img = new Image("/images/virus.png");
                primaryStage.getIcons().add(img);
                primaryStage.sizeToScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
