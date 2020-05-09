package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBoardController {
    public ImageView btnManageUsers;
    public JFXButton btnManageHospitals;
    public JFXButton btnManageQuarantineCenters;
    public AnchorPane root;
    public JFXButton btnGlobalCovid19;

    public void btnManageUsersOnAction(MouseEvent mouseEvent) {
        
    }

    public void btnManageHospitalsOnAction(ActionEvent actionEvent) {
        try {
            Scene manageHospitalScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Hospital.fxml")));
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(manageHospitalScene);
            primaryStage.setTitle("Global Covid-19");
            primaryStage.centerOnScreen();
            Image img = new Image("/images/virus.png");
            primaryStage.getIcons().add(img);
            primaryStage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnManageQuarantineCentersOnAction(ActionEvent actionEvent) {
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
}
