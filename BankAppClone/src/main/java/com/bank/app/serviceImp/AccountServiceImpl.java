/**
 * 
 */
package com.bank.app.serviceImp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.bank.app.config.OnboardingProperties;
import com.bank.app.dtos.TransactionDto;
import com.bank.app.enums.AccountType;
import com.bank.app.enums.TransactionType;
import com.bank.app.exceptions.ClientException;
import com.bank.app.models.Account;
import com.bank.app.models.Customer;
import com.bank.app.repositories.AccountRepository;
import com.bank.app.repositories.CustomerRepository;
import com.bank.app.services.AccountService;
import com.bank.app.services.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ernest Mampana
 *
 */
@Service
//@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	private final CustomerRepository customerRepository;

	private final TransactionService transactionService;
	
	private final OnboardingProperties config;

	@Autowired
	public AccountServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository,
		@Lazy TransactionService transactionService, OnboardingProperties config) {
		this.accountRepository = accountRepository;
		this.customerRepository = customerRepository;
		this.transactionService = transactionService;
		this.config = config;
	}
	
//	BigDecimal initialCredit = new BigDecimal(config.getOnboardingAmount());
	Account account = new Account();

	@Override
	public void addAccountForUser(String cellphoneNumber) {

		Customer customer = customerRepository.findByCellphoneNumber(cellphoneNumber)
				.orElseThrow(() -> new ClientException("User not found."));

		Account currentAccount = Account.builder().accountNumber(account.generateAccountNumber())
				.accountType(AccountType.CURRENT).customerId(customer.getId()).balance(new BigDecimal(00.00)).build();

		accountRepository.save(currentAccount);

		Account savingsAccount = Account.builder().accountNumber(account.generateAccountNumber())
				.accountType(AccountType.SAVINGS).customerId(customer.getId())
				.balance(new BigDecimal(config.getOnboardingAmount())).build();

		TransactionDto dto = TransactionDto.builder().amount(new BigDecimal(config.getOnboardingAmount()))
				.destinationAccountNumber(savingsAccount.getAccountNumber()).type(TransactionType.WELCOME)
				.reference(config.getReference()).build();

		accountRepository.save(savingsAccount);

		transactionService.createDepositTransaction(dto);

	}

	@Override
	public Account getAccountByAccountNumber(int accountNumber) {
		log.info("Fetching account .... # "+accountNumber);
		var accountOptional = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new ClientException("Account not found"));

		return accountOptional;
	}

	@Override
	public BigDecimal getAccountBalance(int accountNumber) {
		Account account = getAccountByAccountNumber(accountNumber);
		return account.getBalance();
	}

	@Override
	public List<Account> getAllAccounts() {
		return accountRepository.findAll();
	}

	@Override
	public List<Account> viewAccountsForUser(String cellphone) {
		Long customerId = customerRepository.findByCellphoneNumber(cellphone).get().getId();
		return accountRepository.findAllByCustomerId(customerId);
	}

//	@Override
//	public List<Transaction> getTransactionHistory(int accountNumber, LocalDate startDate, LocalDate endDate) {
//		Account account = getAccountByAccountNumber(accountNumber);
//        return account.getTransactionsByDateRange(startDate, endDate);
//	}

}
