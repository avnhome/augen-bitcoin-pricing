package com.augen.bitcoin.inputmergerprocessor;

import java.beans.PropertyChangeEvent;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;

import com.augen.bitcoin.domain.PriceFactorDetail;
import com.augen.bitcoin.domain.ProfitFactorDetail;
import com.augen.bitcoin.domain.SpotPriceDetail;
import com.augen.bitcoin.processor.InputMergerCustomProcessor;
import com.augen.bitcoin.registry.ProfitFactorRegistry;
import com.augen.bitcoin.registry.SpotPriceRegistry;

/**
 * This class is about to listen to kafka topic "spot-price" and "profit-factor"
 * then merge kafka message from both topic. After that It send the merged
 * result to kafka topic "input-merger"
 * 
 * @author quoca
 *
 */

@Configuration
@EnableBinding(InputMergerCustomProcessor.class)
public class InputMergerProcessorProcessor {

	/**
	 * This is default price factor (merged result) if there is an error occurs.
	 */
	private final PriceFactorDetail defaultPriceFactorDetail = new PriceFactorDetail(0, "000", 0);

	/**
	 * This is in-memory profitFactorRegistry to store latest profit factor, It is
	 * thread-safe as well.
	 */
	@Autowired
	private ProfitFactorRegistry profitFactorRegistry;
	/**
	 * This is in-memory spot price to store latest spot price of a currency. It is
	 * thread-safe as well.
	 */
	@Autowired
	private SpotPriceRegistry spotPricesRegistry;

	@Autowired
	private InputMergerCustomProcessor inputMergerCustomProcessor;

	/**
	 * This method listens at "profit-factor" topic and sends merged result to
	 * "input-merger" topic
	 * 
	 * @param spotPriceDetail the incoming data
	 * @return PriceFactorDetail the outgoing data
	 */
	@StreamListener(InputMergerCustomProcessor.SPOT_PRICE_INPUT)

	public void processSpotPrice(SpotPriceDetail spotPriceDetail) {
		System.out.println("New processSpotPrice: " + spotPriceDetail.getPrice());
		String currency = spotPriceDetail.getCurrency();
		System.out.println("currency: " + currency);
		spotPricesRegistry.setSpotPrice(currency, spotPriceDetail);
		//propertyChange();

	}

	/**
	 * This method listens at "spot-price" topic and sends merged result to
	 * "input-merger" topic
	 * 
	 * @param profitFactorDetail the incoming data
	 * @return PriceFactorDetail the outgoing data
	 */
	@StreamListener(InputMergerCustomProcessor.PROFIT_FACTOR_INPUT)
	public void processProfitFactor(ProfitFactorDetail profitFactorDetail) {
		System.out.println("New processProfitFactor: " + profitFactorDetail.getProfitFatorValue());
		profitFactorRegistry.setProfitFactor(profitFactorDetail.getProfitFatorValue());
		propertyChange();
	}

	//

	public void propertyChange() {
		System.out.println("propertyChange 8888");
		for (String currency : spotPricesRegistry.getSpotPrices().keySet()) {
			sendPriceFactorDetail(currency);
		}

	}

	@Async
	private void sendPriceFactorDetail(String currency) {

		boolean result = this.inputMergerCustomProcessor.inputMergerOutput()
				.send(MessageBuilder.withPayload(buildPriceFactorDetail(currency)).build());
		System.out.println("Sent: " + result + currency);
	}

	/**
	 * This method helps to build the merged result for sending to "input merger"
	 * topic. when a spot price or profit factor is comming. it will get latest
	 * profitFactorRegistry and spot price then build the PriceFactorDetail
	 * 
	 * @param currency
	 * @return
	 */
	private PriceFactorDetail buildPriceFactorDetail(String currency) {
		System.out.println("profitFactorRegistry.get(): " + profitFactorRegistry.getProfitFactor());

		System.out.println("spotPricesRegistry.get(currency): " + spotPricesRegistry.getSpotPrice(currency).toString());
		if (profitFactorRegistry.getProfitFactor() != null && spotPricesRegistry.getSpotPrice(currency) != null) {
			SpotPriceDetail spotPrice = spotPricesRegistry.getSpotPrice(currency);
			PriceFactorDetail priceFactorDetail = new PriceFactorDetail();

			priceFactorDetail.setCurrency(spotPrice.getCurrency());
			priceFactorDetail.setPrice(spotPrice.getPrice());
			priceFactorDetail.setProfitFatorValue(profitFactorRegistry.getProfitFactor());
			System.out.println(priceFactorDetail.toString());
			return priceFactorDetail;
		} else {
			return defaultPriceFactorDetail;
		}

	}

}
