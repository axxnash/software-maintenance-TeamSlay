/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmaster.booking;

import hotelmaster.Booking;
import java.sql.Types;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

/**
 *
 * @author GEORGE
 */
@Primary
@Repository
public class BookingDAO implements BookingDAOInterface{
    
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    
    @Override
    public int insertBooking(Booking booking) {
        
        jdbcTemplate = new JdbcTemplate(dataSource);
        
        String insertQuery = "INSERT INTO booking (account_id, room_id, booking_date, check_in_date, check_out_date, num_guests) VALUES (?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[]{booking.getAccount_id(), booking.getRoomID(), booking.getBookingDate(), booking.getStartDate(), booking.getEndDate(), booking.getNumGuests()};
        int[] types = new int[]{Types.INTEGER, Types.INTEGER, Types.DATE, Types.DATE, Types.DATE, Types.INTEGER};
        
        return jdbcTemplate.update(insertQuery, params, types);
    }
    
    @Override
    public List<Booking> getBookingsByAccountId(int accountId) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        
        String query = "SELECT b.booking_id, b.room_id, b.booking_date, b.check_in_date, b.check_out_date, b.num_guests, " +
                       "r.room_name, r.price_per_night " +
                       "FROM booking b " +
                       "JOIN room r ON b.room_id = r.room_id " +
                       "WHERE b.account_id = ? " +
                       "ORDER BY b.check_in_date DESC";
        
        return jdbcTemplate.query(query, new Object[]{accountId}, (rs, rowNum) -> {
            Booking booking = new Booking();
            booking.setBooking_id(rs.getInt("booking_id"));
            booking.setRoomID(rs.getInt("room_id"));
            booking.setBookingDate(rs.getString("booking_date"));
            booking.setStartDate(rs.getString("check_in_date"));
            booking.setEndDate(rs.getString("check_out_date"));
            booking.setNumGuests(rs.getInt("num_guests"));
            
            // Generate URL from room name
            String roomName = rs.getString("room_name");
            String roomViewURL = roomName.trim().replaceAll("[^a-zA-Z0-9\\-\\s\\.]", "");
            roomViewURL = roomViewURL.replaceAll("[\\-| |\\.]+", "-");
            roomViewURL = roomViewURL.toLowerCase();
            booking.setBookingURL(roomViewURL);
            
            // Calculate nights and total cost
            booking.setNumNights(rs.getString("check_in_date"), rs.getString("check_out_date"));
            double totalCost = booking.getNumNights() * rs.getDouble("price_per_night");
            booking.setTotalCost(totalCost);
            
            return booking;
        });
    }
    
}
