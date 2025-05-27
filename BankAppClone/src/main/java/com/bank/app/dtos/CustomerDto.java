/**
 * 
 */
package com.bank.app.dtos;

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
public class CustomerDto {
	private String cellphoneNumber;
    private String name;
    private String surname;
    private int pin;
    private String email;
}
