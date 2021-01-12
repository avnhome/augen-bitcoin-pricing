package com.augen.bitcoin.exception;

public class CurrencyNotFoundException extends RuntimeException {

	public CurrencyNotFoundException(String currency) {
		super(currency + " Not Found, This Currency will be available soon");
	}
}
