/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.daos;

import com.mycompany.disastermanagementsystem.db.DBConnection;
import com.mycompany.disastermanagementsystem.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Kanch
 */
public class UserDao {
    public static final UserDao INSTANCE = new UserDao();
    private UserDao() {}
    
    /**
     * Look up a user by email in the database.
     * @return a User object if found, or null.
     */
    
    public User findByEmail(String email) {
        String sql = "SELECT email, password, role, name FROM users WHERE email = ?";

        try (Connection cx = DBConnection.getConnection();
             PreparedStatement ps = cx.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String em   = rs.getString("email");
                    String pw   = rs.getString("password");
                    String role = rs.getString("role");
                    String name = rs.getString("name");
                    return new User(em, pw, role, name);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
    
    
}
