package com.augen.bitcoin.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundMethod {
	public static double Round(double value, int decimal) {
		BigDecimal bigDecimal = new BigDecimal(value).setScale(decimal, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
	}

}
