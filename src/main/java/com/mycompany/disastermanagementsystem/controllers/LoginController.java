/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.controllers;

import com.mycompany.disastermanagementsystem.App;
import com.mycompany.disastermanagementsystem.daos.UserDao;
import com.mycompany.disastermanagementsystem.models.Session;
import com.mycompany.disastermanagementsystem.models.User;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Kanch
 */
public class LoginController {
    
    @FXML
    private TextField emailTextField;
    
    @FXML
    private TextField passwordTextField;
    
    @FXML
    public void loginBtnHandler(ActionEvent e) {
        System.out.println("Login button clicked");
        
        String email = emailTextField.getText().trim();
        String password   = passwordTextField.getText();
        
        User user = UserDao.INSTANCE.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            new Alert(Alert.AlertType.ERROR, "Invalid email or password").showAndWait();
            return;
        }
        
        Session.setCurrentUser(user);

        // 2) choose which FXML to load & which button to highlight
        String fxml, btnId;
        switch (user.getRole()) {
            case "User":
                fxml  = "/com/mycompany/disastermanagementsystem/home.fxml";
                btnId = "dashboardBtn";
                break;
            case "Admin":
                fxml  = "/com/mycompany/disastermanagementsystem/current_emergency_reports.fxml";
                btnId = "viewEmergencyReportsBtn";
                break;
            default:
                // service accounts
                fxml  = "/com/mycompany/disastermanagementsystem/authority_emergency_list.fxml";
                btnId = "viewAssignedEmergenciesBtn";
                break;
        }
        
        try {
            // 3) load with a non-static loader
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            // 4) swap into the existing scene
            App.getScene().setRoot(root);

            // 5) highlight the right nav button in the new controller
            Object controller = loader.getController();
            if (controller instanceof MainController) {
                MainController mc = (MainController) controller;
                // lookup the fresh button by fx:id and highlight it
                Button btn = (Button) root.lookup("#" + btnId);
                mc.highlightNav(btn);
            }

            // 6) shift focus away so no stray focus ring
            root.requestFocus();

        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load initial page").showAndWait();
        }
    }
}
