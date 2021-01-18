package com.augen.bitcoin.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.augen.bitcoin.domain.CoinBaseSpotPrice;

/**
 * this class is about to get spot price from coinbase api
 * @author quoca
 *
 */
@Service
public class SpotPriceService {

	/**
	 * The getSpotPriceByCurrency calls coinbase spot price api to get latest spot price.
	 * @param currency
	 * @return Optional object of CoinBaseSpotPrice or Optional.empty()
	 */
	public Optional<CoinBaseSpotPrice> getSpotPriceByCurrency(String currency) {
		System.out.println("getSpotPriceByCurrency- String currency : " + currency);
		String uri = "https://api.coinbase.com/v2/prices/spot?currency=" + currency;
		System.out.println("uri: " + uri);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<CoinBaseSpotPrice> response = restTemplate.getForEntity(uri, CoinBaseSpotPrice.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return Optional.of(response.getBody());
		} else {
			System.out.println("Error while calling coinbase Api");
			return Optional.empty();
		}

	}
}
