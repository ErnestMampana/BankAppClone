/**
 * 
 */
package com.bank.app.services;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import com.bank.app.dtos.AuthenticationRequestDto;
import com.bank.app.dtos.CustomerDto;
import com.bank.app.models.Account;
import com.bank.app.models.Customer;


/**
 * @author Ernest Mampana
 *
 */
public interface CustomerService {

	CustomerDto onBoardingCustomer (CustomerDto customer);

	void saveUserToken(Customer customer, String jwtToken);

	void revokeAllUserTokens(Customer customer);

	List<Account> login(AuthenticationRequestDto loginDto) throws AccountNotFoundException;
	
	Customer getCustomerById(Long id);
}
