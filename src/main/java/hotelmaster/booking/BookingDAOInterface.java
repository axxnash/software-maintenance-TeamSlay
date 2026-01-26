/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmaster.booking;

import hotelmaster.Booking;
import java.util.List;

/**
 *
 * @author GEORGE
 */
public interface BookingDAOInterface {
    int insertBooking(Booking booking);
    List<Booking> getBookingsByAccountId(int accountId);
}
