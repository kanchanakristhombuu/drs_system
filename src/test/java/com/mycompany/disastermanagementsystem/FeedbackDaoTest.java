/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem;

import com.mycompany.disastermanagementsystem.daos.FeedbackDao;
import com.mycompany.disastermanagementsystem.models.Feedback;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author disal
 */
public class FeedbackDaoTest {

    @Test
    public void testFindByReportReturnsList() {
        UUID randomId = UUID.randomUUID();
        List<Feedback> result = FeedbackDao.INSTANCE.findByReport(randomId);
        assertNotNull(result);
    }

}
