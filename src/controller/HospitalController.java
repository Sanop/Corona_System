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


public class HospitalController {

    public AnchorPane root;
    public ListView lstHospital;
    public JFXButton btnNewHospital;
    public JFXTextField txtId;
    public JFXTextField txtHospitalName;
    public JFXTextField txtCity;
    public JFXComboBox cmbDistrict;
    public JFXTextField txtCapacity;
    public JFXTextField txtDirector;
    public JFXTextField txtDirectorPhoneNumber;
    public JFXTextField txtHospitalFaxNumber;
    public JFXTextField txtHospitalEMail;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public JFXTextField txtHospitalPhoneNo1;
    public JFXTextField txtHospitalPhoneNo2;
    public Label lblHidden;
    public Label lblvalidationDirectorPhoneNumber;
    public Label lblvalidationHospitalPhoneNubmer1;
    public Label lblvalidationHospitalPhoneNubmer2;
    public Label lblvalidationHospitalFaxNumber;
    public Label lblvalidationHospitalEmail;
    public Label lblCapacity;
    public TextField txtSearch;

    public void initialize(){
        common();
        loadHospitalList();
        loadDistrictCombo();
        lstHospital.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                String hospitalName = null;
                try {
                    hospitalName = lstHospital.getSelectionModel().getSelectedItem().toString();
                }catch (NullPointerException e){}

                Connection connection = DBConnection.getInstance().getConnection();

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("select * from hospitals where hospital_Name = ?");
                    preparedStatement.setObject(1,hospitalName);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if(resultSet.next()){
                        txtId.setText(resultSet.getString(1));
                        txtHospitalName.setText(resultSet.getString(2));
                        txtHospitalName.setDisable(false);
                        txtCity.setText(resultSet.getString(3));
                        txtCity.setDisable(false);
                        cmbDistrict.getSelectionModel().select(resultSet.getString(4));
                        cmbDistrict.setDisable(false);
                        txtCapacity.setText(resultSet.getString(5));
                        txtCapacity.setDisable(false);
                        txtDirector.setText(resultSet.getString(6));
                        txtDirector.setDisable(false);
                        txtDirectorPhoneNumber.setText(resultSet.getString(7));
                        txtDirectorPhoneNumber.setDisable(false);
                        txtHospitalPhoneNo1.setText(resultSet.getString(8));
                        txtHospitalPhoneNo1.setDisable(false);
                        txtHospitalPhoneNo2.setText(resultSet.getString(9));
                        txtHospitalPhoneNo2.setDisable(false);
                        txtHospitalFaxNumber.setText(resultSet.getString(10));
                        txtHospitalFaxNumber.setDisable(false);
                        txtHospitalEMail.setText(resultSet.getString(11));
                        txtHospitalEMail.setDisable(false);
                    }
                    btnSave.setText("Update");
                    btnSave.setDisable(false);
                    btnDelete.setDisable(false);

                    lblCapacity.setVisible(false);
                    lblvalidationDirectorPhoneNumber.setVisible(false);
                    lblvalidationHospitalPhoneNubmer1.setVisible(false);
                    lblvalidationHospitalPhoneNubmer2.setVisible(false);
                    lblvalidationHospitalFaxNumber.setVisible(false);
                    lblvalidationHospitalEmail.setVisible(false);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

    }

    private void common() {
        txtId.setEditable(false);
        txtId.clear();
        txtHospitalName.setDisable(true);
        txtHospitalName.clear();
        txtCity.setDisable(true);
        txtCity.clear();
        cmbDistrict.setDisable(true);
        cmbDistrict.getSelectionModel().clearSelection();
        txtCapacity.setDisable(true);
        txtCapacity.clear();
        txtDirector.setDisable(true);
        txtDirector.clear();
        txtDirectorPhoneNumber.setDisable(true);
        txtDirectorPhoneNumber.clear();
        txtHospitalPhoneNo1.setDisable(true);
        txtHospitalPhoneNo1.clear();
        txtHospitalPhoneNo2.setDisable(true);
        txtHospitalPhoneNo2.clear();
        txtHospitalFaxNumber.setDisable(true);
        txtHospitalFaxNumber.clear();
        txtHospitalEMail.setDisable(true);
        txtHospitalEMail.clear();
        btnSave.setDisable(true);
        btnSave.setText("Save");
        btnDelete.setDisable(true);
        btnNewHospital.requestFocus();
    }

