package hotelmaster.admin.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hotelmaster.Report;
import hotelmaster.account.AccountLevel;
import hotelmaster.account.AccountSession;
import hotelmaster.notification.NotificationService;
import hotelmaster.notification.NotificationType;
import hotelmaster.rooms.RoomDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Danny
 */

@Controller
public class ReportController {
    
    @Autowired
    private ReportDAO reportDAO;
    @Autowired 
    private RoomDAO roomDAO;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AccountSession accountSession;    
    
    @RequestMapping(value = "/admin/reports")
    public ModelAndView showForm(ModelAndView model) throws IOException {
        
        if (accountSession.getAccount() == null || accountSession.getAccount().getAccountLevel() == AccountLevel.USER) {
            notificationService.add("Error!", "You do not have the required permissions to access the page", NotificationType.ERROR);
            return new ModelAndView("redirect:/home");
        }
        
        ReportForm reportForm = new ReportForm();
        TreeMap<String, String> floors = roomDAO.getFloors();

        model.addObject("floors", floors);
        model.addObject("reportForm", reportForm);
        model.setViewName("admin/reports");
        
        return model;
    }
    
    @RequestMapping(value = "/admin/reports", method = RequestMethod.POST)
    public ModelAndView listResults(@ModelAttribute("reportForm") ReportForm reportForm, ModelAndView model) throws IOException {
        
        if (accountSession.getAccount() == null || accountSession.getAccount().getAccountLevel() == AccountLevel.USER) {
            notificationService.add("Error!", "You do not have the required permissions to access the page", NotificationType.ERROR);
            return new ModelAndView("redirect:/home");
        }
        
        List<Report> report = reportDAO.listBookings(reportForm.getCheckInDate(), reportForm.getCheckOutDate(), reportForm.getLowerPricePerNight(), reportForm.getUpperPricePerNight(), reportForm.getFloor());
        TreeMap<String, String> floors = roomDAO.getFloors();
        
        double total = 0;
        
        for (Report r : report){
            
            total += Double.parseDouble(r.getTotalPrice());
        }
        
        model.addObject("floors", floors);
        model.addObject("total", String.format("%.2f", total));
        model.addObject("report", report);
        model.addObject("reportForm", reportForm);
        model.setViewName("admin/reports");
               
        return model;
    }
    
    @RequestMapping(value = "/admin/reports/export/csv", method = RequestMethod.POST)
    public void exportCSV(@ModelAttribute("reportForm") ReportForm reportForm, HttpServletResponse response) throws IOException {
        
        if (accountSession.getAccount() == null || accountSession.getAccount().getAccountLevel() == AccountLevel.USER) {
            response.sendRedirect("/home");
            return;
        }
        
        List<Report> report = reportDAO.listBookings(reportForm.getCheckInDate(), reportForm.getCheckOutDate(), 
                                                      reportForm.getLowerPricePerNight(), reportForm.getUpperPricePerNight(), 
                                                      reportForm.getFloor());
        
        // Set response headers for CSV download
        response.setContentType("text/csv");
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        response.setHeader("Content-Disposition", "attachment; filename=\"booking_report_" + timestamp + ".csv\"");
        
        // Create CSV writer
        PrintWriter writer = response.getWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("Account ID", "Room ID", "Room Name", "Floor", "Check-in Date", 
                           "Check-out Date", "Length of Stay", "Price Per Night", "Total Price"));
        
        double total = 0;
        
        for (Report r : report) {
            csvPrinter.printRecord(
                r.getAccountID(),
                r.getRoomID(),
                r.getRoomName(),
                r.getFloor(),
                r.getCheckInDate(),
                r.getCheckOutDate(),
                r.getLengthOfStay(),
                String.format("%.2f", r.getPricePerNight()),
                r.getTotalPrice()
            );
            total += Double.parseDouble(r.getTotalPrice());
        }
        
        // Add total row
        csvPrinter.printRecord("", "", "", "", "", "", "", "TOTAL:", String.format("%.2f", total));
        
