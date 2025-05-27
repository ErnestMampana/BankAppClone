/**
 * 
 */
package com.bank.app.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.bank.app.models.Account;
import com.bank.app.services.AccountService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;

/**
 * @author Ernest Mampana
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
	private final AccountService accountService;

    @PostMapping("/{cellphoneNumber}")
    public ResponseEntity<Void> addAccountForUser(@PathVariable String cellphoneNumber) {
        accountService.addAccountForUser(cellphoneNumber);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/hello")
    public String sahe(){
        System.out.println("Yes we got it.....");
        return "Hi thereeeeee";
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable("accountNumber") int accountNumber) {
        return ResponseEntity.ok(accountService.getAccountBalance(accountNumber));
    }
    
    @GetMapping("/viewAll")
    public ResponseEntity<List<Account>> viewAllAccounts(){
    	return ResponseEntity.ok(accountService.getAllAccounts());
    }
    
    @GetMapping("/user/{cellphone}")
    public ResponseEntity<List<Account>> viewAccountsForUser(@PathVariable("cellphone") String cellphone){
    	return ResponseEntity.ok(accountService.viewAccountsForUser(cellphone));
    }

//    @GetMapping("/{accountNumber}/transactions")
//    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable float accountNumber, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//        List<Transaction> transactions = accountService.getTransactionHistory(accountNumber, startDate, endDate);
//        return ResponseEntity.ok(transactions);
//    }

}
