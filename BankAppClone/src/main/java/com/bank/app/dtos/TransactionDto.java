/**
 * 
 */
package com.bank.app.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bank.app.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ernest Mampana
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {

	private int sourceAccountNumber;
	private int destinationAccountNumber;
	private LocalDateTime transactionDate;
	private TransactionType type;
	private BigDecimal amount;
	private String reference;
}
