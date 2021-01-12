package com.augen.bitcoin.service;

import java.util.Optional;
import com.augen.bitcoin.domain.Quote;

public interface BitcoinPricingService {

	Optional<Quote> quoteByBTC(String currency, int amount);
}
