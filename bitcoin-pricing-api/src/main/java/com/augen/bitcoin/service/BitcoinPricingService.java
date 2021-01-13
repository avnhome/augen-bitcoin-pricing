package com.augen.bitcoin.service;

import java.util.Optional;
import com.augen.bitcoin.domain.Quote;

/**
 * This is an interface for bitcoin pricing service this is use for the Api
 * @author quoca
 *
 */
public interface BitcoinPricingService {

	/**
	 * This method helps us to generate the quote based on the currency and bitcoin amount
	 * @param currency
	 * @param amount
	 * @return Optional<Quote>
	 */
	Optional<Quote> quoteByBTC(String currency, int amount);
}
