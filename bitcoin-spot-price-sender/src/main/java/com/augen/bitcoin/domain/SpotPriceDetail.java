package com.augen.bitcoin.domain;

/**
 * The Spot Price data model to send to kafka server
 * @author quoca
 *
 */
public class SpotPriceDetail {

	private String currency;

	private double price;

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	public SpotPriceDetail() {
	}

	/**
	 * @param currency
	 * @param price
	 */
	public SpotPriceDetail(String currency, double price) {
		super();
		this.currency = currency;
		this.price = price;
	}

}
