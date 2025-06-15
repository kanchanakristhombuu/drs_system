/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.controllers;

import com.mycompany.disastermanagementsystem.daos.AuthorityDao;
import com.mycompany.disastermanagementsystem.daos.EmergencyDepartmentDao;
import com.mycompany.disastermanagementsystem.daos.EmergencyDao;
import com.mycompany.disastermanagementsystem.models.Authority;
import com.mycompany.disastermanagementsystem.models.Report;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 *
 * @author Kanch
 */
public class ViewEmergencyReportsController extends MainController implements Initializable {

    private final AuthorityDao authorityDao = AuthorityDao.AUTHORITY_INSTANCE;

    @FXML
    private TableView<Report> viewReportsTable;
    @FXML
    private TableColumn<Report, java.util.UUID> ID;
    @FXML
    private TableColumn<Report, String> emergencyType;
    @FXML
    private TableColumn<Report, Integer> severity;
    @FXML
    private TableColumn<Report, String> contactNumber;
    @FXML
    private TableColumn<Report, String> address;
    @FXML
    private TableColumn<Report, String> status;
    @FXML
    private TableColumn<Report, Void> contactAuthority;
    @FXML
    private TableColumn<Report, Void> completeBtnCol;

    private FilteredList<Report> activeReports;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {

        super.initialize(url, bundle);

        ID.setCellValueFactory(c
                -> new ReadOnlyObjectWrapper<>(c.getValue().getReportID()));
        emergencyType.setCellValueFactory(c
                -> new ReadOnlyStringWrapper(c.getValue().getEmergencyType()));
        severity.setCellValueFactory(c
                -> new ReadOnlyObjectWrapper<>(c.getValue().getSeverity()));
        contactNumber.setCellValueFactory(c
                -> new ReadOnlyStringWrapper(c.getValue().getContactNumber()));
        address.setCellValueFactory(c
                -> new ReadOnlyStringWrapper(c.getValue().getAddress()));
        status.setCellValueFactory(c
                -> new ReadOnlyStringWrapper(c.getValue().getStatus()));

        ObservableList<Report> master = FXCollections.observableArrayList(EmergencyDao.INSTANCE.getAll());

        activeReports = new FilteredList<>(master,
                rpt -> !"Complete".equals(rpt.getStatus())
        );

        viewReportsTable.setItems(activeReports);

        contactAuthority.setCellFactory(col -> new TableCell<Report, Void>() {
            private final Button btn = new Button("Contact");

            {
                btn.setOnAction(e -> {
                    Report report = getTableView().getItems().get(getIndex());
                    showContactDialog(report);

                    report.setContacted(true);
                    getTableView().refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    Report report = getTableView().getItems().get(getIndex());
                    btn.setDisable(report.isContacted());
                    setGraphic(btn);
                }
            }
        });

        completeBtnCol.setCellFactory(col -> new TableCell<Report, Void>() {
            private final Button btn = new Button("Complete");

            {
                btn.setOnAction(e -> {
                    Report rpt = getTableView().getItems().get(getIndex());
                    UUID id = rpt.getReportID();

                    EmergencyDao.INSTANCE.updateStatus(id, "Complete");
                    EmergencyDepartmentDao.INSTANCE.updateAssignmentStatus(id, "Complete");

                    rpt.setStatus("Complete");

                    activeReports.setPredicate(r
                            -> !"Complete".equals(r.getStatus())
                    );

                    viewReportsTable.refresh();

                    getTableView().getItems().remove(rpt);

                    new Alert(
                            Alert.AlertType.INFORMATION,
                            "Report " + id + " marked Complete."
                    ).showAndWait();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Report rpt = getTableView().getItems().get(getIndex());
                    // disable if already Complete
                    btn.setDisable("Complete".equals(rpt.getStatus()));
                    setGraphic(btn);
                }
            }
        });

    }

    private void showContactDialog(Report report) {
        Dialog<Authority> dlg = new Dialog<>();
        dlg.setTitle("Contact Authorities");

        ButtonType sendButton = new ButtonType("Send", ButtonBar.ButtonData.APPLY);
        ButtonType cancelButton = ButtonType.CANCEL;

        dlg.getDialogPane()
                .getButtonTypes()
                .setAll(sendButton, cancelButton);

        ChoiceBox<Authority> choice = new ChoiceBox<>();
        choice.getItems().setAll(
                AuthorityDao.INSTANCE.findByType(report.getEmergencyType())
        );
        choice.getSelectionModel().selectFirst();
        dlg.getDialogPane().setContent(choice);

        Node sendBtn = dlg.getDialogPane().lookupButton(sendButton);
        sendBtn.disableProperty().bind(
                choice.getSelectionModel()
                        .selectedItemProperty()
                        .isNull()
        );

        dlg.setResultConverter(btn -> {
            if (btn == sendButton) {
                return choice.getValue();
            }
            return null;
        });

        dlg.showAndWait().ifPresent(auth -> {
            EmergencyDepartmentDao.INSTANCE.sendToAuthority(report, auth);
            EmergencyDao.INSTANCE.updateStatus(report.getReportID(), "Assigned");
            report.setStatus("Assigned");
            viewReportsTable.refresh();
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Report sent to " + auth.getName()
            ).showAndWait();
        });
    }
}
