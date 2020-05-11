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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.UsersTM;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Optional;
import java.util.Random;

public class UsersController {
    public JFXButton btnHome;
    public AnchorPane root;
    public JFXButton btnAddNewUser;
    public JFXTextField txtName;
    public JFXTextField txtContactNumber;
    public JFXTextField txtMail;
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public ImageView imgPassword;
    public JFXComboBox cmbUserRole;
    public JFXComboBox cmbHospitalsAndCenters;
    public JFXButton btnSave;
    public TextField txtSearch;
    public TableView<UsersTM> tblUsers;
    public Label lblUserID;
    public JFXTextField txtPasswordShow;
    public Label lblUserName;
    public Label lblPassword;
    public Label lblLocation;
    public Label lblUserRole;
    public Label lblValidateContactNumber;
    public Label lblValidateEmail;
    public JFXButton btnOK;
    public Label lblHidden;

    public void initialize(){
        cmbHospitalsAndCenters.setVisible(false);
        commonDisable(true);
        loadUserRoleCombo();
        txtPasswordShow.setVisible(false);
        btnOK.setDisable(true);
        cmbUserRole.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                if(cmbUserRole.getSelectionModel().getSelectedItem() == null){
                    return;
                }
                cmbHospitalsAndCenters.getSelectionModel().select(null);
                cmbHospitalsAndCenters.setEditable(false);
                String role = cmbUserRole.getSelectionModel().getSelectedItem().toString();

                switch (role){
                    case "Hospital IT":
                        cmbHospitalsAndCenters.setVisible(true);
                        cmbHospitalsAndCenters.setPromptText("Select Hospital");
                        loadHospitalCombo();
                        break;
                    case "Quarantine Center IT":
                        cmbHospitalsAndCenters.setVisible(true);
                        cmbHospitalsAndCenters.setPromptText("Select Quarantine Center");
                        loadQuarantineCenterCombo();
                        break;
                    default:
                        cmbHospitalsAndCenters.setVisible(false);
                        break;
                }
            }
        });

        cmbUserRole.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                lblUserRole.setText("1");
            }
        });

        cmbHospitalsAndCenters.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                lblLocation.setText("1");
            }
        });

        tblUsers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("userName"));
        tblUsers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblUsers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("role"));
        tblUsers.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("button"));

        loadTable();

        tblUsers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UsersTM>() {
            @Override
            public void changed(ObservableValue<? extends UsersTM> observable, UsersTM oldValue, UsersTM newValue) {
                if(tblUsers.getSelectionModel().getSelectedItem() == null){
                    return;
                }
                UsersTM selectedItem = tblUsers.getSelectionModel().getSelectedItem();
                btnSave.setText("Update");
                btnAddNewUser.setDisable(true);
                Connection connection = DBConnection.getInstance().getConnection();

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("select * from users where user_Name = ?");
                    preparedStatement.setObject(1,selectedItem.getUserName());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    lblUserID.setText(resultSet.getString(1));
                    txtName.setText(resultSet.getString(2));
                    txtContactNumber.setText(resultSet.getString(3));
                    txtMail.setText(resultSet.getString(4));
                    txtUserName.setText(resultSet.getString(5));
                    txtPassword.setText(resultSet.getString(6));
                    txtPasswordShow.setText(resultSet.getString(6));
                    String role = resultSet.getString(7);
                    cmbUserRole.getSelectionModel().select(role);
                    if(role.equals("Hospital IT")){
                        cmbHospitalsAndCenters.setVisible(true);
                        cmbHospitalsAndCenters.setPromptText("Select Hospital");

                        preparedStatement = connection.prepareStatement("select location from users where id = ?");
                        preparedStatement.setObject(1,lblUserID.getText());
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        String location = resultSet.getString(1);
                        preparedStatement = connection.prepareStatement("select id from hospitals where hospital_Name = ?");
                        preparedStatement.setObject(1,location);
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        String id = resultSet.getString(1);
                        String result = id+"-"+location;
                        System.out.println(result);
                        loadHospitalCombo();
                        cmbHospitalsAndCenters.setEditable(true);
                        cmbHospitalsAndCenters.getSelectionModel().select(result);
                    }else if(role.equals("Quarantine Center IT")){
                        cmbHospitalsAndCenters.setVisible(true);
                        cmbHospitalsAndCenters.setPromptText("Select Quarantine Center");
                        loadQuarantineCenterCombo();
                        preparedStatement = connection.prepareStatement("select location from users where id = ?");
                        preparedStatement.setObject(1,lblUserID.getText());
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        String location = resultSet.getString(1);
                        preparedStatement = connection.prepareStatement("select id from quarantineCenters where center_Name = ?");
                        preparedStatement.setObject(1,location);
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        String id = resultSet.getString(1);
                        String result = id+"-"+location;
                        System.out.println(result);
                        loadHospitalCombo();
                        cmbHospitalsAndCenters.setEditable(true);
                        cmbHospitalsAndCenters.getSelectionModel().select(result);
                    }
                    commonDisable(false);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                ObservableList<UsersTM> items = tblUsers.getItems();
                JFXButton button = selectedItem.getButton();
                String id = null;
                button.setOnAction(event ->
                        delete()
                );


                btnOK.setDisable(false);
                btnOK.requestFocus();
                lblPassword.setVisible(false);
                lblUserName.setVisible(false);
            }
        });
    }

    private void delete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to delete this member", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.get().equals(ButtonType.YES)){
            String id = lblUserID.getText();
            Connection connection = DBConnection.getInstance().getConnection();

            String role = cmbUserRole.getSelectionModel().getSelectedItem().toString();

            switch(role){
                case "Hospital IT":
                    String location = cmbHospitalsAndCenters.getSelectionModel().getSelectedItem().toString();
                    location = location.substring(5, location.length());
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement("update hospitals set status = 'Not Reserved' where hospital_Name = ?");
                        preparedStatement.setObject(1,location);
                        preparedStatement.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    break;
                case "Quarantine Center IT":
                    String location2 = cmbHospitalsAndCenters.getSelectionModel().getSelectedItem().toString();
                    location2 = location2.substring(5, location2.length());
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement("update quarantineCenters set status = 'Not Reserved' where center_Name = ?");
                        preparedStatement.setObject(1,location2);
                        preparedStatement.executeUpdate();

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    break;
                default:
                    break;
            }


            try {
                PreparedStatement preparedStatement = connection.prepareStatement("delete from users where id = ?");
                preparedStatement.setObject(1,id);
                preparedStatement.executeUpdate();
                loadTable();
                commonClear();
                commonDisable(true);
                btnAddNewUser.setDisable(false);
                btnAddNewUser.requestFocus();
                btnSave.setText("Save");
                btnOK.setDisable(false);
                lblPassword.setVisible(false);
                lblUserName.setVisible(false);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    private void loadUserRoleCombo() {
        ObservableList items = cmbUserRole.getItems();
        items.clear();
        items.add("Admin");
        items.add("P.S.T.F Member");
        items.add("Hospital IT");
        items.add("Quarantine Center IT");
    }

    private  void loadQuarantineCenterCombo(){
        ObservableList items = cmbHospitalsAndCenters.getItems();
        items.clear();
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id,center_Name from quarantineCenters where status = 'Not Reserved'");
            String id = "";
            String name = "";
            while(resultSet.next()){
                id = resultSet.getString(1);
                name = resultSet.getString(2);
                String result = id+"-"+name ;
                items.add(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void loadHospitalCombo(){
        ObservableList items = cmbHospitalsAndCenters.getItems();
        items.clear();
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id,hospital_Name from hospitals where status = 'Not Reserved'");
            String id = "";
            String name = "";
            while(resultSet.next()){
                id = resultSet.getString(1);
                name = resultSet.getString(2);
                String result = id+"-"+name ;
                items.add(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void commonDisable(boolean flag){
        txtName.setDisable(flag);
        txtContactNumber.setDisable(flag);
        txtMail.setDisable(flag);
        txtUserName.setDisable(flag);
        txtPassword.setDisable(flag);
        cmbUserRole.setDisable(flag);
        btnSave.setDisable(flag);
        imgPassword.setDisable(flag);
        txtPasswordShow.setDisable(flag);
        lblValidateContactNumber.setDisable(flag);
        lblValidateEmail.setDisable(flag);
    }

    public void commonClear(){
        lblUserID.setText("");
        txtName.clear();
        txtContactNumber.clear();
        txtMail.clear();
        txtUserName.clear();
        txtPassword.clear();
        txtPasswordShow.clear();
        cmbHospitalsAndCenters.setVisible(false);
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

    public void btnAddNewUserOnAction(ActionEvent actionEvent) {
        commonDisable(false);
        commonClear();
        txtName.requestFocus();
        idAutoGenerate();

        txtUserName.setText(randomNumbers());
        txtPassword.setText(randomNumbers());

        lblUserName.setVisible(true);
        lblPassword.setVisible(true);
        btnSave.setText("Save");
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if(txtName.getText().isEmpty()){
            txtName.requestFocus();
        }else if(txtContactNumber.getText().isEmpty()){
            txtContactNumber.requestFocus();
        }else if(txtMail.getText().isEmpty()){
            txtMail.requestFocus();
        }else if(txtUserName.getText().isEmpty()){
            txtUserName.requestFocus();
        }else if(txtPassword.getText().isEmpty()){
            txtPassword.requestFocus();
        }else if(lblUserRole.getText().equals("0")){
            cmbUserRole.requestFocus();
        }else if(txtContactNumber.getText().matches("\\d{3}[-]\\d{7}")){
            lblValidateContactNumber.setVisible(false);
            if(txtMail.getText().trim().matches("[a-z0-9]+[@][a-z]+.com")){
                lblValidateEmail.setVisible(false);
                if(btnSave.getText().equals("Update")){
                    try {
                        update();
                    }catch (RuntimeException e){
                        cmbHospitalsAndCenters.requestFocus();
                    }
                }else{
                    Connection connection = DBConnection.getInstance().getConnection();
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement("select user_Name from users where user_Name = ?");
                        preparedStatement.setObject(1,txtUserName.getText());
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if(resultSet.next()){
                            new Alert(Alert.AlertType.ERROR,"This User Name is Already Entered\nPlease Enter New One").showAndWait();
                            txtUserName.requestFocus();
                        }else{
                            save();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                loadTable();
                btnOK.setDisable(true);
                lblUserName.setVisible(false);
                lblPassword.setVisible(false);
            }else{
                lblValidateEmail.setVisible(true);
                txtMail.requestFocus();
            }
        }else{
            lblValidateContactNumber.setVisible(true);
            txtContactNumber.requestFocus();
        }
    }

    private void update() {



        String id = lblUserID.getText();
        String name = txtName.getText();
        String contact = txtContactNumber.getText();
        String mail = txtMail.getText();
        String userName = txtUserName.getText();
        String password = txtPassword.getText();
        String new_Role = cmbUserRole.getValue().toString();


        Connection connection = DBConnection.getInstance().getConnection();

        String oldLocation = null;
        String oldRole = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select location,user_Role from users where id =?");
            preparedStatement.setObject(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                oldLocation = resultSet.getString(1);
                oldRole = resultSet.getString(2);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String location = "All";
        switch(oldRole){
            case "Hospital IT":
                switch(new_Role){
                    case "Admin":
                        location = "All";

                        try {
                            PreparedStatement preparedStatement = connection.prepareStatement("update hospitals set status = 'Not Reserved' where hospital_Name = ?");
                            preparedStatement.setObject(1,oldLocation);
                            preparedStatement.executeUpdate();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                        break;
                    case "Quarantine Center IT":
                        location = cmbHospitalsAndCenters.getValue().toString();
                        location = location.substring(5,location.length());
                        try {
                            PreparedStatement preparedStatement = connection.prepareStatement("update hospitals set status = 'Not Reserved' where hospital_Name = ?");
                            preparedStatement.setObject(1,oldLocation);
                            preparedStatement.executeUpdate();

                            preparedStatement = connection.prepareStatement("update quarantinecenters set status = 'Reserved' where center_Name = ?");
                            preparedStatement.setObject(1,location);
                            preparedStatement.executeUpdate();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                break;
            case "Quarantine Center IT":
                switch(new_Role){
                    case "Admin":
                        location = "All";

                        try {
                            PreparedStatement preparedStatement = connection.prepareStatement("update quarantinecenters set status = 'Not Reserved' where center_Name = ?");
                            preparedStatement.setObject(1,oldLocation);
                            preparedStatement.executeUpdate();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        break;
                    case "Hospital IT":
                        location = cmbHospitalsAndCenters.getValue().toString();
                        location = location.substring(5,location.length());
                        try {
                            PreparedStatement preparedStatement = connection.prepareStatement("update quarantinecenters set status = 'Not Reserved' where center_Name = ?");
                            preparedStatement.setObject(1,oldLocation);
                            preparedStatement.executeUpdate();

                            preparedStatement = connection.prepareStatement("update hospitals set status = 'Reserved' where hospital_Name = ?");
                            preparedStatement.setObject(1,location);
                            preparedStatement.executeUpdate();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        switch (new_Role){
            case "Hospital IT":
                location = cmbHospitalsAndCenters.getValue().toString();
                location = location.substring(5,location.length());
                cmbHospitalsAndCenters.setVisible(true);
                if(location.equals(oldLocation)){
                    break;
                }else{
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement("update hospitals set status = 'Not Reserved' where hospital_Name = ?");
                        preparedStatement.setObject(1,oldLocation);
                        preparedStatement.executeUpdate();

                        preparedStatement = connection.prepareStatement("update hospitals set status = 'Reserved' where hospital_Name = ?");
                        preparedStatement.setObject(1,location);
                        preparedStatement.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                break;
            case "Quarantine Center IT":
                location = cmbHospitalsAndCenters.getValue().toString();
                location = location.substring(5,location.length());
                cmbHospitalsAndCenters.setVisible(true);
                if(location.equals(oldLocation)){
                    break;
                }else{
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement("update quarantinecenters set status = 'Not Reserved' where center_Name = ?");
                        preparedStatement.setObject(1,oldLocation);
                        preparedStatement.executeUpdate();

                        preparedStatement = connection.prepareStatement("update quarantinecenters set status = 'Reserved' where center_Name = ?");
                        preparedStatement.setObject(1,location);
                        preparedStatement.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                break;
            default:

                break;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update users set name = ?,contact = ?,e_mail = ?,user_Name = ?,password = ?,user_Role = ?,location = ? where id = ?");

            preparedStatement.setObject(1,name);
            preparedStatement.setObject(2,contact);
            preparedStatement.setObject(3,mail);
            preparedStatement.setObject(4,userName);
            preparedStatement.setObject(5,password);
            preparedStatement.setObject(6,new_Role);
            preparedStatement.setObject(7,location);
            preparedStatement.setObject(8,id);

            int i = preparedStatement.executeUpdate();
            if(i != 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Successfully Updated").showAndWait();
                commonClear();
                commonDisable(true);
                btnSave.setText("Save");
                btnAddNewUser.setDisable(false);
                btnAddNewUser.requestFocus();
                cmbHospitalsAndCenters.setVisible(false);
                cmbUserRole.getSelectionModel().select(null);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void save(){
        Connection connection = DBConnection.getInstance().getConnection();

        String id = lblUserID.getText();
        String name = txtName.getText();
        String contact = txtContactNumber.getText();
        String mail = txtMail.getText();
        String userName = txtUserName.getText();
        String password = txtPassword.getText();
        String role = cmbUserRole.getValue().toString();
        String location = "All";
        if(cmbHospitalsAndCenters.isVisible()){
            if(lblLocation.getText().equals("0")){
                cmbHospitalsAndCenters.requestFocus();
                return;
            }
            location = cmbHospitalsAndCenters.getValue().toString();
            location = location.substring(5, location.length());

            if(cmbHospitalsAndCenters.getPromptText().equals("Select Hospital")){
                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("update hospitals set status = 'Reserved' where hospital_Name = '"+location+"'");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }else{
                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("update quarantineCenters set status = 'Reserved' where center_Name = '"+location+"'");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }



        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into users values(?,?,?,?,?,?,?,?)");

            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,name);
            preparedStatement.setObject(3,contact);
            preparedStatement.setObject(4,mail);
            preparedStatement.setObject(5,userName);
            preparedStatement.setObject(6,password);
            preparedStatement.setObject(7,role);
            preparedStatement.setObject(8,location);

            int i = preparedStatement.executeUpdate();

            if(i != 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Successfully Added").showAndWait();
                loadHospitalCombo();
                loadQuarantineCenterCombo();
                commonDisable(true);
                commonClear();
                btnSave.setText("Save");
                cmbUserRole.getSelectionModel().select(null);
                cmbHospitalsAndCenters.setVisible(false);
                btnAddNewUser.requestFocus();
                lblLocation.setText("0");
                lblUserRole.setText("0");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void idAutoGenerate(){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from users order by id desc limit 1");
            if(resultSet.next()){
                String oldID = resultSet.getString(1);
                int newID = Integer.parseInt(oldID.substring(1, 4)) + 1;
                if(newID < 10){
                    lblUserID.setText("U00"+newID);
                }else if(newID < 100){
                    lblUserID.setText("U0"+newID);
                }else{
                    lblUserID.setText("U"+newID);
                }
            }else{
                lblUserID.setText("U001");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void imgPasswordOnMouseClicked(MouseEvent mouseEvent) {
        TextField txt = null;
        if (txtPasswordShow.getUserData() == null) {
            txt = txtPasswordShow;
            txt.setUserData("front");
            imgPassword.setUserData(imgPassword.getImage());
            imgPassword.setImage(new Image(this.getClass().getResource("/images/eyeClose.png").toString()));
            txtPasswordShow.setVisible(true);
            txtPasswordShow.setText(txtPassword.getText());
            txtPassword.setVisible(false);
        } else {
            txt = txtPassword;
            imgPassword.setImage((Image) imgPassword.getUserData());
            txtPasswordShow.setUserData(null);
            txtPassword.setVisible(true);
            txtPassword.setText(txtPasswordShow.getText());
            txtPasswordShow.setVisible(false);
        }
    }

    public String randomNumbers(){
        byte[] randomBytes = new byte[10];
        Random rnd = new Random();
        for (int i = 0; i < randomBytes.length; i++) {
            randomBytes[i] = (byte) (rnd.nextInt(122 - 65) + 65);
        }
        return new String(randomBytes, StandardCharsets.US_ASCII);
    }

    public void txtUserNameOnKeyReleased(KeyEvent keyEvent) {
        lblUserName.setVisible(false);
    }

    public void txtPasswordOnKeyReleased(KeyEvent keyEvent) {
        lblPassword.setVisible(false);
    }

    public void txtPasswordShowOnKeyReleased(KeyEvent keyEvent) {
        lblPassword.setVisible(false);
    }

    public void loadTable(){
        ObservableList<UsersTM> userTable = tblUsers.getItems();
        userTable.clear();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select user_Name,name,user_Role from users");
            while(resultSet.next()){
                String user_Name = resultSet.getString(1);
                String name = resultSet.getString(2);
                String user_Role = resultSet.getString(3);
                JFXButton button = new JFXButton("Delete");
                button.setStyle("-fx-background-color: #ff5050");
                button.setMaxWidth(200);
                userTable.add(new UsersTM(user_Name,name,user_Role,button));
            }
            tblUsers.refresh();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnOKOnAction(ActionEvent actionEvent) {
        commonDisable(true);
        commonClear();
        btnSave.setText("Save");
        btnAddNewUser.setDisable(false);
        btnAddNewUser.requestFocus();
        btnOK.setDisable(true);
        tblUsers.getSelectionModel().clearSelection();
        lblPassword.setVisible(false);
        lblUserName.setVisible(false);
    }

    public void txtSearchOnKeyReleased(KeyEvent keyEvent) {
        String text = txtSearch.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select user_Name,name,user_Role from users where name like '%"+text+"%' or user_Name like '%"+text+"%'");
            ObservableList<UsersTM> items = tblUsers.getItems();
            items.clear();

            while(resultSet.next()){
                String user_Name = resultSet.getString(1);
                String name = resultSet.getString(2);
                String user_Role = resultSet.getString(3);
                JFXButton button = new JFXButton("Delete");
                button.setStyle("-fx-background-color: #ff5050");
                button.setMaxWidth(200);
                items.add(new UsersTM(user_Name,name,user_Role,button));
            }

            tblUsers.refresh();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void txtNameOnAction(ActionEvent actionEvent) {
        txtContactNumber.requestFocus();
    }

    public void txtContactNumberOnAction(ActionEvent actionEvent) {
        txtMail.requestFocus();
    }

    public void txtMailOnAction(ActionEvent actionEvent) {
        txtUserName.requestFocus();
    }

    public void txtUserNameOnAction(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void txtUserNameOnMouseClicked(MouseEvent mouseEvent) {
        txtUserName.selectAll();
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
        cmbUserRole.requestFocus();
    }

    public void txtPasswordOnMouseClicked(MouseEvent mouseEvent) {
        txtPassword.selectAll();

    }
}
