/**
 * 
 */
package com.bank.app.serviceImp;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import com.bank.app.auth.Token;
import com.bank.app.dtos.AuthenticationRequestDto;
import com.bank.app.dtos.CustomerDto;
import com.bank.app.enums.Role;
import com.bank.app.enums.TokenType;
import com.bank.app.exceptions.ClientException;
import com.bank.app.models.Account;
import com.bank.app.models.Customer;
import com.bank.app.repositories.CustomerRepository;
import com.bank.app.repositories.TokenRepository;
import com.bank.app.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ernest Mampana
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;

	private final AccountServiceImpl accountServiceImpl;

	private final TokenRepository tokenRepository;

	private final JwtService jwtService;

	private final AuthenticationManager authenticationManager;

	private final PasswordEncoder passwordEncoder;

	ModelMapper modelMapper = new ModelMapper();

	@Override
	public CustomerDto onBoardingCustomer(CustomerDto customerDto) {

		customerRepository.findByCellphoneNumber(customerDto.getCellphoneNumber()).ifPresent(cr -> {
			throw new ClientException(
					"Cellphone number " + customerDto.getCellphoneNumber() + " has already been taken");
		});

		Customer customer = modelMapper.map(customerDto, Customer.class);
		customer.setPin(passwordEncoder.encode(Integer.toString(customerDto.getPin())));
		customer.setRole(Role.USER);

		customerRepository.save(customer);

		log.info("Account created for {} ", customerDto.getName());

		accountServiceImpl.addAccountForUser(customerDto.getCellphoneNumber());

		var jwtToken = jwtService.generateToken(customer);

		saveUserToken(customer, jwtToken);

		return customerDto;
	}

//	public UserAccount getUserByEmail(String email) {
//
//		Optional<UserAccount> userOptional = userAccountRepository.findByEmail(email);
//
//		if (userOptional.isPresent()) {
//			UserAccount user = userOptional.get();
//			return user;
//		} else {
//			throw new ClientException("User not fond");
//		}
//
//	}

	@Override
	public void saveUserToken(Customer customer, String jwtToken) {
		var token = Token.builder().user(customer).token(jwtToken).tokenType(TokenType.BEARER).expired(false)
				.revoked(false).build();
		tokenRepository.save(token);
	}

	@Override
	public void revokeAllUserTokens(Customer customer) {
		var validUserTokens = tokenRepository.findAllValidTokenByCustomer(customer.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}

	@Override
	public List<Account> login(AuthenticationRequestDto loginDto) throws AccountNotFoundException {

		var user = customerRepository.findByCellphoneNumber(loginDto.getCellphoneNumber())
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getCellphoneNumber(), loginDto.getPin()));

		log.info("Useraccount Logged In ================= {} {}", user.getName(), user.getSurname());

		var jwtToken = jwtService.generateToken(user);

		revokeAllUserTokens(user);

		saveUserToken(user, jwtToken);

		return accountServiceImpl.viewAccountsForUser(user.getCellphoneNumber());
	}

	@Override
	public Customer getCustomerById(Long id) {
		return customerRepository.findById(id).get();
	}

}
