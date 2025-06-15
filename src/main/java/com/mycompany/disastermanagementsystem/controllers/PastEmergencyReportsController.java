/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.controllers;

import com.mycompany.disastermanagementsystem.daos.FeedbackDao;
import com.mycompany.disastermanagementsystem.daos.EmergencyDao;
import com.mycompany.disastermanagementsystem.models.Feedback;
import com.mycompany.disastermanagementsystem.models.Report;
import com.mycompany.disastermanagementsystem.models.Session;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

/**
 *
 * @author Kanch
 */
public class PastEmergencyReportsController extends MainController implements Initializable {

    private final FeedbackDao feedbackDao = FeedbackDao.INSTANCE;

    @FXML
    private TableView<Report> pastEmergenciesTable;
    @FXML
    private TableColumn<Report, UUID> ID;
    @FXML
    private TableColumn<Report, String> date;
    @FXML
    private TableColumn<Report, String> emergencyType;
    @FXML
    private TableColumn<Report, Integer> severity;
    @FXML
    private TableColumn<Report, Void> feedbackBtnField;
    @FXML
    private TableColumn<Report, String> userFeedback;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private HBox reportControls;

    @FXML
    private void onGenerateReport(ActionEvent event) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert("Please select both start and end dates.");
            return;
        }

        List<Report> filteredReports = pastEmergenciesTable.getItems().stream()
                .filter(r -> {
                    LocalDate reportDate = r.getDate();
                    return (reportDate.isEqual(startDate) || reportDate.isAfter(startDate))
                            && (reportDate.isEqual(endDate) || reportDate.isBefore(endDate));
                })
                .collect(Collectors.toList());

        if (filteredReports.isEmpty()) {
            showAlert("No reports found in the selected date range.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("EmergencyReport.pdf");

        File file = fileChooser.showSaveDialog(startDatePicker.getScene().getWindow());
        if (file != null) {
            try {
                generatePdfReport(file, filteredReports);
                showAlert("Report generated successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Failed to generate report: " + e.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        super.initialize(location, resources);

        if (!Session.getCurrentUser().getRole().equalsIgnoreCase("admin")) {
            reportControls.setVisible(false);
            reportControls.setManaged(false); // prevents layout gaps
        }

        ID.setCellValueFactory(c
                -> new ReadOnlyObjectWrapper<>(c.getValue().getReportID()));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date.setCellValueFactory(c
                -> new ReadOnlyStringWrapper(c.getValue().getDate().format(fmt)));

        emergencyType.setCellValueFactory(c
                -> new ReadOnlyStringWrapper(c.getValue().getEmergencyType()));
        severity.setCellValueFactory(c
                -> new ReadOnlyObjectWrapper<>(c.getValue().getSeverity()));

        if (!Session.getCurrentUser().getRole().equalsIgnoreCase("admin")) {
            reportControls.setVisible(false);
            reportControls.setManaged(false);
        }

        // Give Feedback button—only for Users
        showForRoles(feedbackBtnField, "User");

        feedbackBtnField.setCellFactory(col -> new TableCell<Report, Void>() {
            private final Button btn = new Button("Give Feedback");

            {
                btn.setOnAction(e -> {
                    Report rpt = getTableView().getItems().get(getIndex());
                    GiveFeedbackController.showForReport(
                            getTableView().getScene().getWindow(),
                            rpt
                    );
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                Report rpt = getTableView().getItems().get(getIndex());
                UUID reportId = rpt.getReportID();
                String userEmail = Session.getCurrentUser().getEmail();

                // Check if feedback already exists for this report from the current user
                List<Feedback> feedbacks = FeedbackDao.INSTANCE.findByReport(reportId);
                boolean hasFeedback = feedbacks.stream()
                        .anyMatch(fb -> fb.getUserEmail().equalsIgnoreCase(userEmail));

                btn.setDisable(hasFeedback);
                setGraphic(btn);
            }
        });

        userFeedback.setCellValueFactory(cellData -> {
            UUID reportId = cellData.getValue().getReportID();

            ObservableList<Feedback> feedbacks = FeedbackDao.INSTANCE.findByReport(reportId);

            String text = feedbacks.isEmpty() ? "" : feedbacks.get(0).getFeedbackText();

            return new ReadOnlyStringWrapper(text);
        });

        ObservableList<Report> complete = FXCollections.observableArrayList();
        for (Report r : EmergencyDao.INSTANCE.getAll()) {
            if ("Complete".equals(r.getStatus())) {
                complete.add(r);
            }
        }
        pastEmergenciesTable.setItems(complete);
    }

    private void openFeedbackDialog(Report report) {

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

        Node submitBtn = dialog.getDialogPane().lookupButton(submitType);
        submitBtn.setDisable(true);
        feedbackArea.textProperty().addListener((obs, oldV, newV)
                -> submitBtn.setDisable(newV.trim().isEmpty())
        );

        dialog.setResultConverter(btn -> {
            if (btn == submitType) {
                return feedbackArea.getText().trim();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(text -> {
            FeedbackDao.INSTANCE.save(new Feedback(report.getReportID(), Session.getCurrentUser().getEmail(), text));
            pastEmergenciesTable.refresh();
        });
    }

    private void generatePdfReport(File file, List<Report> reports) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        document.add(new Paragraph("Emergency Report"));
        document.add(new Paragraph("Generated on: " + LocalDateTime.now()));
        document.add(new Paragraph(" "));

        for (Report report : reports) {
            document.add(new Paragraph("ID: " + report.getReportID()));
            document.add(new Paragraph("Date: " + report.getDate()));
            document.add(new Paragraph("Type: " + report.getEmergencyType()));
            document.add(new Paragraph("Severity: " + report.getSeverity()));

            List<Feedback> feedbackList = feedbackDao.findByReport(report.getReportID());
            if (!feedbackList.isEmpty()) {
                for (Feedback fb : feedbackList) {
                    document.add(new Paragraph("Feedback by " + fb.getUserEmail() + " on " + fb.getDateGiven() + ":"));
                    document.add(new Paragraph(fb.getFeedbackText()));
                    document.add(new Paragraph(" "));
                }
            } else {
                document.add(new Paragraph("Feedback: No feedback available"));
            }
            document.add(new Paragraph("-------------------------------"));
        }

        document.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
