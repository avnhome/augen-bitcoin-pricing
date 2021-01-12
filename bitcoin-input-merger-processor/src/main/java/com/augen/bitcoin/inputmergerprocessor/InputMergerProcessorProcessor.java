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

@Configuration
@EnableBinding(InputMergerCustomProcessor.class)
public class InputMergerProcessorProcessor {

	private AtomicReference<Double> profitFactor = new AtomicReference<Double>();
	private ConcurrentHashMap<String, SpotPriceDetail> spotPrices = new ConcurrentHashMap<String, SpotPriceDetail>();
	private final PriceFactorDetail defaultPriceFactorDetail = new PriceFactorDetail(0, "000", 0);
	
	@StreamListener(InputMergerCustomProcessor.SPOT_PRICE_INPUT)
	@SendTo(InputMergerCustomProcessor.INPUT_MERGER_OUTPUT)
	public PriceFactorDetail processSpotPrice(SpotPriceDetail spotPriceDetail) {
		System.out.println("New processSpotPrice: " + spotPriceDetail.getPrice());
		String currency = spotPriceDetail.getCurrency();
		System.out.println("currency: " + currency);
		spotPrices.put(currency, spotPriceDetail);
		return buildPriceFactorDetail(currency);

	}

	@StreamListener(InputMergerCustomProcessor.PROFIT_FACTOR_INPUT)
	@SendTo(InputMergerCustomProcessor.INPUT_MERGER_OUTPUT)
	public PriceFactorDetail processProfitFactor(ProfitFactorDetail profitFactorDetail) {
		System.out.println("New processProfitFactor: " + profitFactorDetail.getProfitFatorValue());
		profitFactor.set(profitFactorDetail.getProfitFatorValue());
		return buildPriceFactorDetail("NZD");
	}

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
