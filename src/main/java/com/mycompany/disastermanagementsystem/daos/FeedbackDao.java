/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.daos;

import com.mycompany.disastermanagementsystem.db.DBConnection;
import com.mycompany.disastermanagementsystem.models.Feedback;
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
public class FeedbackDao {

    public static final FeedbackDao INSTANCE = new FeedbackDao();

    private FeedbackDao() {
    }

    private static final String INSERT_SQL
            = "INSERT INTO feedback (report_id, user_email, feedback_text) VALUES (?, ?, ?)";

    private static final String SELECT_BY_REPORT_SQL
            = "SELECT id, report_id, user_email, feedback_text, date_given "
            + "  FROM feedback WHERE report_id = ?";

    /**
     * Persist a new feedback entry
     */
    public void save(Feedback fb) {
        try (Connection cx = DBConnection.getConnection(); PreparedStatement ps = cx.prepareStatement(INSERT_SQL)) {
            ps.setString(1, fb.getReportID().toString());
            ps.setString(2, fb.getUserEmail());
            ps.setString(3, fb.getFeedbackText());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Load all feedback for a given report
     */
    public ObservableList<Feedback> findByReport(UUID reportID) {
        List<Feedback> list = new ArrayList<>();
        try (Connection cx = DBConnection.getConnection(); PreparedStatement ps = cx.prepareStatement(SELECT_BY_REPORT_SQL)) {
            ps.setString(1, reportID.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Feedback fb = new Feedback(
                            rs.getInt("id"),
                            UUID.fromString(rs.getString("report_id")),
                            rs.getString("user_email"),
                            rs.getString("feedback_text"),
                            rs.getTimestamp("date_given").toLocalDateTime()
                    );
                    list.add(fb);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return FXCollections.observableArrayList(list);
    }
}
