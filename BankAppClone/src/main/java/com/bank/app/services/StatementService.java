package com.bank.app.services;

import java.sql.Date;

import com.bank.app.models.Account;

public interface StatementService {
	
	public void generateStatement(Account acc,int period);

}
