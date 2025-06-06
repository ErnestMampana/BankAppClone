package com.bank.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.bank.app.config.OnboardingProperties;

@SpringBootApplication
public class BankAppCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAppCloneApplication.class, args);
	}

}
