/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.daos;

import com.mycompany.disastermanagementsystem.models.Feedback;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Kanch
 */
public class FeedbackDao {
    public static final FeedbackDao INSTANCE = new FeedbackDao();
    private final Map<UUID, Feedback> store = new HashMap<>();
    
    private FeedbackDao() {}
    
    public void save(Feedback f) { 
        store.put(f.getReportID(), f); 
    }
    
    public Feedback get(UUID reportID){ 
        return store.get(reportID);  
    }
}
