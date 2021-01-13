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

/**
 * This method is about to listens at input-merger kafka topic.
 * @author quoca
 *
 */
@Configuration
public class BitcoinPricingLogger {

	private static final Logger logger = LoggerFactory.getLogger(BitcoinPriceApiApplication.class);

	/**
	 * This registry is an in memory database to store latest price and profit factor of a currency.
	 */
	@Autowired
	private PriceDetailRegistry coinPriceRegistry;

	/**
	 * his method is about to listens at input-merger kafka topic.
	 * When receive an incoming message, then add it to coin price registy.
	 * if the result return true then it is a change then log it
	 * if not, then no need to log
	 * @return
	 */
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