/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.models;

import java.util.UUID;

/**
 *
 * @author Kanch
 */
public class Feedback {
    private final UUID reportID;
    private final String text;
    
    public Feedback(UUID reportID, String text) {
        this.reportID = reportID;
        this.text = text;
    }
    
    public UUID getReportID() { 
        return reportID; 
    }
    
    public String getText() { 
        return text;     
    }
}
