/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.daos;

import com.mycompany.disastermanagementsystem.models.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Kanch
 */
public class ReportDao {
    public static final ReportDao INSTANCE = new ReportDao();
    
    private final ObservableList<Report> reports = FXCollections.observableArrayList();
    
    private ReportDao() {};
    
    public void save(Report report) {
        reports.add(report);
    }
    
    public ObservableList<Report> getReportsList() {
        return reports;
    }
}
