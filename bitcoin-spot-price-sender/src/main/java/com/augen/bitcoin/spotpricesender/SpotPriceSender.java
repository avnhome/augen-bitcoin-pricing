package com.augen.bitcoin.spotpricesender;

import java.util.Optional;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.augen.bitcoin.domain.CoinBaseSpotPrice;
import com.augen.bitcoin.domain.SpotPriceDetail;
import com.augen.bitcoin.service.SpotPriceService;
/**
 * This class is about to send spot price data to kafka.
 * @author quoca
 *
 */
@Configuration
public class SpotPriceSender {

	/**
	 * defaultSpotPriceDetail this is a default spot price to send to kafka if there is an error when calling coinbase api.
	 */
	private final SpotPriceDetail defaultSpotPriceDetail = new SpotPriceDetail("000", 0);

	@Autowired
	SpotPriceService spotPriceService;

	/**
	 * This supplier is about to get data from coinbase api per seconds then send result to kafka topic "spot-price"
	 * If there is an error when calling coinbase api then a defaultSpotPriceDetail will be sent.
	 * @return
	 */
	@Bean
	public Supplier<SpotPriceDetail> sendEvents() {

		return () -> {
			System.out.println("Send new data: ");
			Optional<CoinBaseSpotPrice> coinBaseSpotPrice = spotPriceService.getSpotPriceByCurrency("NZD");
			if (coinBaseSpotPrice.isPresent()) {
				SpotPriceDetail spotPriceDetail = new SpotPriceDetail();
				spotPriceDetail.setCurrency(coinBaseSpotPrice.get().getData().getCurrency());
				spotPriceDetail.setPrice(Double.valueOf(coinBaseSpotPrice.get().getData().getAmount()));
				return spotPriceDetail;
			} else {
				return defaultSpotPriceDetail;
			}
		};
	}
}
