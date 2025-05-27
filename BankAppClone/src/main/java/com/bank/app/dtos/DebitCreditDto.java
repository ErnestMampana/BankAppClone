/**
 * 
 */
package com.bank.app.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ernest Mampana
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitCreditDto {
	
	private int accountNumber;
	private BigDecimal amount;
	private String reference;

}
