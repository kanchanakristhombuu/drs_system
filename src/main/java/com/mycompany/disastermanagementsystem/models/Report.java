/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.models;

import java.time.LocalDate;
import java.util.UUID;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kanch
 */
public class Report {

    private final UUID reportID;
    private String emergencyType;
    private int severity;
    private String contactNumber;
    private String address;

    private final StringProperty status = new SimpleStringProperty(this, "status", "Active");
    private final StringProperty reporterEmail = new SimpleStringProperty(this, "reporterEmail", "");

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(this, "date", LocalDate.now());

    public Report(String emergencyType, int severity, String contactNumber, String address) {
        reportID = UUID.randomUUID();
        this.emergencyType = emergencyType;
        this.severity = severity;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    public Report(UUID id, String emergencyType, int severity, String contactNumber, String address) {
        this.reportID = id;
        this.emergencyType = emergencyType;
        this.severity = severity;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    public UUID getReportID() {
        return reportID;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String s) {
        status.set(s);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public String getReporterEmail() {
        return reporterEmail.get();
    }

    public void setReporterEmail(String email) {
        reporterEmail.set(email);
    }

    public StringProperty reporterEmailProperty() {
        return reporterEmail;
    }
}
