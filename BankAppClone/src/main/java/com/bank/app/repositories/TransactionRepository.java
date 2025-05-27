/**
 * 
 */
package com.bank.app.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bank.app.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Ernest Mampana
 *
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	Optional<List<Transaction>> findAllByAccountNumber(int accountNumber);

	Optional<List<Transaction>> findAllByAccountNumberAndTransactionDateBetween(int accountNumber,
			LocalDateTime startDate, LocalDateTime endDate);
}