    private void validateTextFieldsAndSave(){
        if(txtHospitalName.getText().trim().isEmpty()){
            txtHospitalName.requestFocus();
        }else if(txtCity.getText().trim().isEmpty()){
            txtCity.requestFocus();
        }else if(lblHidden.getText().trim().equals("0")){
            cmbDistrict.requestFocus();
        }else if(txtCapacity.getText().trim().isEmpty()){
            txtCapacity.requestFocus();
        }else if(txtDirector.getText().trim().isEmpty()){
            txtDirector.requestFocus();
        }else if(txtDirectorPhoneNumber.getText().trim().isEmpty()){
            txtDirectorPhoneNumber.requestFocus();
        }else if(txtHospitalPhoneNo1.getText().trim().isEmpty()){
            txtHospitalPhoneNo1.requestFocus();
        }else if(txtHospitalPhoneNo2.getText().trim().isEmpty()){
            txtHospitalPhoneNo2.requestFocus();
        }else if(txtHospitalFaxNumber.getText().trim().isEmpty()){
            txtHospitalFaxNumber.requestFocus();
        }else if(txtHospitalEMail.getText().trim().isEmpty()){
            txtHospitalEMail.requestFocus();
        }else if(txtCapacity.getText().trim().matches("\\d+")){
            lblCapacity.setVisible(false);
            if(txtDirectorPhoneNumber.getText().trim().matches("\\d{3}[-]\\d{7}")){
                lblvalidationDirectorPhoneNumber.setVisible(false);
                if(txtHospitalPhoneNo1.getText().trim().matches("\\d{3}[-]\\d{7}")){
                    lblvalidationHospitalPhoneNubmer1.setVisible(false);
                    if(txtHospitalPhoneNo2.getText().trim().matches("\\d{3}[-]\\d{7}")){
                        lblvalidationHospitalPhoneNubmer2.setVisible(false);
                        if(txtHospitalFaxNumber.getText().trim().matches("\\d{3}[-]\\d{7}")){
                            lblvalidationHospitalFaxNumber.setVisible(false);
                            if(txtHospitalEMail.getText().trim().matches("[a-z0-9]+[@][a-z]+.com")){
                                lblvalidationHospitalEmail.setVisible(false);
                                if(btnSave.getText().trim().equals("Save")){
                                    saveHospital();
                                }else{
                                    updateHospital();
                                }
                                common();
                                try {
                                    loadHospitalList();
                                    btnSave.setText("Save");
                                    btnSave.setDisable(true);
                                    btnDelete.setDisable(true);
                                    txtSearch.clear();
                                    txtSearch.setPromptText("Search Hospital");
                                }catch (NullPointerException e){

                                }
                            }else{
                                lblvalidationHospitalEmail.setVisible(true);
                                txtHospitalEMail.requestFocus();
                            }
                        }else{
                            lblvalidationHospitalFaxNumber.setVisible(true);
                            txtHospitalFaxNumber.requestFocus();
                        }
                    }else{
                        lblvalidationHospitalPhoneNubmer2.setVisible(true);
                        txtHospitalPhoneNo2.requestFocus();
                    }
                }else{
                    lblvalidationHospitalPhoneNubmer1.setVisible(true);
                    txtHospitalPhoneNo1.requestFocus();
                }
            }else{
                lblvalidationDirectorPhoneNumber.setVisible(true);
                txtDirectorPhoneNumber.requestFocus();
            }
        }else{
            lblCapacity.setVisible(true);
            txtCapacity.requestFocus();
        }
    }

