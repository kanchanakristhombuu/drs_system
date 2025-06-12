/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.daos;

import com.mycompany.disastermanagementsystem.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Kanch
 */
public class UserDao {
    public static final UserDao INSTANCE = new UserDao();

    private final ObservableList<User> users = FXCollections.observableArrayList();

    private UserDao() {
        users.add(new User("admin@example.com", "admin", "Admin", "Administrator"));
        users.add(new User("user@example.com", "user", "User", "Regular User"));
        users.add(new User("fire@example.com", "service", "Fire", "Fire Service"));
        users.add(new User("flood@example.com", "service", "Flood", "Flood Service"));
        users.add(new User("earthquake@example.com","service", "Earthquake", "Quake Service"));
        users.add(new User("hurricane@example.com", "service", "Hurricane", "Hurricane Service"));
        users.add(new User("animal@example.com", "service", "Dangerous Animal", "Animal Service"));
    }
    
    public User findByEmail(String email) {
        return users.stream()
                    .filter(u -> u.getEmail().equalsIgnoreCase(email))
                    .findFirst()
                    .orElse(null);
    }
}
