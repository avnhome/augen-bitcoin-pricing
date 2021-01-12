package com.augen.bitcoin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.augen.bitcoin.domain.Quote;
import com.augen.bitcoin.exception.CurrencyNotFoundException;
import com.augen.bitcoin.service.BitcoinPricingService;
@RestController
public class BitcoinPricingController {

	
	@Autowired
	private BitcoinPricingService bitcoinPricingService;
	
	 @GetMapping("/quote")
	 public Quote quoteByBTC(@RequestParam(name = "currency", defaultValue = "NZD") String currency, @RequestParam(name = "amount", defaultValue = "0") int amount) {
	    return bitcoinPricingService.quoteByBTC(currency, amount).orElseThrow(() -> new CurrencyNotFoundException(currency));
	  }
}
