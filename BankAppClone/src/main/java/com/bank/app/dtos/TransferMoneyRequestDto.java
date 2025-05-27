/**
 * 
 */
package com.bank.app.dtos;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author Ernest Mampana
 *
 */
@Data
public class TransferMoneyRequestDto {
	private int sourceAccountNumber;
    private int destinationAccountNumber;
    private BigDecimal amount;
    private String reference;
}
