/**
 * 
 */
package com.bank.app.exceptions;

/**
 * @author Ernest Mampana
 *
 */
public class ClientException extends RuntimeException{
	private int code;

	public ClientException(String message) {
		this(message, 400);// default to bad request
	}

	public ClientException(String message, int code) {
		super(message);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
