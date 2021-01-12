package com.augen.bitcoin.formula;

public class PricingFormula {

	public static double profitMargin(double spotPrice, double profitFactor, int amount) {
		return spotPrice * profitFactor * amount;
	}
	public static double buyOrSellPrice(double spotPrice, double profitFactor, int amount) {
		return (spotPrice * amount) + profitMargin(spotPrice, profitFactor, amount);
	}
}
