package com.augen.bitcoin.spotpricesender;

import java.util.Optional;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.augen.bitcoin.domain.CoinBaseSpotPrice;
import com.augen.bitcoin.domain.SpotPriceDetail;
import com.augen.bitcoin.service.SpotPriceService;

@Configuration
public class SpotPriceSender {

	private final SpotPriceDetail defaultSpotPriceDetail = new SpotPriceDetail("000", 0);

	@Autowired
	SpotPriceService spotPriceService;

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
