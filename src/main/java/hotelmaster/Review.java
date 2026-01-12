/*
 * Booking.java
 * 
 * Changes made for Task 2 (Version Control contribution):
 * 1. Added calculateCostPerNight() method to get average cost per night
 * 2. Added isUpcoming() method to check if booking is in the future
 * 3. Overridden toString() for easier debugging and logging
 */

package hotelmaster;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Booking {
    private String startDate;
    private String endDate;
    private String bookingDate;
    private int numGuests;
    private int numNights;
    private int roomID;
    private int account_id;
    private int booking_id;
    private double totalCost;
    private String bookingURL;

    public Booking(){}
    
    public Booking(String startDate, String endDate, int numGuests, int room, int account){
        this.startDate = startDate;
        this.endDate = endDate;
        this.numGuests = numGuests;
        this.roomID = room;
        this.account_id = account;
    }
    
    // Getters and Setters
    public int getBooking_id(){
        return booking_id;
    }
    
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }
    
    public int getNumNights() {
        return numNights;
    }

    public void setNumNights(int numNights) {
        this.numNights = numNights;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getBookingURL() {
        return bookingURL;
    }

    public void setBookingURL(String bookingURL) {
        this.bookingURL = bookingURL;
    }

    // Method to calculate number of nights between two dates
    public void setNumNights(String fromDate, String toDate) {    
       Calendar cal1 = new GregorianCalendar();
       Calendar cal2 = new GregorianCalendar();
       System.out.println(fromDate + " and " + toDate);

       // Parse fromDate
       String yearFrom = fromDate.substring(0,4);
       String monFrom = fromDate.substring(5,7);
       String ddFrom = fromDate.substring(8,10);
       int intYearFrom = Integer.parseInt(yearFrom);
       int intMonFrom = Integer.parseInt(monFrom);
       int intDdFrom = Integer.parseInt(ddFrom);
       cal1.set(intYearFrom, intMonFrom, intDdFrom);

       // Parse toDate
       String yearTo = toDate.substring(0,4);
       String monTo = toDate.substring(5,7);
       String ddTo = toDate.substring(8,10);
       int intYearTo = Integer.parseInt(yearTo);
       int intMonTo = Integer.parseInt(monTo);
       int intDdTo = Integer.parseInt(ddTo);
       cal2.set(intYearTo, intMonTo, intDdTo);

       // Calculate nights between
       int numNights = nightsBetween(cal1.getTime(), cal2.getTime());
       this.numNights = numNights;
    }
    
    public int nightsBetween(Date d1, Date d2) {
       return (int)((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    // ------------------- Task 2 Additions -------------------

    // 1. Calculate average cost per night
    // Useful for reporting or UI display
    public double calculateCostPerNight() {
        if (numNights > 0) {
            return totalCost / numNights;
        } else {
            return 0;
        }
    }

    // 2. Check if booking is upcoming
    // Returns true if start date is after today
    public boolean isUpcoming() {
        try {
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            return start.after(today);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Override toString() for easier debugging and logging
    @Override
    public String toString() {
        return "Booking ID: " + booking_id + ", Room ID: " + roomID + ", Guests: " + numGuests + 
               ", From: " + startDate + " To: " + endDate + ", Total Cost: RM" + totalCost;
    }
}
