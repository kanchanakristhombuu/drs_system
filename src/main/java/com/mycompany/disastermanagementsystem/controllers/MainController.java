/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.controllers;

import com.mycompany.disastermanagementsystem.App;
import com.mycompany.disastermanagementsystem.models.Session;
import com.mycompany.disastermanagementsystem.models.User;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

/**
 *
 * @author Kanch
 */
public class MainController implements Initializable{
    
    @FXML 
    private Button dashboardBtn;
    @FXML
    private Button reportEmergencyBtn;
    @FXML 
    private Button pastReportsBtn;
    @FXML 
    private Button viewEmergencyReportsBtn;
    @FXML 
    private Button viewAssignedEmergenciesBtn;
    
    private List<Button> navButtons;

    
    protected void showForRoles(Node node, String... roles) {
        if (node == null) return;
        User u = Session.getCurrentUser();
        boolean visible = u != null && Arrays.asList(roles).contains(u.getRole());
        node.setVisible(visible);
    }

    /** Same as above, but for TableColumn objects. */
    protected void showForRoles(TableColumn<?,?> col, String... roles) {
        if (col == null) return;
        User u = Session.getCurrentUser();
        boolean visible = u != null && Arrays.asList(roles).contains(u.getRole());
        col.setVisible(visible);
    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navButtons = Arrays.asList(
          dashboardBtn,
          reportEmergencyBtn,
          pastReportsBtn,
          viewEmergencyReportsBtn,
          viewAssignedEmergenciesBtn
        );
        
        showForRoles(dashboardBtn, "User");
        showForRoles(reportEmergencyBtn, "User");
        showForRoles(pastReportsBtn, "User","Admin");
        showForRoles(viewEmergencyReportsBtn, "Admin");
        showForRoles(viewAssignedEmergenciesBtn,"Fire","Flood","Earthquake","Hurricane","Dangerous Animal");
    }
    
    private void loadAndHighlight(String fxmlPath, String btnFxId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            App.getScene().setRoot(root);

            // remove focus from any nav item
            root.requestFocus();

            // then highlight the correct button…
            Object ctrl = loader.getController();
            if (ctrl instanceof MainController) {
                Button newBtn = (Button) root.lookup("#" + btnFxId);
                ((MainController)ctrl).highlightNav(newBtn);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void highlightNav(Button active) {
        for (Button b : navButtons) {
            b.setStyle("");
        }
        if (active != null) {
            active.setStyle(
              "-fx-border-color: #2196F3;" +
              "-fx-border-width: 0 0 0 3;"
            );
        }
    }
    
    
    @FXML
    public void onDashboardHandler(ActionEvent e) {
        loadAndHighlight(
          "/com/mycompany/disastermanagementsystem/home.fxml",
          "dashboardBtn"
        );
    }
    
    @FXML
    public void onReportEmergencyHandler(ActionEvent e) {
        loadAndHighlight(
          "/com/mycompany/disastermanagementsystem/report_emergency.fxml",
          "reportEmergencyBtn"
        );
    }
    
    @FXML
    public void onPastEmergencyReportHandler(ActionEvent e) {
        loadAndHighlight(
          "/com/mycompany/disastermanagementsystem/past_emergency_reports.fxml",
          "pastReportsBtn"
        );
    }
    
    @FXML 
    public void viewEmergencyReportsHandler(ActionEvent e) {
        loadAndHighlight(
          "/com/mycompany/disastermanagementsystem/current_emergency_reports.fxml",
          "viewEmergencyReportsBtn"
        );
    }
    
    @FXML 
    public void viewAssignedEmergenciesHandler(ActionEvent e) {
        loadAndHighlight(
          "/com/mycompany/disastermanagementsystem/authority_emergency_list.fxml",
          "viewAssignedEmergenciesBtn"
        );
    }
    
    @FXML 
    public void onLogoutHandler(ActionEvent e) {
        Session.setCurrentUser(null);
        try {
            Parent loginRoot = FXMLLoader.load(
              getClass().getResource(
                "/com/mycompany/disastermanagementsystem/login.fxml"
              )
            );
            App.getScene().setRoot(loginRoot);
            // no nav highlight on login
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
