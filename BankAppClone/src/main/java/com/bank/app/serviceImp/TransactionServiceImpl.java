/**
 * 
 */
package com.bank.app.serviceImp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.bank.app.dtos.TransactionDto;
import com.bank.app.enums.AccountType;
import com.bank.app.enums.TransactionType;
import com.bank.app.exceptions.ClientException;
import com.bank.app.models.Account;
import com.bank.app.models.Transaction;
import com.bank.app.repositories.TransactionRepository;
import com.bank.app.services.AccountService;
import com.bank.app.services.NotificationService;
import com.bank.app.services.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ernest Mampana
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
	private final AccountService accountService;
	private final NotificationService notificationService;

	ModelMapper modelMapper = new ModelMapper();

	@Override
	@Transactional
	public void transferMoney(int sourceAccNumber, int destinationAccNumber, BigDecimal amount, String reference) {

		Account sourceAccount = accountService.getAccountByAccountNumber(sourceAccNumber); // .orElse(null);

		if (sourceAccount.getAccountType().equals(AccountType.CURRENT)) {
			Account destinationAccount = accountService.getAccountByAccountNumber(destinationAccNumber); // .orElse(null);

			if (sourceAccount.getBalance().compareTo(amount) < 1)
				throw new ClientException("Payment cannot exceed available balance");

			if (destinationAccount.equals(null))
				throw new ClientException("Invalid destination account number");

			if (sourceAccount.getCustomerId().equals(destinationAccount.getCustomerId()))
				throw new ClientException("You cannot make external payment to yourself, try internal transfer");

			sourceAccount
					.setBalance(sourceAccount.getBalance().subtract(amount.add(amount.multiply(new BigDecimal(0.05)))));

			destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

			TransactionDto transactionDto = TransactionDto.builder().sourceAccountNumber(sourceAccNumber)
					.destinationAccountNumber(destinationAccNumber).amount(amount).reference(reference).build();

			// This transaction is for the account that send money or making payment
			transactionDto.setType(TransactionType.TRANSFER);
			this.createTransaction(transactionDto, sourceAccount.getAccountNumber());

			// This transaction is for the account number the money is being sent to
			transactionDto.setType(TransactionType.MONEY_IN);
			this.createTransaction(transactionDto, destinationAccount.getAccountNumber());

		} else {
			throw new ClientException("Payments can only be made using CURRENT account");
		}

	}

	@Override
	@Transactional
	public void internalMoneyTransfer(int sourceAccNumber, int destinationAccNumber, BigDecimal amount,
			String reference) {

		Account sourceAccount = accountService.getAccountByAccountNumber(sourceAccNumber); // .orElse(null);

		Account destinationAccount = accountService.getAccountByAccountNumber(destinationAccNumber); // orElse(null);

		if (destinationAccount.getCustomerId() != sourceAccount.getCustomerId())
			throw new ClientException("This operation is for internal transfer only");

		if (sourceAccount.getBalance().compareTo(amount) < 1)
			throw new ClientException("Payment cannot exceed available balance");

		if (destinationAccount.equals(null))
			throw new ClientException("Invalid destination account number");

		sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));

		destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

		TransactionDto transactionDto = TransactionDto.builder().sourceAccountNumber(sourceAccNumber)
				.destinationAccountNumber(destinationAccNumber).amount(amount).reference(reference).build();

		// This transaction is for the account that send money or making payment
		transactionDto.setType(TransactionType.TRANSFER);
		this.createTransaction(transactionDto, sourceAccount.getAccountNumber());

		// This transaction is for the account number the money is being sent to
		transactionDto.setType(TransactionType.MONEY_IN);
		this.createTransaction(transactionDto, destinationAccount.getAccountNumber());

	}

	@Override
	public Transaction createTransaction(TransactionDto dto, int accountNumber) {

		Transaction transaction = new Transaction();
		transaction.setSourceAccount(dto.getSourceAccountNumber() == 0 ? null
				: accountService.getAccountByAccountNumber(dto.getSourceAccountNumber()));

		transaction.setDestinationAccount(dto.getDestinationAccountNumber() == 0 ? null
				: accountService.getAccountByAccountNumber(dto.getSourceAccountNumber()));
		transaction.setAmount(dto.getAmount());
		transaction.setType(dto.getType());
		transaction.setTransactionDate(LocalDateTime.now());
		transaction.setAccountNumber(accountNumber);
		transaction.setReference(dto.getReference());

		transaction = transactionRepository.save(transaction);

//		String val = "2015-07-18T13:32:56.971-0400";
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//		LocalDateTime dateTime = java.time.LocalDateTime.parse(val, formatter);
//		System.out.println("===================================="+dateTime);

		// notificationService.sendTransactionNotification(transaction);

		return transaction;
	}

	@Override
	public List<Transaction> getAllTransactions() {

		return transactionRepository.findAll();
	}

	@Override
	public List<TransactionDto> getTransactionsByAccountNumber(int accountNumber) {
		log.info("============================ Fetching transactions for accoount number {}", accountNumber);
		Account account = accountService.getAccountByAccountNumber(accountNumber);

		List<Transaction> transactions = transactionRepository.findAllByAccountNumber(account.getAccountNumber()).get();

		List<TransactionDto> transactionsDto = new ArrayList<>();

		for (int i = 0; i < transactions.size(); i++) {

			TransactionDto transaction = new TransactionDto();

			transaction = modelMapper.map(transactions.get(i), TransactionDto.class);

			transactionsDto.add(transaction);

		}

		return transactionsDto;
	}

	@Override
	@Transactional
	public String debitCustomerAccount(int accountNumber, BigDecimal amount, String reference) {

		String message = "debit was successful";

		Account account = accountService.getAccountByAccountNumber(accountNumber);

		if (account.getBalance().compareTo(amount) < 1)
			throw new ClientException("Insufficient funds.");

		account.setBalance(account.getBalance().subtract(amount.add(amount.multiply(new BigDecimal(0.05)))));

		TransactionDto transactionDto = TransactionDto.builder().amount(amount).type(TransactionType.DEBIT)
				.sourceAccountNumber(accountNumber).reference(reference).build();

		this.createTransaction(transactionDto, accountNumber);

		return message;
	}

	@Override
	@Transactional
	public String creditCustomerAccount(int accountNumber, BigDecimal amount, String reference) {
		String message = "credit was successful";
		Account account = accountService.getAccountByAccountNumber(accountNumber);

		account.setBalance(account.getBalance().add(amount));

		TransactionDto dto = TransactionDto.builder().amount(amount).destinationAccountNumber(accountNumber)
				.type(TransactionType.MONEY_IN).reference(reference).build();

		this.createTransaction(dto, accountNumber);

		return message;
	}

	@Override
	public List<TransactionDto> getFilteredTransactionsByAccountNumber(int accountNumber, LocalDate startDate,
			LocalDate endDate) {

		List<Transaction> transactions = transactionRepository.findAllByAccountNumberAndTransactionDateBetween(
				accountNumber, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay()).get();

		List<TransactionDto> transactionsDto = new ArrayList<>();

		for (int i = 0; i < transactions.size(); i++) {

			TransactionDto transaction = new TransactionDto();

			transaction = modelMapper.map(transactions.get(i), TransactionDto.class);

			transactionsDto.add(transaction);

		}
		return transactionsDto;
	}

}
