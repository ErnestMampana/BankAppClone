/**
 * 
 */
package com.bank.app.serviceImp;

import java.util.Random;

import com.bank.app.services.PartyService;

/**
 * @author Ernest Mampana
 *
 */
public class PartyServiceImpl implements PartyService {

	@Override
	public Float generateAccountNumber() {
    	final float max = 999999999;
		final float min = 100000000;
		Random random = new Random();
		float accountNumber = random.nextFloat((max - min) + 1) + min;
		return accountNumber;
    }

}
