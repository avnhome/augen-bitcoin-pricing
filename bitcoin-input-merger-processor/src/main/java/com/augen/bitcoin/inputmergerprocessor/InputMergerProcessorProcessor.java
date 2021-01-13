package com.augen.bitcoin.inputmergerprocessor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.SendTo;
import com.augen.bitcoin.domain.PriceFactorDetail;
import com.augen.bitcoin.domain.ProfitFactorDetail;
import com.augen.bitcoin.domain.SpotPriceDetail;
import com.augen.bitcoin.processor.InputMergerCustomProcessor;

/**
 * This class is about to listen to kafka topic "spot-price" and "profit-factor" then merge kafka message from both topic.
 * After that It send the merged result to kafka topic "input-merger"
 * @author quoca
 *
 */

@Configuration
@EnableBinding(InputMergerCustomProcessor.class)
public class InputMergerProcessorProcessor {

	/**
	 * This is in-memory profitFactor to store latest profit factor, It is thread-safe as well.
	 */
	private AtomicReference<Double> profitFactor = new AtomicReference<Double>();
	/**
	 * This is in-memory spot price to store latest spot price of a currency. It is thread-safe as well.
	 */
	private ConcurrentHashMap<String, SpotPriceDetail> spotPrices = new ConcurrentHashMap<String, SpotPriceDetail>();
	/**
	 * This is default price factor (merged result) if there is an error occurs.
	 */
	private final PriceFactorDetail defaultPriceFactorDetail = new PriceFactorDetail(0, "000", 0);
	

	/**
	 * This method listens at "profit-factor" topic and sends merged result to "input-merger" topic
	 * @param spotPriceDetail the incoming data
	 * @return PriceFactorDetail the outgoing data
	 */
	@StreamListener(InputMergerCustomProcessor.SPOT_PRICE_INPUT)
	@SendTo(InputMergerCustomProcessor.INPUT_MERGER_OUTPUT)
	public PriceFactorDetail processSpotPrice(SpotPriceDetail spotPriceDetail) {
		System.out.println("New processSpotPrice: " + spotPriceDetail.getPrice());
		String currency = spotPriceDetail.getCurrency();
		System.out.println("currency: " + currency);
		spotPrices.put(currency, spotPriceDetail);
		return buildPriceFactorDetail(currency);

	}

	/**
	 * This method listens at "spot-price" topic and sends merged result to "input-merger" topic
	 * @param profitFactorDetail the incoming data
	 * @return PriceFactorDetail the outgoing data
	 */
	@StreamListener(InputMergerCustomProcessor.PROFIT_FACTOR_INPUT)
	@SendTo(InputMergerCustomProcessor.INPUT_MERGER_OUTPUT)
	public PriceFactorDetail processProfitFactor(ProfitFactorDetail profitFactorDetail) {
		System.out.println("New processProfitFactor: " + profitFactorDetail.getProfitFatorValue());
		profitFactor.set(profitFactorDetail.getProfitFatorValue());
		return buildPriceFactorDetail("NZD");
	}

	/**
	 * This method helps to build the merged result for sending to "input merger" topic.
	 * when a spot price or profit factor is comming.
	 * it will get latest profitFactor and spot price then build the PriceFactorDetail
	 * @param currency
	 * @return
	 */
	private PriceFactorDetail buildPriceFactorDetail(String currency) {
		System.out.println("profitFactor.get(): " + profitFactor.get());
		
		System.out.println("spotPrices.get(currency): " + spotPrices.get(currency));
		if (profitFactor.get() != null && spotPrices.get(currency) != null) {
			SpotPriceDetail spotPrice = spotPrices.get(currency);
			PriceFactorDetail priceFactorDetail = new PriceFactorDetail();

			priceFactorDetail.setCurrency(spotPrice.getCurrency());
			priceFactorDetail.setPrice(spotPrice.getPrice());
			priceFactorDetail.setProfitFatorValue(profitFactor.get());
			return priceFactorDetail;
		} else {
			return defaultPriceFactorDetail;
		}

	}
}
