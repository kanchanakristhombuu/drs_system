/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

/**
 *
 * @author Kanch
 */
public class GiveFeedbackController extends MainController{
    
    @FXML
    private Slider feedbackRatingSlider;
    
    @FXML
    private TextArea experienceTextarea;
    
    @FXML
    public void feebackSubmitHandler(ActionEvent e) {
        System.out.println("Submit feedback clicked");
    }
}
