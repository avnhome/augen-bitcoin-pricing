package com.augen.bitcoin.pricingapi;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.augen.bitcoin.controller.BitcoinPricingController;
import com.augen.bitcoin.domain.Quote;
import com.augen.bitcoin.service.BitcoinPricingService;

@WebMvcTest(BitcoinPricingController.class)
public class BitcoinPricingControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private BitcoinPricingService service;

	@Test
	public void contextLoads() {
	}

	@Test
	public void givenQuote_whenDoQuoteByBTC_thenCheckTheTotalPrice() throws Exception {

		System.out.println("givenQuote_whenDoQuoteByBTC_thenCheckTheTotalPrice");
		Quote quote = new Quote();
		quote.setAmount(1000);
		quote.setId(UUID.randomUUID().toString());
		quote.setProfitFator(0.05);
		quote.setSpotPrice(5000);
		quote.setTotalPrice(1000 * 1.05 * 5000);

		when(service.quoteByBTC("NZD", 1000)).thenReturn(Optional.of(quote));

		this.mvc.perform(get("/quotes/btc?currency=NZD&amount=1000")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("5250000")))
				.andExpect(content().string(containsString("totalPrice")));

	}
}
