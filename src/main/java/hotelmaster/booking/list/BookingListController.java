package hotelmaster.booking.list;

import hotelmaster.Booking;
import hotelmaster.account.AccountSession;
import hotelmaster.booking.BookingDAO;
import hotelmaster.notification.NotificationService;
import hotelmaster.notification.NotificationType;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for displaying user's booking history
 */
@Controller
public class BookingListController {
    
    @Autowired
    private BookingDAO bookingDAO;
    
    @Autowired
    private AccountSession accountSession;
    
    @Autowired
    private NotificationService notificationService;
    
    @RequestMapping(value="/mybookings", method = RequestMethod.GET)
    public ModelAndView listBookings() {
        ModelAndView modelAndView = new ModelAndView("bookingList");
        
        // Check if user is logged in
        if (accountSession.getAccount() == null) {
            notificationService.add("Error:", "Please login first to view your bookings", NotificationType.ERROR);
            return new ModelAndView("redirect:/login");
        }
        
        int accountId = accountSession.getAccount().getId();
        List<Booking> bookings = bookingDAO.getBookingsByAccountId(accountId);
        
        modelAndView.addObject("bookingsList", bookings);
        modelAndView.setViewName("bookingList");
        
        return modelAndView;
    }
}
