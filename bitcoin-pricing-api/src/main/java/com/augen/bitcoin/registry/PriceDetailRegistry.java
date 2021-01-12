package com.augen.bitcoin.registry;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import com.augen.bitcoin.domain.PriceFactorDetail;

@Component
public class PriceDetailRegistry {

	private ConcurrentHashMap<String, PriceFactorDetail> coinPrices = new ConcurrentHashMap<>();

	public synchronized boolean addNewCoinPrice(String key, PriceFactorDetail priceFactorDetail) {
		if (isExisting(key, priceFactorDetail)) {
			return false;
		} else {
			coinPrices.put(key, priceFactorDetail);
			return true;
		}

	}

	public Optional<PriceFactorDetail> getCoinPriceByCurrency(String key) {
		if (containsKey(key)) {
			return Optional.ofNullable(coinPrices.get(key));
		} else {
			return Optional.empty();
		}
	}

	private boolean containsKey(String key) {
		return coinPrices.containsKey(key);
	}

	private boolean isExisting(String key, PriceFactorDetail usageCostDetail) {
		return coinPrices.get(key) != null && coinPrices.get(key).equals(usageCostDetail);
	}
}
