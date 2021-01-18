package com.augen.bitcoin.service;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.augen.bitcoin.domain.PriceFactorDetail;
import com.augen.bitcoin.domain.Quote;
import com.augen.bitcoin.formula.PricingFormula;
import com.augen.bitcoin.kafka.NewCurrencySender;
import com.augen.bitcoin.registry.PriceDetailRegistry;
import com.augen.bitcoin.utils.RoundMethod;

/**
 * This is a business service for api.
 * @author quoca
 *
 */
@Service
public class BitcoinPricingServiceImpl implements BitcoinPricingService {

	@Autowired
	private PriceDetailRegistry coinPriceRegistry;

	@Autowired
	private NewCurrencySender newCurrencySender;
	/**
	 * {@inheritDoc}
	 */
	public Optional<Quote> quoteByBTC(String currency, int amount) {
		Quote quote = new Quote();
		Optional<PriceFactorDetail> priceFactorDetail = coinPriceRegistry.getCoinPriceByCurrency(currency);
		if (priceFactorDetail.isPresent()) {
			double profitFactor, spotPrice;
			profitFactor = priceFactorDetail.get().getProfitFatorValue();
			spotPrice = priceFactorDetail.get().getPrice();

			quote.setAmount(amount);
			quote.setId(UUID.randomUUID().toString());

			System.out
					.println("priceFactorDetail.getProfitFatorValue()" + priceFactorDetail.get().getProfitFatorValue());
			quote.setProfitFator(profitFactor);
			quote.setSpotPrice(RoundMethod.Round(spotPrice, 2));
			quote.setTotalPrice(RoundMethod.Round(PricingFormula.buyOrSellPrice(spotPrice, profitFactor, amount), 2));
			return Optional.of(quote);
		} else {
			newCurrencySender.sendCurrency(currency);
			return Optional.empty();

		}

	}

}
