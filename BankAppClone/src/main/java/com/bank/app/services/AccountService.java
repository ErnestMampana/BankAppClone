/**
 * 
 */
package com.bank.app.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.bank.app.models.Account;


/**
 * @author Ernest Mampana
 *
 */
public interface AccountService {
	
	void addAccountForUser(String cellphoneNumber);
	
	Account getAccountByAccountNumber(int accountNumber);
	
	//List<Transaction> getTransactionHistory(int accountNumber, LocalDate startDate, LocalDate endDate);
	
	BigDecimal getAccountBalance(int accountNumber);
	
	List<Account> getAllAccounts();
	
	List<Account> viewAccountsForUser(String cellphone);
}
