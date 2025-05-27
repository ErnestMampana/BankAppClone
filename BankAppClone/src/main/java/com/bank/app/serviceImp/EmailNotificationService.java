/**
 * 
 */
package com.bank.app.serviceImp;

import com.bank.app.dtos.EmailDetails;
import com.bank.app.models.Customer;
import com.bank.app.models.Transaction;
import com.bank.app.services.CustomerService;
import com.bank.app.services.NotificationService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


import lombok.RequiredArgsConstructor;

/**
 * @author Ernest Mampana
 *
 */
@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {
	
	private final CustomerService customerService;
	
	private final JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String sender;
	
    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@Override
	public void sendTransactionNotification(Transaction transaction) {
		//Date date = dt.parse(transaction.getTransactionDate());
		

		 //Get the customer associated with the source account
        Customer customer = customerService.getCustomerById(transaction.getSourceAccount().getCustomerId());

        // Construct the email message
        String subject = "Transaction Notification";
        String message = "Dear " + customer.getName() + ",\n\n"
                         + "This is to notify you of a transaction that was performed on your account.\n\n"
                         + "Transaction Details:\n"
                         + "-------------------\n"
                         + "Type: " + transaction.getType() + "\n"
                         + "Amount: R" + transaction.getAmount() + "\n"
                         + "Reference: " + transaction.getReference() + "\n"
                         //+ "Date: " + date + "\n\n"
                         + "Thank you for banking with us.\n"
                         + "Bank X";

        // Send the email message to the customer's email address
        String emailAddress = customer.getEmail();
        
		EmailDetails details = EmailDetails.builder().recipient(emailAddress).subject(subject).message(message).build();
        
        sendSimpleMail(details);
		
	}

	@Override
	public String sendSimpleMail(EmailDetails
			details) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			// Setting up necessary details
			mailMessage.setFrom(sender);
			mailMessage.setTo(details.getRecipient());
			mailMessage.setText(details.getMessage());
			mailMessage.setSubject(details.getSubject());

			// Sending the mail
			javaMailSender.send(mailMessage);
			return "Mail Sent Successfully...";
		}
		catch (Exception e) {
			return "Error while Sending Mail";
		}
	}

}
