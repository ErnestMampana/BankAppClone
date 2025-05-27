package com.bank.app.repositories;

import com.bank.app.models.Account;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
	Optional<Account> findByAccountNumber(int accountNumber);
	List<Account> findAllByCustomerId(Long customerId);
}
