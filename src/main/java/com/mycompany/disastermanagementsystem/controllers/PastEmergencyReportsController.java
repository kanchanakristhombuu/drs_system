/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.controllers;

import com.mycompany.disastermanagementsystem.daos.FeedbackDao;
import com.mycompany.disastermanagementsystem.daos.ReportDao;
import com.mycompany.disastermanagementsystem.models.Feedback;
import com.mycompany.disastermanagementsystem.models.Report;
import com.mycompany.disastermanagementsystem.models.Session;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author Kanch
 */
public class PastEmergencyReportsController extends MainController implements Initializable{
    
    @FXML 
    private TableView<Report> pastEmergenciesTable;
    @FXML 
    private TableColumn<Report, UUID>   ID;
    @FXML 
    private TableColumn<Report, String> date;               
    @FXML 
    private TableColumn<Report, String> emergencyType;
    @FXML 
    private TableColumn<Report, Integer> severity;
    @FXML 
    private TableColumn<Report, Void> feedbackBtnField;
    @FXML 
    private TableColumn<Report, String>  userFeedback;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        super.initialize(location, resources);
        
        String role = Session.getCurrentUser().getRole();
      
        
        ID.setCellValueFactory(c ->
            new ReadOnlyObjectWrapper<>(c.getValue().getReportID()));
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date.setCellValueFactory(c -> 
            new ReadOnlyStringWrapper(c.getValue().getDate().format(fmt)));
        
        emergencyType.setCellValueFactory(c ->
            new ReadOnlyStringWrapper(c.getValue().getEmergencyType()));
        severity.setCellValueFactory(c ->
            new ReadOnlyObjectWrapper<>(c.getValue().getSeverity()));

        // Give Feedback button—only for Users
        showForRoles(feedbackBtnField, "User");
        feedbackBtnField.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Report, Void> call(TableColumn<Report, Void> tc) {
                return new TableCell<>() {
                    private final Button btn = new Button("Give Feedback");
                    {
                        btn.setOnAction(e -> {
                            Report rpt = getTableView()
                                .getItems().get(getIndex());
                            openFeedbackDialog(rpt);
                        });
                    }
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        });
        
        showForRoles(userFeedback, "Admin");
        userFeedback.setCellValueFactory(c -> {
            Feedback fb = FeedbackDao.INSTANCE.get(c.getValue().getReportID());
            return new ReadOnlyStringWrapper(fb != null ? fb.getText() : "");
        });
        
        ObservableList<Report> complete = FXCollections.observableArrayList();
        for (Report r : ReportDao.INSTANCE.getReportsList()) {
            if ("Complete".equals(r.getStatus())) complete.add(r);
        }
        pastEmergenciesTable.setItems(complete);
    }
    
    private void openFeedbackDialog(Report report) {
        // 1) Build dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Feedback for Report " + report.getReportID());
        dialog.setHeaderText("Please enter your feedback:");

        ButtonType submitType = new ButtonType("Submit", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitType, ButtonType.CANCEL);

        TextArea feedbackArea = new TextArea();
        feedbackArea.setPromptText("Enter feedback here...");
        feedbackArea.setWrapText(true);
        feedbackArea.setPrefRowCount(5);

        VBox content = new VBox(10, new Label("Feedback:"), feedbackArea);
        dialog.getDialogPane().setContent(content);

        // 2) Disable Submit until non-empty
        Node submitBtn = dialog.getDialogPane().lookupButton(submitType);
        submitBtn.setDisable(true);
        feedbackArea.textProperty().addListener((obs, oldV, newV) ->
            submitBtn.setDisable(newV.trim().isEmpty())
        );

        // 3) Convert result to String
        dialog.setResultConverter(btn -> {
            if (btn == submitType) {
                return feedbackArea.getText().trim();
            }
            return null;
        });

        // 4) Show and save
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(text -> {
            FeedbackDao.INSTANCE.save(new Feedback(report.getReportID(), text));
            pastEmergenciesTable.refresh();
        });
    }
}
