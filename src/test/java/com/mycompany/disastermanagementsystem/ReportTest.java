/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem;

import org.junit.Test;
import static org.junit.Assert.*;

import com.mycompany.disastermanagementsystem.models.Report;

/**
 *
 * @author Kanch
 */

public class ReportTest {
    @Test
    public void testGettersAndSetters() {
        Report report = new Report("Flood", 2, "0712345678", "Sunnybank");
        report.setEmergencyType("Fire");
        report.setSeverity(3);
        report.setContactNumber("0777777777");
        report.setAddress("Runcorn");

        assertEquals("Fire", report.getEmergencyType());
        assertEquals(3, report.getSeverity());
        assertEquals("0777777777", report.getContactNumber());
        assertEquals("Runcorn", report.getAddress());
        assertNotNull(report.getReportID());
        assertNotNull(report.getDate());
    }
}

