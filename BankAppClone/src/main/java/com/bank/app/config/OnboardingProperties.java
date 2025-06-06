package com.bank.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Component
@ConfigurationProperties(prefix = "")
@Data
public class OnboardingProperties {
	
	private double onboardingAmount;
	private String reference;

}
