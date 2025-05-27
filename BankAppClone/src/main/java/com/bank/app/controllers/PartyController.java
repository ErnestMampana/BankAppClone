/**
 *
 */
package com.bank.app.controllers;

import java.lang.ProcessBuilder.Redirect;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import com.bank.app.dtos.AuthenticationRequestDto;
import com.bank.app.dtos.CustomerDto;
import com.bank.app.dtos.TransactionDto;
import com.bank.app.dtos.TransferMoneyRequestDto;
import com.bank.app.models.Account;
import com.bank.app.serviceImp.CustomerServiceImpl;
import com.bank.app.services.AccountService;
import com.bank.app.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;



//import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author Ernest Mampana
 *
 */
@Controller
@RequiredArgsConstructor
@RequestMapping
public class PartyController {

	private final CustomerServiceImpl customerService;

	private final AccountService accountService;

	private final TransactionService transactionService;

	@GetMapping
	public String showForm(Model model) {
		CustomerDto customer = new CustomerDto();
		AuthenticationRequestDto user = new AuthenticationRequestDto();
		model.addAttribute("customer", customer);
		model.addAttribute("user", user);
		return "index";
	}

	@PostMapping("/register")
	public String onBoardingCustomer(@ModelAttribute CustomerDto customer, BindingResult errors, Model model) {
		customerService.onBoardingCustomer(customer);
		List<Account> accounts = accountService.viewAccountsForUser(customer.getCellphoneNumber());
		model.addAttribute("accounts", accounts);
		return "view_accounts";
	}


	@PostMapping("/login")
	public String login(@ModelAttribute AuthenticationRequestDto request, Model model) {
		System.out.println(request);
		try {
			List<Account> accounts = customerService.login(request);
			model.addAttribute("accounts", accounts);
		} catch (AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "view_accounts";
	}

	@GetMapping("/transferRequest/{userId}")
	public String getUserAccounts(@PathVariable("userId") Long userId,Model model) {
		String cellphone = customerService.getCustomerById(userId).getCellphoneNumber();
		List<Account> accounts = accountService.viewAccountsForUser(cellphone);
		model.addAttribute("accounts", accounts);
		return "payment";
	}

	@GetMapping("/transactions/{accountNumber}")
	public String getTransactionsByAccountNumber(@PathVariable("accountNumber") int accountNumber,Model model) {

		List<TransactionDto> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);

		model.addAttribute("transactions", transactions);
		model.addAttribute("accountNo",accountNumber);

		return "transactions";

	}

	@PostMapping("/internal/transfer")
	public String transferMoneyInternal(@ModelAttribute TransferMoneyRequestDto request, Model model) {
		System.out.println(request);
		transactionService.internalMoneyTransfer(request.getSourceAccountNumber(),
				request.getDestinationAccountNumber(), request.getAmount(), request.getReference());
		return "view_accounts";
	}

}
