package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class QuarantineCenterController {
    public AnchorPane root;
    public JFXButton btnHome;
    public TextField txtSearch;
    public ListView lstCenters;
    public JFXButton btnNewCenter;
    public JFXTextField txtId;
    public JFXTextField txtCenterName;
    public JFXTextField txtCity;
    public JFXComboBox cmbDistrict;
    public JFXTextField txtHead;
    public JFXTextField txtHeadContactNumber;
    public JFXTextField txtCenterContactNumber1;
    public JFXTextField txtCenterContactNumber2;
    public JFXTextField txtCapacity;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public Label lblHeadContactNumber;
    public Label lblContactNumber1;
    public Label lblContactNumber2;
    public Label lblCapacity;
    public Label lblHidden;

    String id;
    String name;
    String city;
    String district;
    String head;
    String head_Contact;
    String contact_1;
    String contact_2;
    String capacity;
    String userID = LoginController.userID;

    public void initialize(){
        txtId.setEditable(false);
        lblHeadContactNumber.setVisible(false);
        lblContactNumber1.setVisible(false);
        lblContactNumber2.setVisible(false);
        lblCapacity.setVisible(false);

        commonSetDisable();

        btnSave.setDisable(true);
        btnDelete.setDisable(true);

        loadCenterList();
        loadComboBox();

        lstCenters.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(lstCenters.getSelectionModel().getSelectedItems().isEmpty()){
                    return;
                }
                String centerName = lstCenters.getSelectionModel().getSelectedItem().toString();

                Connection connection = DBConnection.getInstance().getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("select * from quarantineCenters where center_Name = ?");
                    preparedStatement.setObject(1,centerName);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if(resultSet.next()){
                        txtId.setText(resultSet.getString(1));
                        txtCenterName.setText(resultSet.getString(2));
                        txtCity.setText(resultSet.getString(3));
                        cmbDistrict.getSelectionModel().select(resultSet.getString(4));
                        txtHead.setText(resultSet.getString(5));
                        txtHeadContactNumber.setText(resultSet.getString(6));
                        txtCenterContactNumber1.setText(resultSet.getString(7));
                        txtCenterContactNumber2.setText(resultSet.getString(8));
                        txtCapacity.setText(resultSet.getString(9));

                        common();

                        btnSave.setText("Update");
                        btnSave.setDisable(false);
                        btnDelete.setDisable(false);

                        lblHeadContactNumber.setVisible(false);
                        lblContactNumber1.setVisible(false);
                        lblContactNumber2.setVisible(false);
                        lblCapacity.setVisible(false);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        cmbDistrict.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                txtHead.requestFocus();
                lblHidden.setText("1");
            }
        });

    }

    public void btnHomeOnAction(ActionEvent actionEvent) throws IOException {
        Scene dashBoardScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/DashBoard.fxml")));
        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(dashBoardScene);
        Image img = new Image("/images/virus.png");
        primaryStage.getIcons().add(img);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Dash Board");
        primaryStage.sizeToScene();
    }

    private void common(){
        txtCenterName.setDisable(false);
        txtCity.setDisable(false);
        cmbDistrict.setDisable(false);
        txtHead.setDisable(false);
        txtHeadContactNumber.setDisable(false);
        txtCenterContactNumber1.setDisable(false);
        txtCenterContactNumber2.setDisable(false);
        txtCapacity.setDisable(false);
    }

    public void btnNewCenterOnAction(ActionEvent actionEvent) {

        common();
        commonClear();
        btnSave.setDisable(false);
        btnSave.setText("Save");
        btnDelete.setDisable(true);

        idAutoGenerate();

        txtCenterName.requestFocus();

        lstCenters.getSelectionModel().clearSelection();
    }

    public void loadComboBox(){
        ObservableList items = cmbDistrict.getItems();
        items.clear();
        items.add("Jaffna");
        items.add("Kilinochchi");
        items.add("Mannar");
        items.add("Mullaitivu");
        items.add("Vavuniya");
        items.add("Puttalam");
        items.add("Kurunegala");
        items.add("Gampaha");
        items.add("Colombo");
        items.add("Kalutara");
        items.add("Anuradhapura");
        items.add("Polonnaruwa");
        items.add("Matale");
        items.add("Kandy");
        items.add("Nuwara Eliya");
        items.add("Kegalle");
        items.add("Ratnapura");
        items.add("Trincomalee");
        items.add("Batticaloa");
        items.add("Ampara");
        items.add("Badulla");
        items.add("Monaragala");
        items.add("Hambantota");
        items.add("Matara");
        items.add("Galle");
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        validateAndSave();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        id = txtId.getText();
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from quarantineCenters where id = ?");
            preparedStatement.setObject(1,id);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to delete this center", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if(buttonType.get().equals(ButtonType.YES)){
                preparedStatement.executeUpdate();
            }
            commonClear();
            commonSetDisable();
            btnSave.setText("Save");
            btnDelete.setDisable(true);
            btnSave.setDisable(true);
            btnNewCenter.requestFocus();
            loadCenterList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void idAutoGenerate(){
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from quarantineCenters order by id desc limit 1");
            if(resultSet.next()){
                String oldID = resultSet.getString(1);
                int newID = Integer.parseInt(oldID.substring(1, 4)) + 1;
                if(newID < 10){
                    txtId.setText("C00"+newID);
                }else if(newID < 100){
                    txtId.setText("C0"+newID);
                }else{
                    txtId.setText("C"+newID);
                }
            }else{
                txtId.setText("C001");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void validateAndSave(){
        if(txtCenterName.getText().isEmpty()){
            txtCenterName.requestFocus();
        }else if(txtCity.getText().isEmpty()){
            txtCity.requestFocus();
        }else if(lblHidden.getText().equals("0")){
            cmbDistrict.requestFocus();
        }else if(txtHead.getText().isEmpty()){
            txtHead.requestFocus();
        }else if(txtHeadContactNumber.getText().isEmpty()){
            txtHeadContactNumber.requestFocus();
        }else if(txtCenterContactNumber1.getText().isEmpty()){
            txtCenterContactNumber1.requestFocus();
        }else if(txtCenterContactNumber2.getText().isEmpty()){
            txtCenterContactNumber2.requestFocus();
        }else if(txtCapacity.getText().isEmpty()){
            txtCapacity.requestFocus();
        }else if(txtHeadContactNumber.getText().matches("\\d{3}[-]\\d{7}")){
            lblHeadContactNumber.setVisible(false);
            if(txtCenterContactNumber1.getText().matches("\\d{3}[-]\\d{7}")){
                lblContactNumber1.setVisible(false);
                if(txtCenterContactNumber2.getText().matches("\\d{3}[-]\\d{7}")){
                    lblContactNumber2.setVisible(false);
                    if(txtCapacity.getText().matches("\\d+")){
                        lblCapacity.setVisible(false);
                        if(btnSave.getText().equals("Save")){
                            saveCenters();
                        }else{
                            updateCenters();
                        }

                        loadCenterList();

                        commonClear();
                        commonSetDisable();
                        btnSave.setDisable(true);
                        btnSave.setText("Save");
                        btnDelete.setDisable(true);
                        btnNewCenter.requestFocus();
                        lblHidden.setText("0");
                        lstCenters.getSelectionModel().clearSelection();
                    }else{
                        lblCapacity.setVisible(true);
                        txtCapacity.requestFocus();
                    }
                }else{
                    lblContactNumber2.setVisible(true);
                    txtCenterContactNumber2.requestFocus();
                }
            }else{
                lblContactNumber1.setVisible(true);
                txtCenterContactNumber1.requestFocus();
            }
        }else{
            lblHeadContactNumber.setVisible(true);
            txtHeadContactNumber.requestFocus();
        }
    }

    private void updateCenters() {
        initializeVariables();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update quarantineCenters set " +
                    "center_Name = ?," +
                    "city = ?," +
                    "district = ?," +
                    "head = ?," +
                    "head_Contact = ?," +
                    "center_Contact_Number_1 = ?," +
                    "center_Contact_Number_2 = ?," +
                    "capacity = ? where id = ?");

            preparedStatement.setObject(1,name);
            preparedStatement.setObject(2,city);
            preparedStatement.setObject(3,district);
            preparedStatement.setObject(4,head);
            preparedStatement.setObject(5,head_Contact);
            preparedStatement.setObject(6,contact_1);
            preparedStatement.setObject(7,contact_2);
            preparedStatement.setObject(8,capacity);
            preparedStatement.setObject(9,id);

            int i = preparedStatement.executeUpdate();

            if(i != 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Successfully Updated").showAndWait();
            }else{
                new Alert(Alert.AlertType.ERROR,"Some thing went wrong Please try again").showAndWait();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void commonClear() {
        txtId.clear();
        txtCenterName.clear();
        txtHead.clear();
        txtCity.clear();
        cmbDistrict.getSelectionModel().clearSelection();
        txtHeadContactNumber.clear();
        txtCenterContactNumber1.clear();
        txtCenterContactNumber2.clear();
        txtCapacity.clear();
    }

    private void commonSetDisable() {
        txtCenterName.setDisable(true);
        txtCity.setDisable(true);
        cmbDistrict.setDisable(true);
        txtHead.setDisable(true);
        txtHeadContactNumber.setDisable(true);
        txtCenterContactNumber1.setDisable(true);
        txtCenterContactNumber2.setDisable(true);
        txtCapacity.setDisable(true);
    }

    private void saveCenters() {

        initializeVariables();

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into quarantineCenters values(?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,name);
            preparedStatement.setObject(3,city);
            preparedStatement.setObject(4,district);
            preparedStatement.setObject(5,head);
            preparedStatement.setObject(6,head_Contact);
            preparedStatement.setObject(7,contact_1);
            preparedStatement.setObject(8,contact_2);
            preparedStatement.setObject(9,capacity);
            preparedStatement.setObject(10,userID);
            preparedStatement.setObject(11,"Not Reserved");

            int i = preparedStatement.executeUpdate();

            if(i != 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Successfully Added").showAndWait();
            }else{
                new Alert(Alert.AlertType.ERROR,"Something went wrong").showAndWait();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void loadCenterList(){
        Connection connection = DBConnection.getInstance().getConnection();
        ObservableList items = lstCenters.getItems();
        items.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select center_Name from quarantineCenters");
            while(resultSet.next()){
                items.add(resultSet.getString(1));
            }
            lstCenters.refresh();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void initializeVariables(){
        id = txtId.getText();
        name = txtCenterName.getText();
        city = txtCity.getText();
        district = cmbDistrict.getValue().toString();
        head = txtHead.getText();
        head_Contact = txtHeadContactNumber.getText();
        contact_1 = txtCenterContactNumber1.getText();
        contact_2 = txtCenterContactNumber2.getText();
        capacity = txtCapacity.getText();
    }

    public void txtSearchOnKeyReleased(KeyEvent keyEvent) {
        String text = txtSearch.getText();
        ObservableList items = lstCenters.getItems();
        items.clear();
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select center_Name from quarantineCenters where center_Name like '%" + text + "%'");
            while(resultSet.next()){
                items.add(resultSet.getString(1));
            }
            lstCenters.refresh();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void txtSearchOnMouseClicked(MouseEvent mouseEvent) {
        commonClear();
        commonSetDisable();
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
        btnSave.setText("Save");
        lstCenters.getSelectionModel().clearSelection();
        txtSearch.requestFocus();
        lblHeadContactNumber.setVisible(false);
        lblContactNumber1.setVisible(false);
        lblContactNumber2.setVisible(false);
        lblCapacity.setVisible(false);
    }

    public void txtCenterNameOnAction(ActionEvent actionEvent) {
        txtCity.requestFocus();
    }

    public void txtCityOnAction(ActionEvent actionEvent) {
        cmbDistrict.requestFocus();
    }

    public void txtHeadOnAction(ActionEvent actionEvent) {
        txtHeadContactNumber.requestFocus();
    }

    public void txtHeadContactNumberOnAction(ActionEvent actionEvent) {
        txtCenterContactNumber1.requestFocus();
    }

    public void txtCenterContactNumber1OnAction(ActionEvent actionEvent) {
        txtCenterContactNumber2.requestFocus();
    }

    public void txtCenterContactNumber2OnAction(ActionEvent actionEvent) {
        txtCapacity.requestFocus();
    }

    public void txtCapacityOnAction(ActionEvent actionEvent) {
        validateAndSave();
    }
}
