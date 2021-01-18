package com.augen.bitcoin.registry;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class CurrencyRegistry {
	private Set<String> currencies = new ConcurrentHashMap<String, Boolean>().keySet(Boolean.TRUE);

	{
		currencies.add("NZD");
	}
	
	public boolean addNewCurrency(String currency) {
		return currencies.add(currency);
	}

	/**
	 * @return the currencies
	 */
	public Set<String> getCurrencies() {
		return currencies;
	}

}
