/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.daos;

import com.mycompany.disastermanagementsystem.models.Authority;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Kanch
 */
public class AuthorityDao {

    public static final AuthorityDao AUTHORITY_INSTANCE = new AuthorityDao();
    private final ObservableList<Authority> list = FXCollections.observableArrayList();
    public static final AuthorityDao INSTANCE = new AuthorityDao();

    private AuthorityDao() {
        list.add(new Authority("Fire & Rescue", "000", "info@fire.gov.au", "Fire"));
        list.add(new Authority("Bureau of Meteorology (Severe Weather)", "1300 634 034", "emergency@bom.gov.au", "Hurricane"));
        list.add(new Authority("Wildlife Rescue Service", "13 000 727", "wildlife@environment.gov.au", "Dangerous Animal"));
        list.add(new Authority("State Emergency Service (Flood Unit)", "132 500", "floodinfo@ses.nsw.gov.au", "Flood"));
        list.add(new Authority("Geoscience Australia (Earthquake Unit)", "1800 811 214", "earthquakes@ga.gov.au", "Earthquake"
        ));
    }

    public Authority findByType(String type) {
        return list.stream().filter(a -> a.getTypeHandled().equals(type)).findFirst().orElse(null);
    }
}
