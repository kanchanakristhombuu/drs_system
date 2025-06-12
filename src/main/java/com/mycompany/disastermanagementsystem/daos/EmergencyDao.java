/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.daos;

import com.mycompany.disastermanagementsystem.db.DBConnection;
import com.mycompany.disastermanagementsystem.models.Report;
import com.mycompany.disastermanagementsystem.models.Session;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kanch
 */
public class EmergencyDao {

    public static final EmergencyDao INSTANCE = new EmergencyDao();

     private static final String INSERT_SQL =
        "INSERT INTO emergency " +
        "(id, emergency_type, severity, contact_number, address, status, reporter_email) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
     
    private EmergencyDao() {
    }

    public void save(Report r) {
        try (Connection cx = DBConnection.getConnection();
             PreparedStatement ps = cx.prepareStatement(INSERT_SQL)) {

            ps.setString(1, r.getReportID().toString());
            ps.setString(2, r.getEmergencyType());
            ps.setInt   (3, r.getSeverity());
            ps.setString(4, r.getContactNumber());
            ps.setString(5, r.getAddress());
            ps.setString(6, r.getStatus());

            // 7) the currently logged-in user's email
            String email = Session.getCurrentUser().getEmail();
            ps.setString(7, email);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Report> getAll() {
        List<Report> list = new ArrayList<>();
        String sql = "SELECT id, emergency_type, severity, contact_number, address, status, date_reported FROM emergency";
        try (Connection cx = DBConnection.getConnection(); PreparedStatement ps = cx.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Report r = new Report(
                        rs.getString("emergency_type"),
                        rs.getInt("severity"),
                        rs.getString("contact_number"),
                        rs.getString("address")
                );
                r.setStatus(rs.getString("status"));
                // parse date_reported if you’ve added a date field
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

