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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
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

        contactAuthority.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Report, Void> call(TableColumn<Report, Void> col) {
                return new TableCell<>() {
                    private final Button btn = new Button("Contact");

                    {
                        btn.setOnAction(e -> {
                            Report report = getTableView().getItems().get(getIndex());
                            showContactDialog(report);
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

        completeBtnCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Report, Void> call(TableColumn<Report, Void> col) {
                return new TableCell<>() {
                    private final Button btn = new Button("Complete");

                    {
                        btn.setOnAction(e -> {
                            Report rpt = getTableView()
                                    .getItems()
                                    .get(getIndex());
                            rpt.setStatus("Complete");
                            // refresh the filter to remove this row
                            activeReports.setPredicate(r
                                    -> !"Complete".equals(r.getStatus())
                            );
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Report rpt = getTableView()
                                    .getItems()
                                    .get(getIndex());
                            // disable button if already complete
                            btn.setDisable("Complete".equals(rpt.getStatus()));
                            setGraphic(btn);
                        }
                    }
                };
            }
        });

    }

    private void showContactDialog(Report report) {
        // find the right authority
        Authority auth = authorityDao.findByType(report.getEmergencyType());
        if (auth == null) {
            new Alert(Alert.AlertType.ERROR, "No authority found for “"
                    + report.getEmergencyType() + "”").show();
            return;
        }

        // build a simple GridPane for details
        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);
        grid.addRow(0, new Label("Name:"), new Label(auth.getName()));
        grid.addRow(1, new Label("Phone:"), new Label(auth.getPhone()));
        grid.addRow(2, new Label("Email:"), new Label(auth.getEmail()));

        // custom dialog
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Contact Authority");
        dlg.getDialogPane().setContent(grid);
        dlg.getDialogPane().setPrefSize(400, 250);   // width=400px, height=250px
        dlg.setResizable(true);
        dlg.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK, new ButtonType("Contact", ButtonBar.ButtonData.APPLY)
        );

        dlg.showAndWait().ifPresent(bt -> {
            if (bt.getButtonData() == ButtonBar.ButtonData.APPLY) {
                // send report to department
                EmergencyDepartmentDao.INSTANCE.sendToAuthority(report, auth);
                new Alert(Alert.AlertType.INFORMATION,
                        "Report sent to " + auth.getName()
                ).showAndWait();
            }
        });
    }
}
