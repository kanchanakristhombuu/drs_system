/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.disastermanagementsystem.utils;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.mycompany.disastermanagementsystem.daos.FeedbackDao;
import com.mycompany.disastermanagementsystem.models.Feedback;
import com.mycompany.disastermanagementsystem.models.Report;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author disal
 */
public class PdfExporter {

    private static final FeedbackDao feedbackDao = FeedbackDao.INSTANCE;

    public static void generatePdfReport(File file, List<Report> reports) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        document.add(new Paragraph("Emergency Report"));
        document.add(new Paragraph("Generated on: " + LocalDateTime.now()));
        document.add(new Paragraph(" "));

        for (Report report : reports) {
            document.add(new Paragraph("ID: " + report.getReportID()));
            document.add(new Paragraph("Date: " + report.getDate()));
            document.add(new Paragraph("Type: " + report.getEmergencyType()));
            document.add(new Paragraph("Severity: " + report.getSeverity()));
            document.add(new Paragraph("Contact: " + report.getContactNumber()));
            document.add(new Paragraph("Address: " + report.getAddress()));

            List<Feedback> feedbacks = feedbackDao.findByReport(report.getReportID());
            if (!feedbacks.isEmpty()) {
                for (Feedback fb : feedbacks) {
                    document.add(new Paragraph("Feedback by " + fb.getUserEmail() + " on " + fb.getDateGiven() + ":"));
                    document.add(new Paragraph(fb.getFeedbackText()));
                    document.add(new Paragraph(" "));
                }
            } else {
                document.add(new Paragraph("Feedback: No feedback available"));
            }

            document.add(new Paragraph("----------------------------------------"));
        }

        document.close();
    }

}
