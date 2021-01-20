package com.augen.bitcoin.pricingapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.augen.bitcoin.domain.PriceFactorDetail;
import com.augen.bitcoin.domain.Quote;
import com.augen.bitcoin.kafka.NewCurrencySender;
import com.augen.bitcoin.registry.PriceDetailRegistry;
import com.augen.bitcoin.service.BitcoinPricingService;
import com.augen.bitcoin.service.BitcoinPricingServiceImpl;

@WebMvcTest(BitcoinPricingServiceImpl.class)
public class BitcoinPricingServiceTests {

	@Autowired
	private BitcoinPricingService bitcoinPricingService;

	@MockBean
	private PriceDetailRegistry coinPriceRegistry;

	@MockBean
	private NewCurrencySender newCurrencySender;
	
	@Test
	public void contextLoads() {
	}

	@Test
	public void givenPriceFactorDetail_whengetCoinPriceByCurrency_thenQuote() throws Exception {

		System.out.println("givenPriceFactorDetail_whengetCoinPriceByCurrency_thenQuote");

		PriceFactorDetail priceFactorDetail = new PriceFactorDetail();
		priceFactorDetail.setCurrency("NZD");
		priceFactorDetail.setPrice(5000);
		priceFactorDetail.setProfitFatorValue(0.05);
		when(coinPriceRegistry.getCoinPriceByCurrency("NZD")).thenReturn(Optional.of(priceFactorDetail));

		Optional<Quote> quote = this.bitcoinPricingService.quoteByBTC("NZD", 1000);
		assertThat(quote.get().getTotalPrice()).isEqualTo(1000 * 5000 * 1.05);
		assertThat(quote.get().getProfitFator()).isEqualTo(0.05);
		assertThat(quote.get().getSpotPrice()).isEqualTo(5000);
		assertThat(quote.get().getAmount()).isEqualTo(1000);

	}
}