    private void loadDistrictCombo() {
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

    public void btnHomeOnAction(ActionEvent actionEvent) throws IOException {
        Scene dashBoardScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/DashBoard.fxml")));
        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(dashBoardScene);
        Image img = new Image("/images/virus.png");
        primaryStage.getIcons().add(img);
        primaryStage.setTitle("Dash Board");
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
    }

    public void btnNewHospitalOnAction(ActionEvent actionEvent) {
        txtHospitalName.setDisable(false);
        txtId.clear();
        txtHospitalName.clear();
        txtCity.setDisable(false);
        txtCity.clear();
        cmbDistrict.setDisable(false);
        cmbDistrict.getSelectionModel().clearSelection();
        txtCity.setDisable(false);
        txtCity.clear();
        txtDirector.setDisable(false);
        txtDirector.clear();
        txtDirectorPhoneNumber.setDisable(false);
        txtDirectorPhoneNumber.clear();
        txtHospitalPhoneNo1.setDisable(false);
        txtHospitalPhoneNo1.clear();
        txtHospitalPhoneNo2.setDisable(false);
        txtHospitalPhoneNo2.clear();
        txtHospitalFaxNumber.setDisable(false);
        txtHospitalFaxNumber.clear();
        txtHospitalEMail.setDisable(false);
        txtHospitalEMail.clear();

        txtCapacity.setDisable(false);
        txtCapacity.clear();
        txtHospitalName.requestFocus();


        loadDistrictCombo();
        idAutoGenerate();

        lstHospital.getSelectionModel().clearSelection();

        btnSave.setText("Save");
        btnDelete.setDisable(true);
        btnSave.setDisable(false);
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        validateTextFieldsAndSave();
    }

    private void updateHospital() {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update hospitals set hospital_Name = ? , city = ? ,district = ? , capacity = ?, director = ? , director_Phone_No = ? , " +
                    "hospital_Phone_No_1 = ? , hospital_Phone_No_2  = ? , hospital_Fax_No = ? , hospital_Email = ? where id = ?");

            preparedStatement.setObject(1,txtHospitalName.getText());
            preparedStatement.setObject(2,txtCity.getText());
            preparedStatement.setObject(3,cmbDistrict.getValue().toString());
            preparedStatement.setObject(4,txtCapacity.getText());
            preparedStatement.setObject(5,txtDirector.getText());
            preparedStatement.setObject(6,txtDirectorPhoneNumber.getText());
            preparedStatement.setObject(7,txtHospitalPhoneNo1.getText());
            preparedStatement.setObject(8,txtHospitalPhoneNo2.getText());
            preparedStatement.setObject(9,txtHospitalFaxNumber.getText());
            preparedStatement.setObject(10,txtHospitalEMail.getText());
            preparedStatement.setObject(11,txtId.getText());

            int i = preparedStatement.executeUpdate();
            if(i != 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Successfully Updated").showAndWait();
            }else {
                new Alert(Alert.AlertType.ERROR,"Something went wrong").showAndWait();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void saveHospital() {
        String id = txtId.getText();
        String hospital_Name = txtHospitalName.getText();
        String city = txtCity.getText();
        String district = cmbDistrict.getValue().toString();
        String capacity = txtCapacity.getText();
        String director = txtDirector.getText();
        String director_Phone_No = txtDirectorPhoneNumber.getText();
        String hospital_Phone_No_1 = txtHospitalPhoneNo1.getText();
        String hospital_Phone_No_2 = txtHospitalPhoneNo2.getText();
        String hospital_Fax_No = txtHospitalFaxNumber.getText();
        String email = txtHospitalEMail.getText();
        String userID = LoginController.userID;

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into hospitals values(?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,hospital_Name);
            preparedStatement.setObject(3,city);
            preparedStatement.setObject(4,district);
            preparedStatement.setObject(5,capacity);
            preparedStatement.setObject(6,director);
            preparedStatement.setObject(7,director_Phone_No);
            preparedStatement.setObject(8,hospital_Phone_No_1);
            preparedStatement.setObject(9,hospital_Phone_No_2);
            preparedStatement.setObject(10,hospital_Fax_No);
            preparedStatement.setObject(11,email);
            preparedStatement.setObject(12,userID);

            int i = preparedStatement.executeUpdate();

            if(i != 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Successfully Added").showAndWait();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from hospitals where id = ?");
            preparedStatement.setObject(1,txtId.getText());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you realy want to Delete this Hospital", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if(buttonType.get().equals(ButtonType.YES)){
                preparedStatement.executeUpdate();
            }
            loadHospitalList();
            common();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void idAutoGenerate(){
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from hospitals order by id desc limit 1");
            if(resultSet.next()){
                String oldID = resultSet.getString(1);
                int number = Integer.parseInt(oldID.substring(1, 4)) + 1;
                if(number < 10){
                    txtId.setText("H00"+number);
                    return;
                }else if(number < 100){
                    txtId.setText("H0"+number);
                    return;
                }else{
                    txtId.setText("H"+number);
                    return;
                }
            }else{
                txtId.setText("H001");
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void cmbDistrict(ActionEvent actionEvent) {
        lblHidden.setText("1");
        txtCapacity.requestFocus();
    }

    public void loadHospitalList(){
        ObservableList items = lstHospital.getItems();
        try {
            items.clear();
        }catch (NullPointerException e){}
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select hospital_Name from hospitals");
            while(resultSet.next()){
                items.add(resultSet.getString(1));
            }
            lstHospital.refresh();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void txtSearchOnKeyReleased(KeyEvent keyEvent) {
        String text = txtSearch.getText();
        // select hospital_Name from hospitals where hospital_Name like '%h%';
        Connection connection = DBConnection.getInstance().getConnection();
        ObservableList items = lstHospital.getItems();
        items.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select hospital_Name from hospitals where hospital_Name like '%"+text+"%'");
            while(resultSet.next()){
                items.add(resultSet.getString(1));
            }
            lstHospital.refresh();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void txtSearchOnMouseClicked(MouseEvent mouseEvent) {
        common();
        txtSearch.requestFocus();
    }

    public void txtHospitalNameOnActon(ActionEvent actionEvent) {
        txtCity.requestFocus();
    }

    public void txtCityOnAction(ActionEvent actionEvent) {
        cmbDistrict.requestFocus();
    }

    public void txtCapacityOnAction(ActionEvent actionEvent) {
        txtDirector.requestFocus();
    }

    public void txtDirectorOnAction(ActionEvent actionEvent) {
        txtDirectorPhoneNumber.requestFocus();
    }

    public void txtDirectorPhoneNumberOnAction(ActionEvent actionEvent) {
        txtHospitalPhoneNo1.requestFocus();
    }

    public void txtHospitalPhoneNo1OnAction(ActionEvent actionEvent) {
        txtHospitalPhoneNo2.requestFocus();
    }

    public void txtHospitalPhoneNo2OnAction(ActionEvent actionEvent) {
        txtHospitalFaxNumber.requestFocus();
    }

    public void txtHospitalFaxNumberOnAction(ActionEvent actionEvent) {
        txtHospitalEMail.requestFocus();
    }

    public void txtHospitalEMailOnAction(ActionEvent actionEvent) {
        validateTextFieldsAndSave();
    }
}
