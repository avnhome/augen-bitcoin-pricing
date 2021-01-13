package com.augen.bitcoin.domain;

/**
 * This is data model for coinbase spot price api result
 * @author quoca
 *
 */
public class CoinBaseSpotPriceData {
	 private String amount;
	 private String currency;


	 // Getter Methods 

	 public String getAmount() {
	  return amount;
	 }

	 public String getCurrency() {
	  return currency;
	 }

	 // Setter Methods 

	 public void setAmount(String amount) {
	  this.amount = amount;
	 }

	 public void setCurrency(String currency) {
	  this.currency = currency;
	 }
	}