/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.models;

/**
 *
 * @author Kanch
 */
public class Session {
    private static User currentUser;
    public static void setCurrentUser(User u) { 
        currentUser = u; 
    }
    
    public static User getCurrentUser() { 
        return currentUser; 
    }
}
