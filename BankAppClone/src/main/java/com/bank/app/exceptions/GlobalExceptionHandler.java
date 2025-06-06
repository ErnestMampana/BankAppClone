package com.bank.app.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ClientException.class)
	public ResponseEntity<Map<String, String>> handleClientException(ClientException ex) {
		Map<String, String> errorResponse = new HashMap<>();
		
		errorResponse.put("error", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
}
