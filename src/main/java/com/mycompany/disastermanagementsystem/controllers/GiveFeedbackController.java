/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.controllers;

import com.mycompany.disastermanagementsystem.daos.FeedbackDao;
import com.mycompany.disastermanagementsystem.models.Feedback;
import com.mycompany.disastermanagementsystem.models.Report;
import com.mycompany.disastermanagementsystem.models.Session;
import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author Kanch
 */
public class GiveFeedbackController extends MainController {

    @FXML
    private TextArea feedbackTextarea;
    private Report report;

    public void setReport(Report report) {
        this.report = report;
    }

    @FXML
    public void onSubmit(ActionEvent e) {
        String text = feedbackTextarea.getText().trim();
        if (text.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Feedback cannot be empty.")
                    .showAndWait();
            return;
        }

        Feedback fb = new Feedback(
                report.getReportID(),
                Session.getCurrentUser().getEmail(),
                text
        );
        FeedbackDao.INSTANCE.save(fb);

        new Alert(Alert.AlertType.INFORMATION,
                "Thank you for your feedback!"
        ).showAndWait();

        ((Stage) feedbackTextarea.getScene().getWindow()).close();
    }

    @FXML
    public void onCancel(ActionEvent e) {
        ((Stage) feedbackTextarea.getScene().getWindow()).close();
    }

    public static void showForReport(Window owner, Report rpt) {
        try {

            URL fxmlUrl = GiveFeedbackController.class.getResource(
                    "/com/mycompany/disastermanagementsystem/give_feedback.fxml"
            );

            FXMLLoader loader = new FXMLLoader(fxmlUrl);

            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(owner);
            dialog.setScene(new Scene(root));

            GiveFeedbackController ctrl = loader.getController();
            ctrl.setReport(rpt);

            dialog.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Unable to open feedback form."
            ).showAndWait();
        }
    }
}
