/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.controllers;

import com.mycompany.disastermanagementsystem.daos.ReportDao;
import com.mycompany.disastermanagementsystem.models.Report;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 *
 * @author Kanch
 */
public class HomeController extends MainController implements Initializable{
    
    @FXML 
    private TextArea currentEmergencyTextarea;
    
    @FXML
    private TextArea safetyGuidelinesTextarea;
    
    private static final Map<String,String> GUIDELINES = Map.of(
      "Fire",            "• Stay low to avoid smoke.\n• Evacuate immediately.\n• Call 000 once safe.",
      "Earthquake",      "• Drop, cover, and hold on.\n• Stay clear of windows.\n• When safe, move to open space.",
      "Flood",           "• Move to higher ground.\n• Never walk/drive through floodwater.\n• Follow SES instructions.",
      "Hurricane",       "• Secure loose objects outside.\n• Stay indoors away from windows.\n• Keep emergency kit ready.",
      "Dangerous Animal","• Keep a safe distance.\n• Do not corner or provoke.\n• Contact Wildlife Rescue Service."
    );
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        Optional<Report> maybeCurrent = ReportDao.INSTANCE.getReportsList().stream()
            .filter(r -> !"Complete".equals(r.getStatus()))
            .findFirst();

        if (maybeCurrent.isEmpty()) {
            currentEmergencyTextarea.setText("No current emergency");
            safetyGuidelinesTextarea.clear();
            return;
        }

        Report cur = maybeCurrent.get();
        StringBuilder sb = new StringBuilder();
        sb.append("Status  : ").append(cur.getStatus()).append("\n")
          .append("Type    : ").append(cur.getEmergencyType()).append("\n")
          .append("Severity: ").append(cur.getSeverity());
        currentEmergencyTextarea.setText(sb.toString());

        String guide = GUIDELINES.getOrDefault(
          cur.getEmergencyType(),
          "No guidelines available for this emergency type."
        );
        safetyGuidelinesTextarea.setText(guide);
    }
}
