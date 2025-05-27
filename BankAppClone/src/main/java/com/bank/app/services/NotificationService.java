/**
 * 
 */
package com.bank.app.services;

import com.bank.app.dtos.EmailDetails;
import com.bank.app.models.Transaction;


//import com.bank.co.za.bankX.models.Transaction;

/**
 * @author Ernest Mampana
 *
 */
public interface NotificationService {
	void sendTransactionNotification(Transaction transaction);
	String sendSimpleMail(EmailDetails details);
}
