/**
 * 
 */
package com.bank.app.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bank.app.enums.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * @author Ernest Mampana
 *
 */
@Entity
@Data
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime transactionDate;

	@Column(name = "amount", nullable = false, columnDefinition = "DECIMAL(10,2)")
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionType type;

	@ManyToOne
	private Account sourceAccount;

	@ManyToOne
	private Account destinationAccount;
	
	private int accountNumber;
	
	private String reference;

}
