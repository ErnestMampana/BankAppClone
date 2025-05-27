package com.bank.app.repositories;

import com.bank.app.models.Customer;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
	
	Optional<Customer> findByCellphoneNumber(String cellphoneNumber);
}