        csvPrinter.flush();
        csvPrinter.close();
    }
    
    @RequestMapping(value = "/admin/reports/export/pdf", method = RequestMethod.POST)
    public void exportPDF(@ModelAttribute("reportForm") ReportForm reportForm, HttpServletResponse response) throws IOException {
        
        if (accountSession.getAccount() == null || accountSession.getAccount().getAccountLevel() == AccountLevel.USER) {
            response.sendRedirect("/home");
            return;
        }
        
        List<Report> report = reportDAO.listBookings(reportForm.getCheckInDate(), reportForm.getCheckOutDate(), 
                                                      reportForm.getLowerPricePerNight(), reportForm.getUpperPricePerNight(), 
                                                      reportForm.getFloor());
        
        // Set response headers for PDF download
        response.setContentType("application/pdf");
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        response.setHeader("Content-Disposition", "attachment; filename=\"booking_report_" + timestamp + ".pdf\"");
        
        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, response.getOutputStream());
            
            document.open();
            
            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("HotelMaster Booking Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Add report parameters
            Font paramFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
            if (reportForm.getCheckInDate() != null && !reportForm.getCheckInDate().isEmpty()) {
                document.add(new Paragraph("Check-in Date: " + reportForm.getCheckInDate(), paramFont));
            }
            if (reportForm.getCheckOutDate() != null && !reportForm.getCheckOutDate().isEmpty()) {
                document.add(new Paragraph("Check-out Date: " + reportForm.getCheckOutDate(), paramFont));
            }
            if (reportForm.getFloor() != null && !reportForm.getFloor().equals("All")) {
                document.add(new Paragraph("Floor: " + reportForm.getFloor(), paramFont));
            }
            document.add(new Paragraph(" "));
            
            // Create table
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            // Set column widths
            float[] columnWidths = {1f, 1f, 2f, 1f, 1.5f, 1.5f, 1.2f, 1.5f, 1.5f};
            table.setWidths(columnWidths);
            
            // Add table headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
            String[] headers = {"Account ID", "Room ID", "Room Name", "Floor", "Check-in", "Check-out", "Nights", "Price/Night", "Total"};
            
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.DARK_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
            }
            
            // Add data rows
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);
            double total = 0;
            
            for (Report r : report) {
                table.addCell(new Phrase(String.valueOf(r.getAccountID()), dataFont));
                table.addCell(new Phrase(String.valueOf(r.getRoomID()), dataFont));
                table.addCell(new Phrase(r.getRoomName(), dataFont));
                table.addCell(new Phrase(r.getFloor(), dataFont));
                table.addCell(new Phrase(r.getCheckInDate(), dataFont));
                table.addCell(new Phrase(r.getCheckOutDate(), dataFont));
                table.addCell(new Phrase(String.valueOf(r.getLengthOfStay()), dataFont));
                table.addCell(new Phrase(String.format("$%.2f", r.getPricePerNight()), dataFont));
                table.addCell(new Phrase("$" + r.getTotalPrice(), dataFont));
                
                total += Double.parseDouble(r.getTotalPrice());
            }
            
            // Add total row
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.BLACK);
            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
            emptyCell.setColspan(7);
            emptyCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(emptyCell);
            
            PdfPCell totalLabelCell = new PdfPCell(new Phrase("TOTAL:", totalFont));
            totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalLabelCell.setPadding(5);
            totalLabelCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(totalLabelCell);
            
            PdfPCell totalValueCell = new PdfPCell(new Phrase(String.format("$%.2f", total), totalFont));
            totalValueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalValueCell.setPadding(5);
            totalValueCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(totalValueCell);
            
            document.add(table);
            
            // Add footer
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, BaseColor.GRAY);
            Paragraph footer = new Paragraph("Generated on " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), footerFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            footer.setSpacingBefore(20);
            document.add(footer);
            
            document.close();
            
        } catch (DocumentException e) {
            throw new IOException("Error generating PDF: " + e.getMessage(), e);
        }
    }
}
