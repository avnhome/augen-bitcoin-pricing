package com.augen.bitcoin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.augen.bitcoin.domain.Quote;
import com.augen.bitcoin.exception.CurrencyNotFoundException;
import com.augen.bitcoin.service.BitcoinPricingService;

/**
 * this is controller for bit coin pricing API
 * @author quoca
 *
 */
@RestController
public class BitcoinPricingController {

	
	@Autowired
	private BitcoinPricingService bitcoinPricingService;
	
	/**
	 * This method for quote api to create quote result based on currency and amount of bitcoin
	 * @param currency Currency
	 * @param amount amount of bitcoin
	 * @return Quote
	 */
	 @GetMapping("/quotes/btc")
	 public Quote quoteByBTC(@RequestParam(name = "currency", defaultValue = "NZD") String currency, @RequestParam(name = "amount", defaultValue = "0") int amount) {
	    return bitcoinPricingService.quoteByBTC(currency, amount).orElseThrow(() -> new CurrencyNotFoundException(currency));
	  }
		/**
		 * This method for quote api to create quote result based on currency and amount of bitcoin
		 * @param currency Currency
		 * @param amount amount of bitcoin
		 * @return Quote
		 */
		 @GetMapping("/quotes/money")
		 public Quote quoteByMoney(@RequestParam(name = "currency", defaultValue = "NZD") String currency, @RequestParam(name = "amount", defaultValue = "0") int amount) {
		    return bitcoinPricingService.quoteByMoney(currency, amount).orElseThrow(() -> new CurrencyNotFoundException(currency));
		  }
}
