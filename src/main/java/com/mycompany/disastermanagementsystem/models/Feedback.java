/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author Kanch
 */
public class Feedback {

    private final int id;
    private final UUID reportID;
    private final String userEmail;
    private final String feedbackText;
    private final LocalDateTime dateGiven;

    public Feedback(int id,
            UUID reportID,
            String userEmail,
            String feedbackText,
            LocalDateTime dateGiven) {
        this.id = id;
        this.reportID = reportID;
        this.userEmail = userEmail;
        this.feedbackText = feedbackText;
        this.dateGiven = dateGiven;
    }

    public Feedback(UUID reportID, String userEmail, String feedbackText) {
        this(0, reportID, userEmail, feedbackText, null);
    }

    public int getId() {
        return id;
    }

    public UUID getReportID() {
        return reportID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public LocalDateTime getDateGiven() {
        return dateGiven;
    }

}
