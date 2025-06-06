/**
 * 
 */
package com.bank.app.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.bank.app.dtos.DebitCreditDto;
import com.bank.app.dtos.TransactionDto;
import com.bank.app.models.Account;
import com.bank.app.models.Transaction;


/**
 * @author Ernest Mampana
 *
 */
public interface TransactionService {

	Transaction createTransaction(TransactionDto transactionDto, int accountNumver);
	
	Transaction createDepositTransaction(TransactionDto dto);

	List<Transaction> getAllTransactions();
	
	void transferMoney(int sourceAccNumber,int destinationAccNumber,BigDecimal amount,String reference);
	
	void internalMoneyTransfer(int sourceAccNumber,int destinationAccNumber,BigDecimal amount,String reference);
	
	List<TransactionDto> getTransactionsByAccountNumber(int accountNumber);
	
	String debitCustomerAccount(int accountNumber,BigDecimal amount,String reference);
	
	String creditCustomerAccount(int accountNumber,BigDecimal amount,String Reference);

	List<TransactionDto> getFilteredTransactionsByAccountNumber(int accountNumber, LocalDate startDate, LocalDate endDate);
	
	
//	String depositMoney(int accountNumber,BigDecimal amount,String reference);
	
//
//	void processTransaction(Transaction transaction);
////
////	void processTransactions(List<Transaction> transactions);
//
//	List<Transaction> getTransactionsForReconciliation(LocalDate date);
}
