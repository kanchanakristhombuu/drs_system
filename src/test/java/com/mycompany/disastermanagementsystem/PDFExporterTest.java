/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem;

import com.mycompany.disastermanagementsystem.models.Report;
import com.mycompany.disastermanagementsystem.utils.PdfExporter;
import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author disal
 */
public class PDFExporterTest {

    @Test
    public void testGeneratePdfDoesNotThrow() {
        try {
            List<Report> reports = Collections.singletonList(new Report("Fire", 4, "0411111111", "Salisbury"));
            File file = new File("test_report.pdf");

            PdfExporter.generatePdfReport(file, reports);

            assertTrue(file.exists());
            file.delete();
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

}
