/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.daos;

import com.mycompany.disastermanagementsystem.db.DBConnection;
import com.mycompany.disastermanagementsystem.models.Authority;
import com.mycompany.disastermanagementsystem.models.Session;
import com.mycompany.disastermanagementsystem.models.Report;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Kanch
 */
public class EmergencyDepartmentDao {

    public static final EmergencyDepartmentDao INSTANCE = new EmergencyDepartmentDao();

    private static final String INSERT_SQL
            = "INSERT INTO assignments (report_id, service_role, authority_name, status) "
            + "VALUES (?, ?, ?, ?)";

    private EmergencyDepartmentDao() {
    }

    public void sendToAuthority(Report r, Authority auth) {
        try (Connection cx = DBConnection.getConnection(); PreparedStatement ps = cx.prepareStatement(INSERT_SQL)) {

            ps.setString(1, r.getReportID().toString());
            ps.setString(2, r.getEmergencyType());
            ps.setString(3, auth.getName());
            ps.setString(4, "Assigned");

            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void sendToAuthority(Report report) {
        Authority defaultAuth = new Authority(
                report.getEmergencyType(),
                report.getEmergencyType() + " Dept",
                "000-000-0000",
                "N/A"
        );
        sendToAuthority(report, defaultAuth);
    }

    public ObservableList<Report> getAssignedReportsList() {
        List<Report> list = new ArrayList<>();
        String sql
                = "SELECT e.id, "
                + "       e.emergency_type, "
                + "       e.severity, "
                + "       e.contact_number, "
                + "       e.address, "
                + "       e.status, "
                + "       e.reporter_email, "
                + "       e.date_reported "
                + "  FROM emergency e "
                + "  JOIN assignments a ON e.id = a.report_id "
                + " WHERE a.service_role = ? "
                + "   AND a.status = 'Assigned'";

        String myRole = Session.getCurrentUser().getRole();
        try (Connection cx = DBConnection.getConnection(); PreparedStatement ps = cx.prepareStatement(sql)) {

            ps.setString(1, myRole);
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    Report r = new Report(
                            id,
                            rs.getString("emergency_type"),
                            rs.getInt("severity"),
                            rs.getString("contact_number"),
                            rs.getString("address")
                    );
                    r.setStatus(rs.getString("status"));
                    r.setReporterEmail(rs.getString("reporter_email"));

                    list.add(r);
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return FXCollections.observableArrayList(list);
    }

    public void updateAssignmentStatus(UUID reportId, String newStatus) {
        String sql = "UPDATE assignments SET status = ? WHERE report_id = ?";
        try (var cx = DBConnection.getConnection(); var ps = cx.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setString(2, reportId.toString());
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
