package com.bank.app.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import com.bank.app.dtos.TransactionDto;
import com.bank.app.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;
    
    private int accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private Long customerId;
    
    @OneToMany
    private List<Transaction> transactions;
    
    
    public int generateAccountNumber() {
    	final int max = 999999999;
		final int min = 100000000;
		Random random = new Random();
		int accountNumber = random.nextInt((max - min) + 1) + min;
		return accountNumber;
    }
//    public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
//        List<Transaction> transactionsInRange = new ArrayList<>();
//        for (Transaction transaction : transactionHistory) {
//            LocalDate transactionDate = transaction.getDate();
//            if (transactionDate.isEqual(startDate) || transactionDate.isEqual(endDate) ||
//                (transactionDate.isAfter(startDate) && transactionDate.isBefore(endDate))) {
//                transactionsInRange.add(transaction);
//            }
//        }
//        return transactionsInRange;
//    }


}
