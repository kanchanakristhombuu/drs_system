/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.controllers;

import com.mycompany.disastermanagementsystem.daos.EmergencyDao;
import com.mycompany.disastermanagementsystem.daos.EmergencyDepartmentDao;
import com.mycompany.disastermanagementsystem.models.Report;
import com.mycompany.disastermanagementsystem.models.Session;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 *
 * @author Kanch
 */
public class ViewAssignedEmergenciesController extends MainController implements Initializable {

    @FXML
    private TableView<Report> assignedEmergenciesTable;
    @FXML
    private TableColumn<Report, UUID> ID;
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
    private TableColumn<Report, Void> inProgressBtnField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        super.initialize(url, rb);

        String role = Session.getCurrentUser().getRole();

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
                -> c.getValue().statusProperty());

        ObservableList<Report> allAssigned
                = EmergencyDepartmentDao.INSTANCE.getAssignedReportsList();
        ObservableList<Report> filtered
                = allAssigned.filtered(r -> r.getEmergencyType().equals(role));

        assignedEmergenciesTable.setItems(filtered);

        inProgressBtnField.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Report, Void> call(TableColumn<Report, Void> col) {
                return new TableCell<>() {
                    private final Button btn = new Button("In Progress");

                    {
                        btn.setOnAction(evt -> {
                            Report rpt = getTableView()
                                    .getItems()
                                    .get(getIndex());
                            UUID id = rpt.getReportID();

                            EmergencyDao.INSTANCE.updateStatus(id, "In Progress");

                            EmergencyDepartmentDao.INSTANCE
                                    .updateAssignmentStatus(id, "In Progress");

                            rpt.setStatus("In Progress");
                            getTableView().refresh();
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // disable if already in-progress or complete
                            Report rpt = getTableView()
                                    .getItems()
                                    .get(getIndex());
                            btn.setDisable(
                                    !"Assigned".equals(rpt.getStatus())
                            );
                            setGraphic(btn);
                        }
                    }
                };
            }
        });

    }
}
