/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.dao;

import com.mycompany.disastermanagementsystem.models.Session;
import com.mycompany.disastermanagementsystem.models.User;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kanch
 */

public class SessionTest {
    
    @Test
    public void testSetAndGetCurrentUser() {
        User user = new User("test@example.com", "password", "User", "user");
        Session.setCurrentUser(user);
        assertEquals("test@example.com", Session.getCurrentUser().getEmail());
    }
    
}
