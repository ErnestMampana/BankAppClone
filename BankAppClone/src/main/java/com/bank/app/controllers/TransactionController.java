/**
 * 
 */
package com.bank.app.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.bank.app.dtos.DebitCreditDto;
import com.bank.app.dtos.TransactionDto;
import com.bank.app.dtos.TransferMoneyRequestDto;
import com.bank.app.models.Account;
import com.bank.app.services.TransactionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

/**
 * @author Ernest Mampana
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

	private final TransactionService transactionService;

	@PutMapping("/external/transfer")
	public ResponseEntity<Void> transferMoneyExternal(@RequestBody TransferMoneyRequestDto request) {
		transactionService.transferMoney(request.getSourceAccountNumber(), request.getDestinationAccountNumber(),
				request.getAmount(), request.getReference());
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@PutMapping("/internal/transfer")
	public ResponseEntity<Void> transferMoneyInternal(@RequestBody TransferMoneyRequestDto request) {
		transactionService.internalMoneyTransfer(request.getSourceAccountNumber(),
				request.getDestinationAccountNumber(), request.getAmount(), request.getReference());
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@GetMapping("/{accountNumber}/transactions")
	public ResponseEntity<List<TransactionDto>> getTransactionsByAccountNumber(
			@PathVariable("accountNumber") int accountNumber) {
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(transactionService.getTransactionsByAccountNumber(accountNumber));
	}

	@PostMapping("/debit")
	public ResponseEntity<String> debitCustomerAccount(@RequestBody DebitCreditDto dto) {
		return ResponseEntity.status(HttpStatus.OK).body(
				transactionService.debitCustomerAccount(dto.getAccountNumber(), dto.getAmount(), dto.getReference()));
	}

	@PostMapping("/credit")
	public ResponseEntity<String> creditCustomerAccount(@RequestBody DebitCreditDto dto) {
		return ResponseEntity.status(HttpStatus.OK).body(
				transactionService.creditCustomerAccount(dto.getAccountNumber(), dto.getAmount(), dto.getReference()));
	}

	@GetMapping("/{accountNumber}/filtered/transactions")
	public ResponseEntity<List<TransactionDto>> getFilteredTransactionsByAccountNumber(
			@PathVariable("accountNumber") int accountNumber, @RequestParam LocalDate startDate,
			@RequestParam LocalDate endDate) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(transactionService.getFilteredTransactionsByAccountNumber(accountNumber, startDate, endDate));
	}

//	@PostMapping("/deposit")
//	public ResponseEntity<String> deposit(@RequestBody DebitCreditDto dto) {
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(transactionService.depositMoney(dto.getAccountNumber(), dto.getAmount(), dto.getReference()));
//	}

}
