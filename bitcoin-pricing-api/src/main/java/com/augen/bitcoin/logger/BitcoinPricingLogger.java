package com.augen.bitcoin.logger;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.augen.bitcoin.BitcoinPriceApiApplication;
import com.augen.bitcoin.domain.PriceFactorDetail;
import com.augen.bitcoin.registry.PriceDetailRegistry;

@Configuration
public class BitcoinPricingLogger {

	private static final Logger logger = LoggerFactory.getLogger(BitcoinPriceApiApplication.class);

	@Autowired
	private PriceDetailRegistry coinPriceRegistry;

	@Bean
	public Consumer<PriceFactorDetail> process() {
		return priceFactorDetail -> {

			boolean isAdded = coinPriceRegistry.addNewCoinPrice(priceFactorDetail.getCurrency(), priceFactorDetail);
			if (isAdded) {
				logger.info(priceFactorDetail.toString());
			}
		};
	}
}