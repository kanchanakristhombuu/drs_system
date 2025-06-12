/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.models;

/**
 *
 * @author Kanch
 */
public class Authority {
    private final String name;
    private final String phone;
    private final String email;
    private final String typeHandled;

    public Authority(String name, String phone, String email, String typeHandled) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.typeHandled = typeHandled;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getTypeHandled() {
        return typeHandled;
    }
    
    

}
