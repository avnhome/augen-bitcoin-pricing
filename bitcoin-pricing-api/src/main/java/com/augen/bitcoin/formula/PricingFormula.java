package com.augen.bitcoin.formula;

/**
 * This is all about Pricing formula
 * @author quoca
 *
 */
public class PricingFormula {

	/**
	 * This method helps use to calculate profit margin
	 * Profit Margin = Spot price * profit factor * amount
	 * @param spotPrice
	 * @param profitFactor
	 * @param amount
	 * @return
	 */
	public static double profitMargin(double spotPrice, double profitFactor, int amount) {
		return spotPrice * profitFactor * amount;
	}
	
	/**
	 * This method help us to calculate the buy or sell price of bitcoin
	 * Buy or Sell Price = (Spot price *  amount) + Profit Margin
	 * @param spotPrice
	 * @param profitFactor
	 * @param amount
	 * @return
	 */
	public static double buyOrSellPrice(double spotPrice, double profitFactor, int amount) {
		return (spotPrice * amount) + profitMargin(spotPrice, profitFactor, amount);
	}
}
