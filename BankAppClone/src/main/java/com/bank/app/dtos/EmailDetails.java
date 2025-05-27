/**
 * 
 */
package com.bank.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ernest Mampana
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDetails {

	private String recipient;
    private String message;
    private String subject;
    private String attachment;
}
