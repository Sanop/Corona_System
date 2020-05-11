package util;

import com.jfoenix.controls.JFXButton;

public class UsersTM {
    private String userName;
    private String name;
    private String role;
    private JFXButton button;

    public UsersTM(String userName, String name, String role, JFXButton button) {
        this.userName = userName;
        this.name = name;
        this.role = role;
        this.button = button;
    }

    public UsersTM() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public JFXButton getButton() {
        return button;
    }

    public void setButton(JFXButton button) {
        this.button = button;
    }

    @Override
    public String toString() {
        return "UsersTM{" +
                "userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", button=" + button +
                '}';
    }


}
