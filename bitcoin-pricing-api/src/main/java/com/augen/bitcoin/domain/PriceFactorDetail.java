package com.augen.bitcoin.domain;

import java.util.Objects;

public class PriceFactorDetail {

	private double profitFatorValue;
	private String currency;

	private double price;

	/**
	 * @return the profitFatorValue
	 */
	public double getProfitFatorValue() {
		return profitFatorValue;
	}

	/**
	 * @param profitFatorValue the profitFatorValue to set
	 */
	public void setProfitFatorValue(double profitFatorValue) {
		this.profitFatorValue = profitFatorValue;
	}

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

	/**
	 * 
	 */
	public PriceFactorDetail() {
		
	}
	
	

	/**
	 * @param profitFatorValue
	 * @param currency
	 * @param price
	 */
	public PriceFactorDetail(double profitFatorValue, String currency, double price) {
		super();
		this.profitFatorValue = profitFatorValue;
		this.currency = currency;
		this.price = price;
	}

	@Override
	public String toString() {
		return "PriceFactorDetail [profitFatorValue=" + profitFatorValue + ", currency=" + currency + ", price=" + price
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(currency, price, profitFatorValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PriceFactorDetail)) {
			return false;
		}
		PriceFactorDetail other = (PriceFactorDetail) obj;
		return Objects.equals(currency, other.currency)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Double.doubleToLongBits(profitFatorValue) == Double.doubleToLongBits(other.profitFatorValue);
	}
	
	
	
}
