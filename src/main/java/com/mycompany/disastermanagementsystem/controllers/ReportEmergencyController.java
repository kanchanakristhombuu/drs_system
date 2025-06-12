/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.controllers;

import com.mycompany.disastermanagementsystem.daos.EmergencyDao;
import com.mycompany.disastermanagementsystem.models.Report;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Kanch
 */
public class ReportEmergencyController extends MainController implements Initializable{
    
    @FXML
    private ChoiceBox<String>  emergencyTypeSelect;
    
    @FXML
    private Slider emergencyLevelSlider;
    
    @FXML
    private TextField contactNumberTextField;
    
    @FXML
    private TextArea addressTextarea;
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        super.initialize(url, bundle);
        
        emergencyTypeSelect.getItems().addAll("Fire", "Earthquake", "Flood", "Hurricane", "Dangerous Animal");
        emergencyTypeSelect.setValue("Fire");
    }
    
    @FXML
    public void onReportSubmit(ActionEvent e) {
        System.out.println("Submit button clicked");

        String emergencyType = emergencyTypeSelect.getValue();
        int severity = (int) emergencyLevelSlider.getValue();
        String contactNumber = contactNumberTextField.getText();
        String address = addressTextarea.getText();

        Report report = new Report(emergencyType, severity, contactNumber, address);
        EmergencyDao.INSTANCE.save(report);

        Alert success = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Report submitted (ID: " + report.getReportID() + ")"
        );
        success.showAndWait();

         // Clear the form or navigate away
        emergencyTypeSelect.setValue("Fire");
        emergencyLevelSlider.setValue(1);
        contactNumberTextField.clear();
        addressTextarea.clear();
    }

            
    
}
