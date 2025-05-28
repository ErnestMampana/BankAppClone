/**
 * 
 */
package com.bank.app.controllers;

import com.bank.app.dtos.CustomerDto;
import com.bank.app.serviceImp.CustomerServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * @author Ernest Mampana
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {
	private final CustomerServiceImpl customerService;

	@PostMapping("/register")
	public ResponseEntity<CustomerDto> onBoardingCustomerr(@RequestBody CustomerDto customer) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerService.onBoardingCustomer(customer));
	}

}
