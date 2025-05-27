/**
 * 
 */
package com.bank.app.auth;

import com.bank.app.enums.TokenType;
import com.bank.app.models.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ernest Mampana
 *
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

	@Id
	  @GeneratedValue
	  public Integer id;

	  @Column(unique = true)
	  public String token;

	  @Enumerated(EnumType.STRING)
	  public TokenType tokenType = TokenType.BEARER;

	  public boolean revoked;

	  public boolean expired;

	  @ManyToOne
	  @JoinColumn(name = "user_id")
	  public Customer user;
}
