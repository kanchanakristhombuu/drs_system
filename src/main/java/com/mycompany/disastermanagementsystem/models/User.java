/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kanch
 */
public class User {
    private final StringProperty email    = new SimpleStringProperty(this, "email");
    private final StringProperty password = new SimpleStringProperty(this, "password");
    private final StringProperty role     = new SimpleStringProperty(this, "role");
    private final StringProperty name     = new SimpleStringProperty(this, "name");
    
    public User(String email, String password, String role, String name) {
        setEmail(email);
        setPassword(password);
        setRole(role);
        setName(name);
    }
    
    public String getEmail() { 
        return email.get(); 
    }
    
    public void setEmail(String e) { 
        email.set(e); 
    }
    
    public StringProperty emailProperty() { 
        return email; 
    }

    public String getPassword() { 
        return password.get(); 
    }
    
    public void setPassword(String p) { 
        password.set(p); 
    }
    
    public StringProperty passwordProperty() { 
        return password; 
    }

    public String getRole() { 
        return role.get(); 
    }
    
    public void setRole(String r) { 
        role.set(r); 
    }
    
    public StringProperty roleProperty() { 
        return role; 
    }

    public String getName() { 
        return name.get(); 
    }
    
    public void setName(String n) { 
        name.set(n); 
    }
    
    public StringProperty nameProperty() { 
        return name; 
    }
}
