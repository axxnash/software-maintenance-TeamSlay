/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmaster.contact.form;

import hotelmaster.notification.NotificationService;
import hotelmaster.notification.NotificationType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author GEORGE
 */


@Controller
public class ContactController {
 
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private NotificationService notificationService;
    
    @RequestMapping(value="/contact", method = RequestMethod.GET)
    public ModelAndView showContactForm(){
        ModelAndView modelAndView = new ModelAndView("contact");
        ContactForm cf = new ContactForm();
        modelAndView.addObject("contactForm", cf);
        return modelAndView;
    }
     
    
    @RequestMapping(value="/contact", method = RequestMethod.POST)
    public ModelAndView processForm(@ModelAttribute("contactForm") @Valid ContactForm cf,  BindingResult result, Errors errors) {
         ModelAndView modelAndView = new ModelAndView("contactResult");
        
        // CR-02: Check email format
        if (cf.getEmail() == null || !cf.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            notificationService.add(
                "Invalid Email",
                "Please enter a valid email address.",
                NotificationType.ERROR
            );
            modelAndView.setViewName("contact");
            modelAndView.addObject("contactForm", cf);
            return modelAndView;
        }
        
        // CR-02: Check message is not empty
        if (cf.getMessage() == null || cf.getMessage().trim().isEmpty()) {
            notificationService.add(
                "Empty Message",
                "Message field cannot be empty.",
                NotificationType.ERROR
            );
            modelAndView.setViewName("contact");
            modelAndView.addObject("contactForm", cf);
            return modelAndView;
        }
        
        if (!result.hasErrors()){
            System.out.println("Form submitted: No Errors.");
            
            modelAndView.addObject("contactForm", cf);
                        
            String senderEmail = cf.getEmail();
            
            String subject = cf.getSubject();
            String message = cf.getMessage();

            // creates a simple e-mail object
            SimpleMailMessage email = new SimpleMailMessage();
            
            email.setFrom(senderEmail);
            email.setTo("george.didok@gmail.com"); //SET RECEPIENTS
            email.setSubject(subject);
            email.setText(message);

            // sends the e-mail
            mailSender.send(email);
            
            
            // for testing purpose:
            System.out.println("name: " + cf.getName());
            System.out.println("email: " + cf.getEmail());
            System.out.println("subject: " + cf.getSubject());
            System.out.println("message: " + cf.getMessage());
        } else {
            
            String name = cf.getName();
            String email = cf.getEmail();
            String subject = cf.getSubject();
            String message = cf.getMessage();
            
            if(name.isEmpty() || email.isEmpty() || subject.isEmpty() || message.isEmpty()){
                result.reject("name", "");
            }
            System.out.println("Form submitted: Errors.");
            //result.reject("Errors: Youve been rejected!");
            modelAndView.addObject("contactForm", cf);
            modelAndView.setViewName("contact");
        }
         
        return modelAndView;
    }
    
    
}