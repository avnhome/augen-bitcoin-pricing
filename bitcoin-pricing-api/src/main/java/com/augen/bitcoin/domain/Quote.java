package com.augen.bitcoin.domain;

public class Quote {

	public Quote() { }

	private String id;
	private double spotPrice;
	private double profitFator;
	private int amount;
	private double totalPrice;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the spotPrice
	 */
	public double getSpotPrice() {
		return spotPrice;
	}
	/**
	 * @param spotPrice the spotPrice to set
	 */
	public void setSpotPrice(double spotPrice) {
		this.spotPrice = spotPrice;
	}
	/**
	 * @return the profitFator
	 */
	public double getProfitFator() {
		return profitFator;
	}
	/**
	 * @param profitFator the profitFator to set
	 */
	public void setProfitFator(double profitFator) {
		this.profitFator = profitFator;
	}
	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	/**
	 * @return the totalPrice
	 */
	public double getTotalPrice() {
		return totalPrice;
	}
	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	
	
}
