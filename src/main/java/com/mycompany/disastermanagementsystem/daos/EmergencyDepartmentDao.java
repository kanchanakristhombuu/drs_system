/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.daos;

import com.mycompany.disastermanagementsystem.models.Authority;
import com.mycompany.disastermanagementsystem.models.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Kanch
 */
public class EmergencyDepartmentDao {
    public static final EmergencyDepartmentDao INSTANCE = new EmergencyDepartmentDao();
    private final ObservableList<Report> assignedReports  = FXCollections.observableArrayList();

    private EmergencyDepartmentDao(){}

    public void sendToAuthority(Report r, Authority a) {
        assignedReports.add(r);
    }

    public ObservableList<Report> getAssignedReportsList() {
        return assignedReports;
    }
}
