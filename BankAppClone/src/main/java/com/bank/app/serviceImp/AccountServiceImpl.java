/**
 * 
 */
package com.bank.app.serviceImp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.bank.app.enums.AccountType;
import com.bank.app.exceptions.ClientException;
import com.bank.app.models.Account;
import com.bank.app.models.Customer;
import com.bank.app.repositories.AccountRepository;
import com.bank.app.repositories.CustomerRepository;
import com.bank.app.services.AccountService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * @author Ernest Mampana
 *
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	private final CustomerRepository customerRepository;

	BigDecimal initialCredit = new BigDecimal(500.00);
	Account account = new Account();

	@Override
	public void addAccountForUser(String cellphoneNumber) {

		Customer customer = customerRepository.findByCellphoneNumber(cellphoneNumber).orElse(null);

		if (customer.equals(null))
			throw new ClientException("User not found.");

		Account currentAccount = Account.builder().accountNumber(account.generateAccountNumber())
				.accountType(AccountType.CURRENT).customerId(customer.getId()).balance(new BigDecimal(00.00)).build();

		accountRepository.save(currentAccount);

		Account savingsAccount = Account.builder().accountNumber(account.generateAccountNumber())
				.accountType(AccountType.SAVINGS).customerId(customer.getId()).balance(initialCredit).build();

		accountRepository.save(savingsAccount);

	}

	@Override
	public Account getAccountByAccountNumber(int accountNumber) {
		Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);
		if (accountOptional.isEmpty())
			throw new ClientException("Account not found");
		return accountOptional.get();
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
		List<Account> accounts = accountRepository.findAllByCustomerId(customerId);
		return accounts;
	}

//	@Override
//	public List<Transaction> getTransactionHistory(int accountNumber, LocalDate startDate, LocalDate endDate) {
//		Account account = getAccountByAccountNumber(accountNumber);
//        return account.getTransactionsByDateRange(startDate, endDate);
//	}

}
