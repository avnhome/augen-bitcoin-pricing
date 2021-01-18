package com.augen.bitcoin.spotpricesender;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.augen.bitcoin.domain.CoinBaseSpotPrice;
import com.augen.bitcoin.domain.SpotPriceDetail;
import com.augen.bitcoin.registry.CurrencyRegistry;
import com.augen.bitcoin.service.SpotPriceService;

/**
 * This class is about to send spot price data to kafka.
 * 
 * @author quoca
 *
 */
@EnableScheduling
@EnableBinding(Source.class)
public class SpotPriceSender {

	/**
	 * defaultSpotPriceDetail this is a default spot price to send to kafka if there
	 * is an error when calling coinbase api.
	 */
	private final SpotPriceDetail defaultSpotPriceDetail = new SpotPriceDetail("000", 0);

	@Autowired
	private SpotPriceService spotPriceService;

	@Autowired
	private Source source;

	private SpotPriceDetail spotPriceDetail;

	@Autowired
	private CurrencyRegistry currencyRegistry;

	/**
	 * This supplier is about to get data from coinbase api per seconds then send
	 * result to kafka topic "spot-price" If there is an error when calling coinbase
	 * api then a defaultSpotPriceDetail will be sent.
	 * 
	 * @return
	 */
	@Scheduled(fixedDelay = 1000)
	public void sendEvents() {

		Set<String> currencies = currencyRegistry.getCurrencies();
		for (String currency : currencies) {
			sendSpotPrice(currency);
		}
	}

	@Async
	private void sendSpotPrice(String currency) {

		System.out.println("Send new data: " + currency);
		Optional<CoinBaseSpotPrice> coinBaseSpotPrice = spotPriceService.getSpotPriceByCurrency(currency);
		System.out.println("coinBaseSpotPrice: " + coinBaseSpotPrice);
		if (coinBaseSpotPrice.isPresent()) {
			spotPriceDetail = new SpotPriceDetail();
			spotPriceDetail.setCurrency(coinBaseSpotPrice.get().getData().getCurrency());
			spotPriceDetail.setPrice(Double.valueOf(coinBaseSpotPrice.get().getData().getAmount()));
		} else {
			spotPriceDetail = defaultSpotPriceDetail;
		}
		boolean result = this.source.output().send(MessageBuilder.withPayload(spotPriceDetail).build());
		System.out.println("Sent: " + result + currency);
	}
}
